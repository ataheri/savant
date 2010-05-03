/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BAMParametersDialog.java
 *
 * Created on Apr 20, 2010, 11:20:40 AM
 */

package savant.view.swing;

import java.awt.*;

/**
 *
 * @author vwilliam
 */
public class BAMParametersDialog extends javax.swing.JDialog {

    private int discordantMin;
    private int discordantMax;
    private double arcLengthThreshold;

    private boolean cancelled = false;
    private boolean accepted  = true;

    /** Creates new form BAMParametersDialog */
    public BAMParametersDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.setModal(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        textDiscordantMin = new javax.swing.JTextField();
        textDiscordantMax = new javax.swing.JTextField();
        textArcThreshold = new javax.swing.JTextField();
        buttonCancel = new javax.swing.JButton();
        buttonOK = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("BAM Arc Parameters");

        jLabel1.setText("Min normal mapped distance:");

        jLabel2.setText("Max normal mapped distance:");

        jLabel3.setText("Don't display mapped distances smaller than:");

        textDiscordantMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textDiscordantMinActionPerformed(evt);
            }
        });

        textDiscordantMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textDiscordantMaxActionPerformed(evt);
            }
        });

        textArcThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textArcThresholdActionPerformed(evt);
            }
        });

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonOK.setText("OK");
        buttonOK.setSelected(true);
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKActionPerformed(evt);
            }
        });

        jLabel4.setText("eg. 100 or 10%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textDiscordantMin, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                            .addComponent(textDiscordantMax, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textArcThreshold, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOK)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textDiscordantMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textDiscordantMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textArcThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOK)
                    .addComponent(buttonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textDiscordantMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textDiscordantMinActionPerformed
//        parseDiscordantMin();
    }//GEN-LAST:event_textDiscordantMinActionPerformed

    private void textDiscordantMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textDiscordantMaxActionPerformed
//        parseDiscordantMax();
    }//GEN-LAST:event_textDiscordantMaxActionPerformed

    private void textArcThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textArcThresholdActionPerformed
//        parseArcThreshold();
    }//GEN-LAST:event_textArcThresholdActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        setCancelled(true);
        this.setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
        if (parseDiscordantMin() && parseDiscordantMax() && parseArcThreshold()) {
            setAccepted(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_buttonOKActionPerformed

    private boolean parseDiscordantMin() {

        boolean result = false;
        String minStr = textDiscordantMin.getText();
        if (minStr == null || minStr.equals("")) {
            setDiscordantMin(Integer.MIN_VALUE);
            result = true;
        }
        else {
            try {
                setDiscordantMin(Integer.parseInt(minStr));
                result = true;
            } catch (NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                textDiscordantMin.setText("");
                textDiscordantMin.grabFocus();
            }
        }
        return result;
    }

    private boolean parseDiscordantMax() {

        boolean result = false;
        String maxStr = textDiscordantMax.getText();
        if (maxStr == null || maxStr.equals("")) {
            setDiscordantMax(Integer.MAX_VALUE);
            result = true;
        }
        else {
            try {
                setDiscordantMax(Integer.parseInt(maxStr));
                result = true;
            } catch (NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                textDiscordantMax.setText("");
                textDiscordantMax.grabFocus();
            }
        }
        return result;
    }

    private boolean parseArcThreshold() {

        boolean result = false;
        String threshStr = textArcThreshold.getText();
        if (threshStr == null || threshStr.equals("")) {
            setArcLengthThreshold(Integer.MIN_VALUE);
            result = true;
        }
        else {
            boolean percent = false;
            if (threshStr.endsWith("%")) {
                threshStr = threshStr.substring(0, threshStr.length()-1);
                percent = true;
            }
            try {
                double tempThresh = Double.parseDouble(threshStr);
                if (percent) {
                    setArcLengthThreshold(tempThresh/100.0d);
                }
                else {
                    setArcLengthThreshold(tempThresh);
                }
                result = true;
            } catch (NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                textArcThreshold.setText("");
                textArcThreshold.grabFocus();
            }
        }
        return result;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BAMParametersDialog dialog = new BAMParametersDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField textArcThreshold;
    private javax.swing.JTextField textDiscordantMax;
    private javax.swing.JTextField textDiscordantMin;
    // End of variables declaration//GEN-END:variables


    public int getDiscordantMin() {
        return discordantMin;
    }

    public void setDiscordantMin(int discordantMin) {
        this.discordantMin = discordantMin;
    }

    public int getDiscordantMax() {
        return discordantMax;
    }

    public void setDiscordantMax(int discordantMax) {
        this.discordantMax = discordantMax;
    }

    public double getArcLengthThreshold() {
        return arcLengthThreshold;
    }

    public void setArcLengthThreshold(double arcLengthThreshold) {
        this.arcLengthThreshold = arcLengthThreshold;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
