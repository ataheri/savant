/*
 *    Copyright 2010 University of Toronto
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
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import savant.format.util.data.FieldType;
import savant.util.DataFormatUtils;

public class PointGenericFormatter extends GenericFormatter {

    public static final int RECORDS_PER_INTERRUPT_CHECK = 100;

     public PointGenericFormatter(String inFile, DataOutputStream outFile) {
        this.inFile = inFile;
        this.out = outFile;
    }

    public void format() throws IOException, InterruptedException{
        // Initialize the total size of the input file, for purposes of tracking progress
        this.totalBytes = new File(inFile).length();

        inputFile = this.openInputFile();

        fields = new ArrayList<FieldType>();
        fields.add(FieldType.INTEGER);
        fields.add(FieldType.STRING);

        modifiers = new ArrayList<Object>();
        modifiers.add(null);
        int descriptionLength = pointGenericGetDescriptionLength();
        modifiers.add(descriptionLength);

        DataFormatUtils.writeFieldsHeader(out, fields);

        try {
            String strLine;
            List<Object> line;
            boolean done = false;
            while (!done) {
                for (int i=0; i<RECORDS_PER_INTERRUPT_CHECK; i++) {
                    if ((strLine = inputFile.readLine()) == null) {
                        done = true;
                        break;
                    }
                    // update bytes read from input
                    this.byteCount += strLine.getBytes().length;
                    // parse input and write output
                    if ((line = DataFormatUtils.parseTxtLine(strLine, fields)) != null) {
                        line.set(0, ((Integer) line.get(0))+ this.baseOffset);
                        DataFormatUtils.writeBinaryRecord(out, line, fields, modifiers);
                    }
                }
                // check to see if format has been cancelled
                if (Thread.interrupted()) throw new InterruptedException();
                // update progress property for UI
                updateProgress();
            }
        }
        finally {
            inputFile.close();
            out.close();
        }
        
    }

    private int pointGenericGetDescriptionLength() throws IOException {

        BufferedReader inputFileL = this.openInputFile();
        String txtLine = "";
        StringTokenizer tok;
        int tokenNum;

        int maxDescriptionLength = Integer.MIN_VALUE;

        while ((txtLine = inputFileL.readLine()) != null) {

            tok = new StringTokenizer(txtLine);
            tokenNum = 0;
            String token = "";
            while (tok.hasMoreElements()) {
                token = tok.nextToken();
                if (tokenNum == 1) {
                    int descriptionLength = token.length();
                    maxDescriptionLength = Math.max(descriptionLength, maxDescriptionLength);
                    break;
                }
                tokenNum++;
            }
        }

        return maxDescriptionLength;
    }


}