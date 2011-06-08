/*
 *    Copyright 2010-2011 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package savant.format;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.file.FieldType;
import savant.file.FileType;
import savant.file.SavantROFile;
import savant.util.Range;
import savant.util.SavantFileUtils;


/**
 * Class to perform formatting of biological data files (FASTA, BED, etc.) into Savant's binary formats.
 * Sometimes a separate index file is created. Occasionally, auxiliary files are created, such as
 * coverage files for BAM maps.
 *
 * @author mfiume
 */
public class DataFormatter {
    private static final Log LOG = LogFactory.getLog(DataFormatter.class);

    /**
     * Input path
     */
    private File inFile;

    /**
     * output path
     */
    private File outFile;

    public final File getInputFile() { return inFile; }

    public File getOutputFile() {
        if (getInputFileType() == FileType.INTERVAL_BAM) {
            return new File(inFile + ".cov.tdf").getAbsoluteFile();
        } else {
            return outFile;
        }
    }

    public FileType getInputFileType() { return inputFileType; }

    /**
     * File type
     */
    private FileType inputFileType;

    // property change support to make progress changes visible to UI
    // FIXME: figure out why PropertyChangeSupport does not work. Then get rid of FormatProgressListener and related stuff.
    // private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private List<FormatProgressListener> progressListeners = new ArrayList<FormatProgressListener>();

    // used to generalize 0 vs 1-based input files
    boolean isOneBased = true; // TRUE if 1-based; FALSE if 0-based

    /**
     * Establishes a data formatter which can be run by
     * calling the format() function.
     * @param inPath input file path
     * @param outPath output file path (should not already exist)
     * @param fileType type of the input file (e.g. interval, point, etc)
     * @param isInputOneBased whether or not the file is one-based (i.e.
     * annotation of 10 refers to the 10th position in the genome, not the 9th
     * as in zero-based scheme)
     */
    public DataFormatter(File inFile, File outFile, FileType fileType, boolean isInputOneBased) {
        this.inFile = inFile;   // set the desired input file path
        this.outFile = outFile; // set the desired output file path
        this.inputFileType = fileType;  // set the input file type (e.g. interval, point, etc)
        this.isOneBased = isInputOneBased;
    }

    /**
     * Establishes a data formatter which can be run by
     * calling the format() function.
     *  - sets one-based to true
     * @param inPath input file path
     * @param outPath output file path (should not already exist)
     * @param fileType type of the input file (e.g. interval, point, etc)
     */
    public DataFormatter(File inFile, File outFile, FileType fileType) {
        this(inFile, outFile, fileType, true);
    }

    /**
     * FUNCTIONS
     */

    /**
     * Format the input file path, storing the result in the output file path
     * @return whether or not the format was successful
     * @throws InterruptedException
     * @throws IOException
     * @throws ParseException
     */
    public boolean format() throws InterruptedException, IOException, ParseException, SavantFileFormattingException {

        if (inputFileType == FileType.INTERVAL_BAM) {
            // Create a coverage file from a BAM file.
            runFormatter(new BAMToCoverage(inFile));
        } else {
            // format files with Savant header

            // If necessary, check that it really is a text file
            if (inputFileType != FileType.INTERVAL_BIGBED && inputFileType != FileType.CONTINUOUS_BIGWIG && !verifyTextFile(inFile)) {
                throw new IOException("Input file is not a text file");
            }

            // format the input file in the appropriate way
            switch (inputFileType) {
                case POINT_GENERIC:
                    runFormatter(new PointGenericFormatter(inFile, outFile, isOneBased));
                    break;
                case INTERVAL_BED:
                case INTERVAL_GENERIC:
                    runFormatter(new TabixFormatter(inFile, outFile, isOneBased, inputFileType, 0, 1, 2, '#'));
                    break;
                case INTERVAL_GFF:
                    runFormatter(new TabixFormatter(inFile, outFile, isOneBased, inputFileType, 0, 3, 4, '#'));
                    break;
                case CONTINUOUS_GENERIC:
                    runFormatter(new ContinuousGenericFormatter(inFile, outFile));
                    break;
                case CONTINUOUS_WIG:
                    runFormatter(new TDFFormatter(inFile, outFile));
                    break;
                case SEQUENCE_FASTA:
                    runFormatter(new FastaFormatter(inFile, outFile));
                    break;
                case CONTINUOUS_BIGWIG:
                    runFormatter(new BigWigFormatter(inFile, outFile));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static Map<String,IntervalSearchTree> readIntervalBSTs(SavantROFile dFile) throws IOException {

        // read the refname -> index position map
        Map<String, long[]> refMap = SavantFileUtils.readReferenceMap(dFile);

        if (LOG.isDebugEnabled()) LOG.debug("\n=== DONE PARSING REF<->DATA MAP ===\n\n");

        // change the offset
        dFile.setHeaderOffset(dFile.getFilePointer());

        Map<String, IntervalSearchTree> trees = new HashMap<String, IntervalSearchTree>();

        int treenum = 0;

        if (LOG.isDebugEnabled()) LOG.debug("Number of trees to get: " + refMap.keySet().size());

        // keep track of the maximum end of tree position
        // (IMPORTANT NOTE: order of elements returned by keySet() is not gauranteed!!!)
        long maxend = Long.MIN_VALUE;
        for (String refname : refMap.keySet()) {
            long[] v = refMap.get(refname);
            if (LOG.isDebugEnabled()) LOG.debug("========== Reading tree for reference " + refname + " ==========");
            dFile.seek(v[0] + dFile.getHeaderOffset());

            if (LOG.isDebugEnabled()) LOG.debug("Starting tree at: " + dFile.getFilePointer());

            IntervalSearchTree t = readIntervalBST(dFile);

            if (LOG.isDebugEnabled()) LOG.debug("Finished tree at: " + dFile.getFilePointer());

            maxend = Math.max(maxend,dFile.getFilePointer());

            trees.put(refname, t);
            treenum++;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Read " + treenum + " trees (i.e. indicies)");
            LOG.debug("\n=== DONE PARSING REF<->INDEX MAP ===");
            LOG.debug("Changing offset from " + dFile.getHeaderOffset() + " to " + (dFile.getFilePointer()+dFile.getHeaderOffset()) + "\n");
        }

        // set the header offset appropriately
        dFile.setHeaderOffset(maxend);

        return trees;
    }

    /**
     * Reads an IntervalSearchTree from file
     * @param file The file containing the IntervalSearchTree. The IntervalSearchTree
     * must start at the current position in the file.
     * @return An IntervalSearchTree which was represented in the file
     * @throws IOException
     */
    private static IntervalSearchTree readIntervalBST(SavantROFile file) throws IOException {

        //RandomAccessFile file = SavantFileUtils.openFile(indexFileName, false);

        // the node list
        List<IntervalTreeNode> nodes = new ArrayList<IntervalTreeNode>();

        // fields for a node
        List<FieldType> fields = new ArrayList<FieldType>();
        fields.add(FieldType.INTEGER);  // node ID
        fields.add(FieldType.RANGE);    // range
        fields.add(FieldType.LONG);     // start position in file
        fields.add(FieldType.INTEGER);  // size
        fields.add(FieldType.INTEGER);  // subtree size
        fields.add(FieldType.INTEGER);  // parent index

        // map from node index to parent index
        HashMap<Integer,Integer> nodeIndex2ParentIndices = new HashMap<Integer,Integer>();

        int i = 0;

        // keep reading nodes until done
        while(true) {

            LOG.debug("Reading node at byte position: " + file.getFilePointer());

            // read in the node fields
            List<Object> r1;
            try {
                r1 = SavantFileUtils.readBinaryRecord(file, fields);
            } catch (EOFException e) {
                LOG.error("Hit EOF while trying to parse IntervalSearchTree from file");
                break;
            }

            // create an IntervalTreeNode
            IntervalTreeNode n = new IntervalTreeNode((Range) r1.get(1), (Integer) r1.get(0));
            if (n.index == -1) {
                LOG.debug("Tree contains " + i + " nodes");
                break;
            }   // the "null" terminator node has -1 as its index

            if (LOG.isDebugEnabled()) {
                LOG.debug("Node params read: ");
                for (int j = 0; j < 6; j++) {
                    LOG.debug(j + ". " + r1.get(j));
                }
            }

            n.startByte = (Long) r1.get(2);
            n.size = (Integer) r1.get(3);
            n.subtreeSize = (Integer) r1.get(4);
            nodeIndex2ParentIndices.put(n.index, (Integer) r1.get(5));

            if (LOG.isDebugEnabled()) LOG.debug("Node:\tindex: " + n.index + "\trange: " + n.range + "\tsize: " + n.size + "\tsubsize: " + n.subtreeSize + "\tbyte: " + n.startByte);


            // add this node to the list
            nodes.add(n);

            i++;
            LOG.debug((i) + ". Read node with range " + n.range + " and index " + n.index);
        }

        // sort node list by index
        Collections.sort(nodes);

        LOG.debug("Finished parsing IBST");

        // make a map of node to child indicies
       HashMap<Integer,List<Integer>> nodeIndex2ChildIndices = new HashMap<Integer,List<Integer>>();

       for (Integer key : nodeIndex2ParentIndices.keySet()) {
           int parent = nodeIndex2ParentIndices.get(key);
           if (!nodeIndex2ChildIndices.containsKey(parent)) {
               nodeIndex2ChildIndices.put(parent, new ArrayList<Integer>());
           }
           List<Integer> children = nodeIndex2ChildIndices.get(parent);
           children.add(key);
           nodeIndex2ChildIndices.put(parent, children);
       }

       for (Integer index : nodeIndex2ChildIndices.keySet()) {

           if (index == -1) { continue; }

           IntervalTreeNode n = nodes.get(index);
           List<Integer> cis = nodeIndex2ChildIndices.get(index);

           if (LOG.isDebugEnabled()) LOG.debug("Node " + n.index + " [ ");

           for (Integer childIndex : cis) {
               if (LOG.isDebugEnabled()) LOG.debug(childIndex + " ");
                n.children.add(nodes.get(childIndex));
           }

           if (LOG.isDebugEnabled()) LOG.debug("]");
       }

       return new IntervalSearchTree(nodes);
    }


    private boolean verifyTextFile(File fileName) {
        boolean result = false;
        BufferedReader reader=null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            char[] readBuf = new char[1000];
            int charsRead = reader.read(readBuf);
            if (charsRead != -1) {
                String readStr = new String(readBuf);
                if (readStr.contains("\r") || readStr.contains("\n")) {
                    // newline found in first 1000 characters, probably is a text file
                    result = true;
                }
            }
        } catch (IOException e) {
            // result will be false
        } finally {
            try { if (reader != null) reader.close(); } catch (IOException ignore) {}
        }
        return result;
    }

    public void addProgressListener(FormatProgressListener listener) {
        progressListeners.add(listener);
    }

    public void removeProgressListener(FormatProgressListener listener) {
        progressListeners.remove(listener);
    }

    private void runFormatter(SavantFileFormatter ff) throws IOException, InterruptedException, SavantFileFormattingException {
        for (FormatProgressListener listener : progressListeners) {
            ff.addProgressListener(listener);
        }
        ff.format();

        for (FormatProgressListener listener : progressListeners) {
            ff.removeProgressListener(listener);
        }
    }
}