/**
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package savant.geneontology;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import savant.settings.BrowserSettings;
import savant.settings.DirectorySettings;


/**
 *
 * @author Nirvana Nursimulu
 */
public class CreateMappings {
    
    public static InputStreamReader is;
    public static BufferedReader buffer;
    
    /**
     * Location of the XML file which is to be parsed for location of files.
     */
    public static final String LOCATION_XML = BrowserSettings.URL + "/nirvana/GOmap.xml";
    
    /**
     * Tries to open an input stream from the url given.
     * @param url the url of interest.
     * @return the data input stream of the url
     */
    public static void openStream(URL url) throws Exception{
        
        is = null;
        buffer = null;
        // open an input stream from the url.
        is = new InputStreamReader(url.openStream()); 
        // Convert the input stream into a buffered input stream.
        buffer = new BufferedReader(is);
    }
    
    
    /**
     * Creates the file of interest, with the mappings from GO ID to Ref Seq ID
     * and genome locations.
     */
    public static String getMappings() throws Exception{
        
        // Open the XML file and find the url's of interest.
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build((new URL(LOCATION_XML)).openStream()); 
        
        List versionChildren = doc.getRootElement().getChildren("branch");
        List children = null;
        
        for (Object versionChild: versionChildren){
            
            Element versionElem = (Element)versionChild;
            // if this is the version we want...
            if (versionElem.getAttributeValue("name").matches
                    ("Current Version.*")){
                
                children = versionElem.getChildren();
            }
        }
        
        // The location of the file of RefSeq IDs to location.
        String urlRefSeqToLoc = null;
        
        // The location of GO IDs to RefSeq IDs.
        String urlGOToRefSeq = null;

        // Look through each child.
        for (Object outerChildObj: children){
            
            Element outerChild = (Element)outerChildObj;

            if (outerChild.getAttribute("name").getValue().equals
                    ("Map of RefSeq IDs to genomic locations.")){
                
                urlRefSeqToLoc = 
                        outerChild.getChild("leaf").getChild("url").getText();
            }
            else if (outerChild.getAttribute("name").getValue().equals
                    ("Map of RefSeq IDs to GO IDs.")){
                
                urlGOToRefSeq = 
                        outerChild.getChild("leaf").getChild("url").getText();
            }
        }        
        
        String version = 
                urlRefSeqToLoc.split("/")[urlRefSeqToLoc.split("/").length - 1];
        version = version.split("-")[version.split("-").length - 1];
        
        // The destination of the file.
        String destination = DirectorySettings.getPluginsDirectory() + 
                File.separator + "Gene Ontology Plugin";
        (new File(destination)).mkdir();
        
        String fileName = 
                File.separator + "MAPPING_GO_Genome_location_" + version;

        // Does the file already exist?  If it does, don't recreate it.
        destination = destination + fileName;
        File file = new File(destination);
        System.out.println(file.getAbsolutePath());
        if (file.exists()){
            return destination;
        }
        
        // Open the input stream for reading the file of ref seq IDs to 
        // genomic locations.
        openStream(new URL(urlRefSeqToLoc));
        
        // Get the map of ref seq IDs to genomic locations.
        HashMap<String, ArrayList<ArrayList<String>>> refSeqToGenome = 
                mapRefSeqToGenome();
        
        
        // Open the input stream for reading the file of GO IDs to RefSeq IDs.
        openStream(new URL(urlGOToRefSeq));

        makeFileGoToGenomeLoc(destination, refSeqToGenome);
        
        return destination;
    }
    
    
    /**
     * Make up file with in the first column the GO ID, the second column a
     * corresponding ref seq ID, and the third and last column a location on a 
     * genome.
     * @param destination the name of the file to contain this information. 
     * @param refSeqToGenome map of refSeq IDs to genome location.
     */
    private static void makeFileGoToGenomeLoc
            (String destination, 
            HashMap<String, ArrayList< ArrayList<String> > > refSeqToGenome) 
            throws IOException{
        
        // File to write to; have the file be deleted upon exit.
        File toWriteTo = new File(destination);
//        toWriteTo.deleteOnExit();
        Writer writer = new BufferedWriter(new FileWriter(toWriteTo));
        
        // Parse through file containing GO to ref seq IDs map.
        
        String line;
        
        // Read line by line until we have reached the end of the file.
        while ((line = buffer.readLine()) != null){
            
            String[] split = line.split("\t");
           
            // forget about those lines where the qualifier is NOT.
            if (split[5].trim().equals("NOT")){
                continue;
            }            
            
            // Get the GO ID.
            String goID = split[3].trim();
            
            // Get the RefSeq ID.
            String refSeq = split[0].trim();
            
            // Get all the locations given this refseq
            ArrayList<ArrayList<String>> locations = refSeqToGenome.get(refSeq);
            
            // Given each location from this refseq: add to the file.
            for (ArrayList<String> location: locations){
                
                String writeLine = goID + "\t" + refSeq;
                for (String part: location){
                    
                    writeLine = writeLine + "\t" + part;
                }
                writeLine = writeLine + "\n";
                writer.write(writeLine);
            }
        }
        
        buffer.close();
        is.close();
        writer.flush();
        writer.close();
    }
    
        /**
     * Return a map from the ref seq ID to genome information.
     * @return the map from ref seq ID to genome information. Genome information
     * is contained in a list with at index 0: the chromosome, at index 1: the 
     * start position, at index 2: the end position.  Note that the value may 
     * contain many such arrays. (Note BED formatting.)
     */
    private static HashMap<String, ArrayList<ArrayList<String>>> 
            mapRefSeqToGenome() throws Exception{
        
        HashMap<String, ArrayList<ArrayList<String>>> refSeqToGenome = 
                new HashMap<String, ArrayList<ArrayList<String>>>();
        
        String line;
        String[] split;
        
            
        // While there is still something to be read.
        while ((line = buffer.readLine()) != null){
                
                // Split the line based on tab delimiters.
                split = line.split("\t");
                
                // add genomic information to array list.
                ArrayList<String> arrayInfo = new ArrayList<String>();
                arrayInfo.add(split[0].trim());
                arrayInfo.add(split[1].trim());
                arrayInfo.add(split[2].trim());
                
                // The refseq ID.
                String key = split[3].trim();
                ArrayList<ArrayList<String>> value = refSeqToGenome.get(key); 
                
                if (value == null){
                    
                    value = new ArrayList<ArrayList<String>>();
                    refSeqToGenome.put(key, value);
                }
                    
                value.add(arrayInfo);
        }

        
        buffer.close();
        is.close();
        return refSeqToGenome;
    }
 
}
