/*
 * Copyright (c) 2007-2010 by The Broad Institute, Inc. and the Massachusetts Institute of Technology.
 * All Rights Reserved.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL), Version 2.1 which
 * is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 *
 * THE SOFTWARE IS PROVIDED "AS IS." THE BROAD AND MIT MAKE NO REPRESENTATIONS OR WARRANTIES OF
 * ANY KIND CONCERNING THE SOFTWARE, EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT
 * OR OTHER DEFECTS, WHETHER OR NOT DISCOVERABLE.  IN NO EVENT SHALL THE BROAD OR MIT, OR THEIR
 * RESPECTIVE TRUSTEES, DIRECTORS, OFFICERS, EMPLOYEES, AND AFFILIATES BE LIABLE FOR ANY DAMAGES OF
 * ANY KIND, INCLUDING, WITHOUT LIMITATION, INCIDENTAL OR CONSEQUENTIAL DAMAGES, ECONOMIC
 * DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER THE BROAD OR MIT SHALL
 * BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT SHALL KNOW OF THE POSSIBILITY OF THE
 * FOREGOING.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.broad.igv.tools;

import net.sf.samtools.util.CloseableIterator;
import org.broad.igv.feature.Chromosome;
import org.broad.igv.feature.Genome;
import org.broad.igv.feature.Strand;
import org.broad.igv.sam.Alignment;
import org.broad.igv.sam.AlignmentBlock;
import org.broad.igv.tools.parsers.DataConsumer;
import org.broad.igv.track.TrackType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.broad.igv.sam.reader.BAMQueryReader;

/**
 *   TODO -- normalize option
 *
 -n           Normalize the count by the total number of reads. This option
 multiplies each count by (1,000,000 / total # of reads). It
 is useful when comparing multiple chip-seq experiments when
 the absolute coverage depth is not important.
 */

/**
 *
 */
public class CoverageCounter {

    private String alignmentFile;
    private DataConsumer consumer;
    private float[] buffer;
    private int windowSize = 1;
    // TODO -- make mapping qulaity a parameter
    private int minMappingQuality = 0;
    private int strandOption = -1;
    private int extFactor;
    private int totalCount = 0;
    private File wigFile = null;
    private WigWriter wigWriter = null;
    private Genome genome;

    public CoverageCounter(String alignmentFile,
                           DataConsumer consumer,
                           int windowSize,
                           int extFactor,
                           File wigFile,
                           Genome genome,
                           int strandOption) {
        this.alignmentFile = alignmentFile;
        this.consumer = consumer;
        this.windowSize = windowSize;
        this.extFactor = extFactor;
        this.wigFile = wigFile;
        this.genome = genome;
        this.strandOption = strandOption;
        buffer = strandOption < 0 ? new float[1] : new float[2];
    }

    private boolean passFilter(Alignment alignment) {

        if (strandOption > 0 && alignment.getFragmentStrand(strandOption) == Strand.NONE) {
            return false;
        }
        return alignment.isMapped() && !alignment.isDuplicate() && alignment.getMappingQuality() >= minMappingQuality;
    }

    public void parse(StatusMonitor monitor) throws IOException, InterruptedException {

        int tolerance = (int) (windowSize * (Math.floor(extFactor / windowSize) + 2));
        consumer.setSortTolerance(tolerance);

        BAMQueryReader reader = null;
        CloseableIterator<Alignment> iter = null;

        String lastChr = "";
        ReadCounter counter = null;


        try {

            if (wigFile != null) {
                wigWriter = new WigWriter(wigFile, windowSize);
            }

            reader = new BAMQueryReader(new File(alignmentFile));
            double denominator = reader.getSequenceNames().size() * 0.01;
            double chrCount = 0.0;
            iter = reader.iterator();

            while (iter != null && iter.hasNext()) {
                Alignment alignment = iter.next();
                if (passFilter(alignment)) {

                    totalCount++;

                    String alignmentChr = alignment.getChr();

                    // Close all counters with position < alignment.getStart()
                    if (alignmentChr.equals(lastChr)) {
                        if (counter != null) {
                            counter.closeBucketsBefore(alignment.getAlignmentStart() - tolerance);
                        }
                    } else {
                        if (counter != null) {
                            counter.closeBucketsBefore(Integer.MAX_VALUE);
                        }
                        counter = new ReadCounter(alignmentChr);
                        lastChr = alignmentChr;
                        if (monitor != null) {
                            chrCount++;
                            monitor.setPercentComplete(chrCount / denominator);
                        }
                    }

                    AlignmentBlock[] blocks = alignment.getAlignmentBlocks();
                    if (blocks != null) {
                        for (AlignmentBlock block : blocks) {

                            int adjustedStart = block.getStart();
                            int adjustedEnd = block.getEnd();
                            if (alignment.isNegativeStrand()) {
                                adjustedStart = Math.max(0, adjustedStart - extFactor);
                            } else {
                                adjustedEnd += extFactor;
                            }

                            for (int pos = adjustedStart; pos < adjustedEnd; pos++) {

                                counter.incrementCount(pos);
                            }
                        }
                    } else {
                        int adjustedStart = alignment.getAlignmentStart();
                        int adjustedEnd = alignment.getAlignmentEnd();
                        if (alignment.isNegativeStrand()) {
                            adjustedStart = Math.max(0, adjustedStart - extFactor);
                        } else {
                            adjustedEnd += extFactor;
                        }

                        for (int pos = adjustedStart; pos < adjustedEnd; pos++) {
                            counter.incrementCount(pos);
                        }
                    }
                }
                if (monitor != null && monitor.isInterrupted()) {
                    throw new InterruptedException();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (counter != null) {
                counter.closeBucketsBefore(Integer.MAX_VALUE);
            }

            consumer.setAttribute("totalCount", String.valueOf(totalCount));
            consumer.parsingComplete();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            if (iter != null) {
                iter.close();
            }

            if (wigWriter != null) {
                wigWriter.close();
            }
        }
    }

    class ReadCounter {

        String chr;
        TreeMap<Integer, Counter> counts = new TreeMap();

        ReadCounter(String chr) {
            this.chr = chr;
        }

        void incrementCount(int position) {
            Integer bucket = position / windowSize;
            if (!counts.containsKey(bucket)) {
                counts.put(bucket, new Counter());
            }
            counts.get(bucket).increment();
        }

        void incrementNegCount(int position) {
            Integer bucket = position / windowSize;
            if (!counts.containsKey(bucket)) {
                counts.put(bucket, new Counter());
            }
            counts.get(bucket).incrementNeg();
        }

        void closeBucketsBefore(int position) throws IOException, InterruptedException {
            List<Integer> bucketsToClose = new ArrayList();

            Integer bucket = position / windowSize;
            for (Map.Entry<Integer, Counter> entry : counts.entrySet()) {
                if (entry.getKey() < bucket) {

                    // Divide total count by window size.  This is the average count per
                    // base over the window,  so 30x coverage remains 30x irrespective of window size.
                    int bucketStartPosition = entry.getKey() * windowSize;
                    int bucketEndPosition = bucketStartPosition + windowSize;
                    if (genome != null) {
                        Chromosome chromosome = genome.getChromosome(chr);
                        if (chromosome != null) {
                            bucketEndPosition = Math.min(bucketEndPosition, chromosome.getLength());
                        }
                    }
                    int bucketSize = bucketEndPosition - bucketStartPosition;

                    buffer[0] = ((float) entry.getValue().getCount()) / bucketSize;

                    if (strandOption > 0) {
                        buffer[1] = ((float) entry.getValue().getCount()) / bucketSize;
                    }

                    consumer.addData(chr, bucketStartPosition, bucketEndPosition, buffer, null);
                    if (wigWriter != null) {
                        wigWriter.addData(chr, bucketStartPosition, bucketEndPosition, buffer, null);
                    }
                    bucketsToClose.add(entry.getKey());
                }
            }

            for (Integer key : bucketsToClose) {
                counts.remove(key);
            }


        }
    }

    static class Counter {

        int count = 0;
        int negCount = 0;

        void increment() {
            count++;
        }

        void incrementNeg() {
            negCount++;
        }

        int getCount() {
            return count;
        }

        int getNegCount() {
            return negCount;
        }
    }

    /**
     * Creates a vary step wig file
     */
    class WigWriter implements DataConsumer {
        String lastChr = null;
        int lastPosition = 0;
        int step;
        int span;
        PrintWriter pw;

        WigWriter(File file, int step) throws IOException {
            this.step = step;
            this.span = step;
            pw = new PrintWriter(new FileWriter(file));
        }

        public void addData(String chr, int start, int end, float[] data, String name) {

            if (genome.getChromosome(chr) == null) {
                return;
            }

            if (data[0] == 0 || end <= start) {
                return;
            }

            int dataSpan = end - start;

            if (chr == null || !chr.equals(lastChr) || dataSpan != span) {
                span = dataSpan;
                outputStepLine(chr, start + 1);
            }
            pw.println((start + 1) + "\t" + data[0]);
            lastPosition = start;
            lastChr = chr;

        }

        private void close() {
            pw.close();

        }

        private void outputStepLine(String chr, int start) {
            pw.println("variableStep chrom=" + chr + " span=" + span);
        }

        public void setType(String type) {
            //To change body of implemented methods use File | Settings | File Templates.
        }


        public void parsingComplete() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setTrackParameters(TrackType trackType, String trackLine, String[] trackNames) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setSortTolerance(int tolerance) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setAttribute(String key, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}
