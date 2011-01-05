/*
 * DataFormatForm.java
 * Created on Feb 11, 2010, 5:56:38 PM
 *
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

package savant.view.dialog;

import savant.file.FileType;
import savant.view.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import savant.format.DataFormatter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import savant.util.MiscUtils;

/**
 * @author mfiume
 */
public class DataFormatForm extends JDialog {

    private static final Log LOG = LogFactory.getLog(DataFormatForm.class);

    private static List<Boolean> defaultBases;
    private static List<Boolean> canChooseBaseStatus;
    private static List<String> formats;
    private static HashMap<String,String> formatDescriptionMap;
    private static HashMap<String,FileType> formatTypeMap;

    private static JTextArea formatDescriptionTextArea;
    private boolean success = false;

    //private FormatTask formatTask;

    // message set by format task during execution
    private String message;


    /** Creates new form DataFormatForm */
    public DataFormatForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();
        initFormats();
        initListActionHandler();

        // in the future
        checkbox_tempOut.setVisible(false);

        //checkbox_chooseBase.setVisible(false);

        formatDescriptionTextArea = this.textarea_formatDescription;
        validateReadyToFormat();
    }

    public void setInFile(String infile) {

        if (infile != null) {
            this.textfield_inPath.setText(infile);
            this.setOutputPath(infile);
            validateReadyToFormat();
        }

    }

    public void clear() {
        this.textfield_inPath.setEnabled(true);
        this.textfield_outPath.setEnabled(true);
        this.button_openInPath.setEnabled(true);
        this.button_openOutFile.setEnabled(true);
        this.list_formats.setEnabled(true);

        this.textfield_inPath.setText("");
        this.textfield_outPath.setText("");
        if (!this.list_formats.isSelectionEmpty())
            this.list_formats.clearSelection();
        validateReadyToFormat();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textfield_inPath = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        textarea_formatDescription = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_formats = new javax.swing.JList();
        button_openInPath = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        button_openOutFile = new javax.swing.JButton();
        button_format = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        textfield_outPath = new javax.swing.JTextField();
        checkbox_tempOut = new javax.swing.JCheckBox();
        checkbox_chooseBase = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Format");

        textfield_inPath.setEditable(false);
        textfield_inPath.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        textarea_formatDescription.setColumns(20);
        textarea_formatDescription.setEditable(false);
        textarea_formatDescription.setFont(new java.awt.Font("Verdana", 0, 10));
        textarea_formatDescription.setLineWrap(true);
        textarea_formatDescription.setRows(3);
        textarea_formatDescription.setWrapStyleWord(true);
        jScrollPane2.setViewportView(textarea_formatDescription);

        jLabel2.setText("Format");

        list_formats.setModel(new DefaultListModel());
        list_formats.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_formats.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_formatsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(list_formats);

        button_openInPath.setText("...");
        button_openInPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_openInPathActionPerformed(evt);
            }
        });

        jLabel1.setText("Input File");

        jLabel3.setText("Output File");

        button_openOutFile.setText("...");
        button_openOutFile.setEnabled(false);
        button_openOutFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_openOutFileActionPerformed(evt);
            }
        });

        button_format.setText("Format");
        button_format.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_formatActionPerformed(evt);
            }
        });

        jLabel4.setText("Format Description");

        textfield_outPath.setEditable(false);
        textfield_outPath.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        textfield_outPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textfield_outPathActionPerformed(evt);
            }
        });

        checkbox_tempOut.setText("Store output temporarily");
        checkbox_tempOut.setToolTipText("Store the formatted file just for this session");
        checkbox_tempOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkbox_tempOutActionPerformed(evt);
            }
        });

        checkbox_chooseBase.setSelected(true);
        checkbox_chooseBase.setText("Input file is 1-based");
        checkbox_chooseBase.setToolTipText("Checked for 1-based; unchecked for 0-based.");
        checkbox_chooseBase.setEnabled(false);
        checkbox_chooseBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkbox_chooseBaseActionPerformed(evt);
            }
        });

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(textfield_inPath, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_openInPath))
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                    .addComponent(button_format, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(textfield_outPath, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_openOutFile))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkbox_chooseBase)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkbox_tempOut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textfield_inPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_openInPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(checkbox_chooseBase)
                        .addComponent(checkbox_tempOut)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textfield_outPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_openOutFile))
                .addGap(26, 26, 26)
                .addComponent(button_format)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_openInPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_openInPathActionPerformed
        JFrame jf = new JFrame();
        String selectedFileName;
        if (Savant.mac) {
            FileDialog fd = new FileDialog(jf, "Input File", FileDialog.LOAD);
            fd.setVisible(true);
            jf.setAlwaysOnTop(true);
            // get the path (null if none selected)
            selectedFileName = fd.getFile();
            if (selectedFileName != null) {
                selectedFileName = fd.getDirectory() + selectedFileName;
            }
        }
        else {
            JFileChooser fd = new JFileChooser();
            fd.setDialogTitle("Input File");
            fd.setDialogType(JFileChooser.OPEN_DIALOG);
            int result = fd.showOpenDialog(this);
            if (result == JFileChooser.CANCEL_OPTION || result == JFileChooser.ERROR_OPTION ) return;
            selectedFileName = fd.getSelectedFile().getPath();
        }

        // set the genome
        if (selectedFileName != null) {
            this.textfield_inPath.setText(selectedFileName);
            setOutputPath(selectedFileName);
        }

        validateReadyToFormat();
    }//GEN-LAST:event_button_openInPathActionPerformed

    private void checkbox_tempOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkbox_tempOutActionPerformed
        
        setOutputPath(this.textfield_inPath.getText());
        
    }//GEN-LAST:event_checkbox_tempOutActionPerformed

    private void button_openOutFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_openOutFileActionPerformed
        JFrame jf = new JFrame();
        String selectedFileName;
        if (Savant.mac) {
            FileDialog fd = new FileDialog(jf, "Output File", FileDialog.SAVE);
            fd.setVisible(true);
            jf.setAlwaysOnTop(true);
            // get the path (null if none selected)
            selectedFileName = fd.getFile();
            if (selectedFileName != null) {
                selectedFileName = fd.getDirectory() + selectedFileName;
            }
        }
        else {
            JFileChooser fd = new JFileChooser();
            fd.setDialogTitle("Output File");
            fd.setDialogType(JFileChooser.SAVE_DIALOG);
            int result = fd.showOpenDialog(jf);
            if (result == JFileChooser.CANCEL_OPTION || result == JFileChooser.ERROR_OPTION ) return;
            selectedFileName = fd.getSelectedFile().getPath();
        }

        // set the genome
        if (selectedFileName != null) {
            this.textfield_outPath.setText(selectedFileName);
        }

        validateReadyToFormat();
    }//GEN-LAST:event_button_openOutFileActionPerformed


    private void button_formatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_formatActionPerformed

        File infile = new File(textfield_inPath.getText());
        File outfile = new File(textfield_outPath.getText());
        FileType ft = formatTypeMap.get(formats.get(list_formats.getSelectedIndex()));
        boolean isInputOneBased = checkbox_chooseBase.isSelected();
        
        DataFormatter df = new DataFormatter(infile, outfile, ft, isInputOneBased);
        FormatFrame fpd = new FormatFrame(df);
        fpd.setLocationRelativeTo(this);
        fpd.setVisible(true);
        this.dispose();

        // make sure the user can't interact with anything while formatting
        //this.textfield_inPath.setEnabled(false);
        //this.textfield_outPath.setEnabled(false);
        //this.button_openInPath.setEnabled(false);
        //this.button_openOutFile.setEnabled(false);
        //this.list_formats.setEnabled(false);

        //formatTask.execute();

    }//GEN-LAST:event_button_formatActionPerformed

    private void checkbox_chooseBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkbox_chooseBaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkbox_chooseBaseActionPerformed

    private void textfield_outPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textfield_outPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textfield_outPathActionPerformed

    private void list_formatsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_formatsValueChanged
        this.setOutputPath(this.textfield_inPath.getText());
        validateReadyToFormat();

//        String ff = (String) ((JList) evt.getSource()).getSelectedValue();
//        System.out.println(ff);
//        if (ff.equals("BAM Coverage")) {
//            this.button_openOutFile.setEnabled(false);
//        } else {
//            this.button_openOutFile.setEnabled(true);
//        }
    }//GEN-LAST:event_list_formatsValueChanged

    /*
    public void cancelFormatTask() {
        if (formatTask != null) formatTask.cancel(true);
        setVisible(false);
        DataFormatForm.this.getParent().requestFocus();
    }
     * 
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_format;
    private javax.swing.JButton button_openInPath;
    private javax.swing.JButton button_openOutFile;
    private javax.swing.JCheckBox checkbox_chooseBase;
    private javax.swing.JCheckBox checkbox_tempOut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList list_formats;
    private javax.swing.JTextArea textarea_formatDescription;
    private javax.swing.JTextField textfield_inPath;
    private javax.swing.JTextField textfield_outPath;
    // End of variables declaration//GEN-END:variables

    private void initListActionHandler() {
        this.list_formats.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    JList jl = (JList) e.getSource();
                    if (!jl.isSelectionEmpty()) {
                        int index = jl.getSelectedIndex();
                        LOG.info(index + " picked");
                        formatDescriptionTextArea.setText(formatDescriptionMap.get(formats.get(index)));

                        checkbox_chooseBase.setSelected(defaultBases.get(index));
                        checkbox_chooseBase.setEnabled(canChooseBaseStatus.get(index));
                    }
                    //checkbox_chooseBase.setVisible(canChooseBaseStatus.get(index));
                    //Savant.log("List value changed to " + e.getFirstIndex() + " " + e.getLastIndex() + " " + minIndex + " " + maxIndex);
                }
                validateReadyToFormat();
            }
        });
    }

    private void initFormats() {
        defaultBases = new ArrayList<Boolean>();
        canChooseBaseStatus = new ArrayList<Boolean>();
        formats = new ArrayList<String>();
        formatDescriptionMap = new HashMap<String,String>();

        formatTypeMap = new HashMap<String,FileType>();
        addFormat("FASTA", true, false, FileType.SEQUENCE_FASTA ,  "FASTA format is a text-based format for representing either nucleotide sequences or peptide sequences, in which base pairs or amino acids are represented using single-letter codes.");
        addFormat("BED", false, false, FileType.INTERVAL_BED , "BED format is an alternative to GFF format for describing co-ordinates of localized features on genomes.");
        addFormat("GFF", true, false, FileType.INTERVAL_GFF , "GFF is a format for locating & describing genes and other localized features associated with DNA, RNA and Protein sequences.");
        addFormat("BAM Coverage", true, false, FileType.INTERVAL_BAM , "SAM format (binary, for BAM) is a generic format for storing large nucleotide sequence alignments.");
        addFormat("WIG/BedGraph", true, false, FileType.CONTINUOUS_WIG , "WIG format allows display of continuous-valued data in track format. This display type is useful for GC percent, probability scores, and transcriptome data.");
        addFormat("Generic Interval", true, true, FileType.INTERVAL_GENERIC , "Generic intervals can be used to display any number of from to pairs, each with an associated description.");
        addFormat("Generic Point", true, true, FileType.POINT_GENERIC ,"Generic points can be used to display any number of positional elements, each with an associated description.");
        addFormat("Generic Continuous", true, true, FileType.CONTINUOUS_GENERIC ,"Generic intervals can be used to display continuous values.");

        DefaultListModel model = (DefaultListModel) this.list_formats.getModel();
        for (int i = 0; i < formats.size(); i++) {
            model.add(i, formats.get(i));
        }
    }

    public FileType getFileType() {
        return formatTypeMap.get(formats.get(list_formats.getSelectedIndex()));
    }

    private void addFormat(String fname, boolean defaultIsOneBase, boolean canChooseBase, FileType ft, String fdescription) {
        formats.add(fname);
        formatTypeMap.put(fname, ft);
        formatDescriptionMap.put(fname, fdescription);
        defaultBases.add(defaultIsOneBase);
        canChooseBaseStatus.add(canChooseBase);
    }

    private void setOutputPath(String selectedFileName) {
        FileType ft = null;
        try { ft = formatTypeMap.get(formats.get(list_formats.getSelectedIndex())); }
        catch (Exception e) {}

        if (this.checkbox_tempOut.isSelected()) {
            this.button_openOutFile.setEnabled(false);
            if ((ft == null && this.textfield_inPath.getText().endsWith(".bam")) || ft == FileType.INTERVAL_BAM) {
                 this.textfield_outPath.setEnabled(false);
                 this.button_openOutFile.setEnabled(false);
                 this.textfield_outPath.setText("Directory of BAM file");
             } else {
                this.textfield_outPath.setEnabled(true);
                 this.button_openOutFile.setEnabled(true);
                 this.textfield_outPath.setText(selectedFileName + ".tmp.savant");
             }
        } else {
            this.button_openOutFile.setEnabled(true);
            if (selectedFileName.equals("")) {
                if ((ft == null && this.textfield_inPath.getText().endsWith(".bam")) || ft == FileType.INTERVAL_BAM) {
                    this.textfield_outPath.setEnabled(false);
                    this.button_openOutFile.setEnabled(false);
                    this.textfield_outPath.setText("No input file selected");
                 } else {
                    this.textfield_outPath.setEnabled(true);
                    this.button_openOutFile.setEnabled(true);
                    this.textfield_outPath.setText("No input file selected");
                 }
            } else {
                 if ((ft == null && this.textfield_inPath.getText().endsWith(".bam")) || ft == FileType.INTERVAL_BAM) {
                    this.textfield_outPath.setEnabled(false);
                    this.button_openOutFile.setEnabled(false);
                    this.textfield_outPath.setText("Directory of BAM file");
                 } else {
                     this.textfield_outPath.setEnabled(true);
                    this.button_openOutFile.setEnabled(true);
                    this.textfield_outPath.setText(selectedFileName + ".savant");
                 }
            }
        }
        validateReadyToFormat();
    }

    private void validateReadyToFormat() {

        if (
                this.textfield_inPath.getText().equals("")
                || this.textfield_outPath.getText().equals("")
                || this.list_formats.getSelectedIndex() < 0 )
        {
            this.button_format.setEnabled(false);
        } else {
            this.button_format.setEnabled(true);
            this.getRootPane().setDefaultButton(this.button_format);
            //this.button_format.requestFocus();
        }

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        boolean oldValue = this.success;
        this.success = success;
        // force property change support to fire the event, even if the property hasn't actually changed.
        // this is the only way Savant has of knowing what has happened during formatting.
        firePropertyChange("success", !this.success, this.success);
    }

    /*
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                if (formatTask.isCancelled() || formatTask.isDone()) {
                    formatProgressBar.setVisible(false);
                    setVisible(false);
                    DataFormatForm.this.getParent().requestFocus();
                }
            }
        }
        else if ("progress".equals(evt.getPropertyName())) {
            formatProgressBar.setValue((Integer)evt.getNewValue());
        }
    }
     * 
     */

    /*
    public FormatTask getFormatTask() {
        return formatTask;
    }
     * 
     */
    
    public String getMessage() {
        return message;
    }

    public void addToMessage(String msg) {
        if (message == null) { message = MiscUtils.now() + "\t" + msg; }
        else { this.message = message + "\n" + MiscUtils.now() + "\t" + msg; }
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /*
    class FormatTask extends SwingWorker<Void, Void> implements /*PropertyChangeListener FormatProgressListener {

        private DataFormatter df;

        public FormatTask(DataFormatter df) {
            this.df = df;
//            this.df.addPropertyChangeListener("progress", this);
            df.addProgressListener(this);
        }

        @Override
        public Void doInBackground() throws Exception {
            //setMessage(null);
            //setProgress(0);
            //success = false; // don't use setSuccess because it fires an event
                FormatProgressDialog d = new FormatProgressDialog(
                        this,
                        true,
                        df);

                addToMessage("Beginning format");
                addToMessage("Input file path: " + this.df.getInputFilePath());
                addToMessage("Input file type: " + this.df.getInputFileType());
                addToMessage("Output file path: " + this.df.getOutputFilePath());
                d.setVisible(true);

                if (d.didFormatComplete()) {

                }


                //df.format();
                //setProgress(100);
                //setSuccess(true);
                
            /*
                } catch (InterruptedException e) {
                log.info("Format cancelled by user");
            } catch (Exception e){
                log.error("Error formatting file ", e);
                addToMessage("Error, printing stack trace\n" +
                        MiscUtils.getStackTrace(e));
                setSuccess(false);
            } catch (Throwable t) {
                log.error("Error formatting file ", t);
                addToMessage("Error, printing stack trace\n" +
                        MiscUtils.getStackTrace(t));
                setSuccess(false);
            }
            finally {
                return null;
            }
             * 
             *

                return null;
        }

        @Override
        public void done() {
            button_format.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
//            this.df.removePropertyChangeListener("progress", this);
        }

        @Override
        public void progressUpdate(int progress, String status) {
        }

//        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
//            if ("progress".equals(propertyChangeEvent.getPropertyName())) {
//                setProgress((Integer)propertyChangeEvent.getNewValue());
//            }
//        }


        /*
        public void progressUpdate(int value) {
            setProgress(value);
        }
         * 
         */

        /*
        int pr = 0;
        String status = "";

        @Override
        public void progressUpdate(int progress, String st) {
            if (progress != -1) {
                pr = progress;
                //this.setProgress(progress);
                formatProgressBar.setValue(progress);
                formatProgressBar.setString(progress + "%");
            }
            /*
            if (status != null) {
                status = st;
                formatProgressBar.setString(st);
            }
             * 
             *
            System.out.println("Progress update received: (p = " + pr + ") s = (" + status + ")");
        }
         * 
         *

    }
         * 
         */
}
