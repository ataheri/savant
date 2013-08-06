/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package savant.geneontology;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import savant.settings.DirectorySettings;

/**
 *
 * @author Nirvana Nursimulu
 */
class XMLontology {
    
    public static String LOCATION_OF_GO_XML_FILE = "http://archive."
            + "geneontology.org/latest-termdb/go_daily-termdb.obo-xml.gz";
    
    /**
     * Makes and returns a tree using GO terms.
     * @param mapFile map of GO ID to genome locations.
     * @return the tree.
     */
    public static XTree makeTree(String mapFile) throws Exception{
        
        
        // The location of the file
        String locationOfFile = DirectorySettings.getTmpDirectory() + 
                File.separator + "TEMP_GOtree.xml";
        // The file to contain the XML file.
        File file = new File(locationOfFile);   
        file.deleteOnExit();
                
        InputStream stream = 
            (new URL(LOCATION_OF_GO_XML_FILE)).openStream();
        GZIPInputStream gzip = new GZIPInputStream(stream);
        OutputStream out = new FileOutputStream(file);
                
        byte[] buf = new byte[1024];
        int len;
                
        while((len = gzip.read(buf)) > 0){

            out.write(buf, 0, len);
        }

        
        XTree tree = new XTree(mapFile);
        
        Handler handler = new Handler(tree, locationOfFile);
        
        // Parse through the file, and get the xtree to be made and return it.
        parseXMLFile(true, handler);
        
        // No need to delete the file here: it is automatically deleted.
        return tree;
    }
    
    
    static class Handler extends DefaultHandler{
        
        /**
         * The tree to be populated via parsing.
         */
        private XTree tree;
        
        /**
         * The location of the file to be parsed.
         */
        private String locationOfFile;
        
        /**
         * The current node being filled.
         */
        private XNode currNode;
        
        /**
         * List of parents for the current node.
         */
        private List<String> currParents;
        
        /**
         * true iff a new term has been seen.
         */
        private boolean haveSeenTerm;
        
        /**
         * true iff the term which has just been seen is a root.
         */
        private boolean isRoot;
        
        /**
         * The string to be accumulated.
         */
        private String stringAcc;
        
//        private StringBuilder strBuilder;
        
        Handler(XTree tree, String locationOfFile){
            
            this.tree = tree;
            this.locationOfFile = locationOfFile;
            this.currNode = null;
            this.currParents = new ArrayList<String>();
            this.haveSeenTerm = false;
            this.isRoot = false;
            stringAcc = "";
//            strBuilder = new StringBuilder();
        }
        
        @Override
        /**
         * Receive notification of the start of an element.
         */
        public void startElement(String namespace, String localName, 
        String qName, Attributes attrs){
            
            // If ever we have an element of interest...
            if (qName.equals("term")){
                
                // say that we have detected a new term.
                haveSeenTerm = true;
            }
            
//            strBuilder = new StringBuilder();
            stringAcc = "";
        }
        
        @Override
        /**
         * Receive notification of the end of an element.
         */
        public void endElement(String uri, String localName, String qName){
            
                // Check that we have not received note of this term being obsolete
                // If see that term is obsolete, just stop recording this info.
                if (qName.equals("is_obsolete") && stringAcc.equals("1")){
                
                    currNode = null;
                    currParents.clear();
                    haveSeenTerm = false;
                }
                // If this term is not obsolete and we have seen the start of a term
                else if (haveSeenTerm){

                    // If we have reached the end of an element of interest, 
                    // add it to the tree.
                    if (qName.equals("term") && currNode != null){

                        // If this is not a root, do a simple addition.
                        if (!isRoot){
                            
                            tree.addNode(currNode, currParents);
                        }
                        // But if this is a root, add as a root.
                        else{
                        
                            isRoot = false;
                            tree.addRoot(currNode);
                        }
                        
                        currParents.clear();
                        haveSeenTerm = false;
                    }
                    // Create a node if we have seen the id.
                    if (qName.equals("id")){

                        currNode = new XNode(stringAcc);
                    }
                    // Set the description of the node.
                    else if (qName.equals("name")){

                        // To ensure that you set the description only once.
                        if (currNode.getDescription() == null){
                            
                            currNode.setDescription(stringAcc);
                        }
                    }
                    // Add the list of parents.
                    else if(qName.equals("is_a")){

                        currParents.add(stringAcc);
                    }
                    // Am I seeing a root?        
                    else if(qName.equals("is_root") && stringAcc.equals("1")){
                        
                        isRoot = true;
                    }
            }
        }
        
        @Override
        /**
         * Receive notification of character data inside an element. 
         */
        public void characters(char[] ch, int start, int length){
            
            StringBuilder buf = new StringBuilder();
            buf.append(ch, start, length);
            stringAcc = stringAcc + buf.toString();

        }
    }
    
    public static void parseXMLFile(boolean validating, Handler handler) 
            throws Exception{
        
            // Create a builder factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(validating);
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(handler.locationOfFile), handler);
    }
    
}
