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
package savant.view.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import savant.api.util.DialogUtils;
import savant.controller.DataSourcePluginController;
import savant.data.sources.DataSource;
import savant.data.types.Genome;
import savant.view.swing.Savant;
import savant.view.swing.TrackFactory;

/**
 *
 * @author mfiume
 */
public class LoadGenomeDialog extends JDialog {

    /** Creates new form LoadGenomeDialog */
    public LoadGenomeDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        if (DataSourcePluginController.getInstance().hasOnlySavantRepoDataSource()) {
            fromOtherButton.setText("Repository");
        }

        initDropDowns();
        updateEnabledControls();
        getRootPane().setDefaultButton(okButton);
        setLocationRelativeTo(parent);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        withoutSequenceButtonGroup = new javax.swing.ButtonGroup();
        javax.swing.JPanel withoutSequencePanel = new javax.swing.JPanel();
        javax.swing.JLabel nameLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        nameField = new javax.swing.JTextField();
        javax.swing.JLabel lengthLabel = new javax.swing.JLabel();
        lengthField = new javax.swing.JTextField();
        javax.swing.JLabel buildLabel = new javax.swing.JLabel();
        genesCombo = new javax.swing.JComboBox();
        genomesCombo = new javax.swing.JComboBox();
        javax.swing.JLabel speciesLabel = new javax.swing.JLabel();
        commonGenomeRadio = new javax.swing.JRadioButton();
        userSpecifiedRadio = new javax.swing.JRadioButton();
        javax.swing.JButton cancelButton = new javax.swing.JButton();
        javax.swing.JPanel withSequencePanel = new javax.swing.JPanel();
        fromFileButton = new javax.swing.JButton();
        fromURLButton = new javax.swing.JButton();
        fromOtherButton = new javax.swing.JButton();
        javax.swing.JLabel fromLabel = new javax.swing.JLabel();

        withoutSequenceButtonGroup.add(commonGenomeRadio);
        withoutSequenceButtonGroup.add(userSpecifiedRadio);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Genome");

        withoutSequencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Load Genome without Sequence"));
        withoutSequencePanel.setEnabled(false);

        nameLabel.setText("Name:");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        nameField.setToolTipText("Name of reference (must correspond to name in records)");

        lengthLabel.setText("Length:");

        lengthField.setToolTipText("Length in basepairs of reference");
        lengthField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lengthFieldFocusLost(evt);
            }
        });

        buildLabel.setText("Genes:");

        genesCombo.setToolTipText("Build for species to load");
        genesCombo.setEnabled(false);

        genomesCombo.setToolTipText("Species of a published genome to load");
        genomesCombo.setEnabled(false);

        speciesLabel.setText("Genome:");

        commonGenomeRadio.setText("Published Genome");
        commonGenomeRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonGenomeRadioActionPerformed(evt);
            }
        });

        userSpecifiedRadio.setText("User-specified");
        userSpecifiedRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSpecifiedRadioActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout withoutSequencePanelLayout = new javax.swing.GroupLayout(withoutSequencePanel);
        withoutSequencePanel.setLayout(withoutSequencePanelLayout);
        withoutSequencePanelLayout.setHorizontalGroup(
            withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(withoutSequencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commonGenomeRadio)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, withoutSequencePanelLayout.createSequentialGroup()
                        .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(withoutSequencePanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lengthLabel)
                                    .addComponent(nameLabel)))
                            .addComponent(userSpecifiedRadio)
                            .addGroup(withoutSequencePanelLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buildLabel)
                                    .addComponent(speciesLabel))))
                        .addGap(23, 23, 23)
                        .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addComponent(lengthField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addComponent(genomesCombo, 0, 291, Short.MAX_VALUE)
                            .addComponent(genesCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, 291, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, withoutSequencePanelLayout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(okButton)))
                .addContainerGap())
        );
        withoutSequencePanelLayout.setVerticalGroup(
            withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(withoutSequencePanelLayout.createSequentialGroup()
                .addComponent(commonGenomeRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(speciesLabel)
                    .addComponent(genomesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buildLabel))
                .addGap(9, 9, 9)
                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(withoutSequencePanelLayout.createSequentialGroup()
                        .addComponent(userSpecifiedRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameLabel))
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthLabel)
                    .addComponent(lengthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(withoutSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        withSequencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Load Genome with Sequence"));

        fromFileButton.setText("File");
        fromFileButton.setPreferredSize(new java.awt.Dimension(135, 25));
        fromFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromFileButtonActionPerformed(evt);
            }
        });

        fromURLButton.setText("URL");
        fromURLButton.setPreferredSize(new java.awt.Dimension(135, 25));
        fromURLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromURLButtonActionPerformed(evt);
            }
        });

        fromOtherButton.setText("Other Datasource");
        fromOtherButton.setPreferredSize(new java.awt.Dimension(135, 25));
        fromOtherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromOtherButtonActionPerformed(evt);
            }
        });

        fromLabel.setText("Load from:");

        javax.swing.GroupLayout withSequencePanelLayout = new javax.swing.GroupLayout(withSequencePanel);
        withSequencePanel.setLayout(withSequencePanelLayout);
        withSequencePanelLayout.setHorizontalGroup(
            withSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(withSequencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(withSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(withSequencePanelLayout.createSequentialGroup()
                        .addComponent(fromFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromURLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromOtherButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fromLabel))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        withSequencePanelLayout.setVerticalGroup(
            withSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, withSequencePanelLayout.createSequentialGroup()
                .addComponent(fromLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(withSequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromURLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromOtherButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(withSequencePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(withoutSequencePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(withSequencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(withoutSequencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        
        if (!userSpecifiedRadio.isSelected() && !this.commonGenomeRadio.isSelected()) {
            return;
        }

        if (userSpecifiedRadio.isSelected() && !validateUserSpecifiedLength()) {
            return;
        }

        if (userSpecifiedRadio.isSelected()) {
            isPopularGenome = false;
            loadedGenome = new Genome(nameField.getText(), Integer.parseInt(lengthField.getText()));

        } else {
            isPopularGenome = true;
            loadedGenome = (Genome)genomesCombo.getSelectedItem();
        }

        Savant.getInstance().setGenome(loadedGenome.getName(), loadedGenome, null);

        setVisible(false);
}//GEN-LAST:event_okButtonActionPerformed

    private void lengthFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lengthFieldFocusLost
        //validateUserSpecifiedLength();
}//GEN-LAST:event_lengthFieldFocusLost

    private void commonGenomeRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonGenomeRadioActionPerformed
        updateEnabledControls();
}//GEN-LAST:event_commonGenomeRadioActionPerformed

    private void userSpecifiedRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSpecifiedRadioActionPerformed
        updateEnabledControls();
}//GEN-LAST:event_userSpecifiedRadioActionPerformed

    private void fromFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromFileButtonActionPerformed
        setVisible(false);
        Savant.getInstance().showOpenTracksDialog(true);
    }//GEN-LAST:event_fromFileButtonActionPerformed

    private void fromOtherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromOtherButtonActionPerformed
        setVisible(false);
        DataSource s;
        try {
            if (DataSourcePluginController.getInstance().hasOnlySavantRepoDataSource()) {
                s = DataSourcePluginController.getInstance().getDataSourcePlugins().get(0).getDataSource();
            } else {
                s = DataSourcePluginDialog.getDataSource(this);
            }
            if (s != null) {
                Savant.getInstance().setGenomeFromTrack(TrackFactory.createTrack(s), null);
                return;
            }
        } catch (Exception x) {
            DialogUtils.displayException("Error", "Unable to load genome from the plugin datasource.", x);
        }
        Savant.getInstance().showOpenGenomeDialog();
    }//GEN-LAST:event_fromOtherButtonActionPerformed

    private void fromURLButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromURLButtonActionPerformed
        setVisible(false);
        Savant.getInstance().showOpenURLDialog(true);
    }//GEN-LAST:event_fromURLButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton commonGenomeRadio;
    private javax.swing.JButton fromFileButton;
    private javax.swing.JButton fromOtherButton;
    private javax.swing.JButton fromURLButton;
    private javax.swing.JComboBox genesCombo;
    private javax.swing.JComboBox genomesCombo;
    private javax.swing.JTextField lengthField;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton userSpecifiedRadio;
    private javax.swing.ButtonGroup withoutSequenceButtonGroup;
    // End of variables declaration//GEN-END:variables
    public boolean isPopularGenome = true;
    public Genome loadedGenome;

    private void initDropDowns() {
        genomesCombo.setModel(new DefaultComboBoxModel(Genome.getDefaultGenomes()));
        genomesCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGeneList();
            }
        });
        updateGeneList();
    }

    private void updateGeneList() {
        Genome curGenome = (Genome)genomesCombo.getSelectedItem();
        genesCombo.setModel(new DefaultComboBoxModel(curGenome.getGenes()));
        genesCombo.setEnabled(genesCombo.getItemCount() > 0);
    }

    private void updateEnabledControls() {
        setPublishedGenomeControlsEnabled(commonGenomeRadio.isSelected());
        setUserSpecifiedControlsEnabled(userSpecifiedRadio.isSelected());
    }

    private void setPublishedGenomeControlsEnabled(boolean isEnabled) {
        genomesCombo.setEnabled(isEnabled);
        genesCombo.setEnabled(isEnabled && genesCombo.getItemCount() > 0);
    }

    private void setUserSpecifiedControlsEnabled(boolean isEnabled) {
        lengthField.setEnabled(isEnabled);
        nameField.setEnabled(isEnabled);
    }

    private boolean validateUserSpecifiedLength() {
        String refname = nameField.getText();
        if (refname.equals("")) {
            DialogUtils.displayError("Invalid name.");
            nameField.requestFocus();
            return false;
        }

        String text = lengthField.getText();
        try {
            int i = Integer.parseInt(text);
            if (i <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            DialogUtils.displayError("Invalid length.");
            lengthField.requestFocus();
            return false;
        }

        return true;
    }
}
