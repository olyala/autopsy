/*
 * Autopsy Forensic Browser
 *
 * Copyright 2012 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.casemodule;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPanel;

/**
 * ImageTypePanel for adding an image file such as .img, .E0x, .00x, etc.
 */
public class ImageFilePanel extends JPanel implements DocumentListener {
    private static ImageFilePanel instance = null;
    private PropertyChangeSupport pcs = null;
    private JFileChooser fc = new JFileChooser();

    /**
     * Creates new form ImageFilePanel
     */
    public ImageFilePanel() {
        initComponents();
        fc.setDragEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.addChoosableFileFilter(AddImageWizardChooseDataSourceVisual.rawFilter);
        fc.addChoosableFileFilter(AddImageWizardChooseDataSourceVisual.encaseFilter);
        fc.setFileFilter(AddImageWizardChooseDataSourceVisual.allFilter);
    }
    
    /**
     * Returns the default instance of a ImageFilePanel.
     */
    public static synchronized ImageFilePanel getDefault() {
        if (instance == null) {
            instance = new ImageFilePanel();
	    instance.postInit();
        }
        return instance;
    }

    //post-constructor initialization to properly initialize listener support
    //without leaking references of uninitialized objects
    private void postInit() {
        pathTextField.getDocument().addDocumentListener(this);
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pathLabel = new javax.swing.JLabel();
        browseButton = new javax.swing.JButton();
        pathTextField = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(0, 65));
        setPreferredSize(new java.awt.Dimension(403, 65));

        org.openide.awt.Mnemonics.setLocalizedText(pathLabel, org.openide.util.NbBundle.getMessage(ImageFilePanel.class, "ImageFilePanel.pathLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(ImageFilePanel.class, "ImageFilePanel.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        pathTextField.setText(org.openide.util.NbBundle.getMessage(ImageFilePanel.class, "ImageFilePanel.pathTextField.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(browseButton)
                .addGap(2, 2, 2))
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathLabel)
                .addGap(0, 284, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseButton)
                    .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        String oldText = pathTextField.getText();
        // set the current directory of the FileChooser if the ImagePath Field is valid
        File currentDir = new File(oldText);
        if (currentDir.exists()) {
            fc.setCurrentDirectory(currentDir);
        }

        int retval = fc.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            pathTextField.setText(path);
        }
        pcs.firePropertyChange(AddImageWizardChooseDataSourceVisual.EVENT.FOCUS_NEXT.toString(), false, true);
    }//GEN-LAST:event_browseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JTextField pathTextField;
    // End of variables declaration//GEN-END:variables
        
    /**
     * Get the path of the user selected image.
     * @return the image path
     */
    public String getContentPaths() {
        return pathTextField.getText();
    }

    /**
     * Set the path of the image file.
     */
    public void setContentPath(String s) {
        pathTextField.setText(s);
    }

    public String getContentType() {
        return "IMAGE";
    }

    public void reset() {
        //nothing to reset
    }
    
    /**
     * Should we enable the next button of the wizard?
     * @return true if a proper image has been selected, false otherwise
     */
    public boolean validatePanel() {
        String path = getContentPaths();
        if (path == null || path.isEmpty()) {
            return false;
        }
        boolean isExist = Case.pathExists(path);
        boolean isPhysicalDrive = Case.isPhysicalDrive(path);
        boolean isPartition = Case.isPartition(path);
        
        return (isExist || isPhysicalDrive || isPartition);
    }

    /**
     * Update functions are called by the pathTextField which has this set
     * as it's DocumentEventListener. Each update function fires a property change
     * to be caught by the parent panel.
     * @param e the event, which is ignored
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        pcs.firePropertyChange(AddImageWizardChooseDataSourceVisual.EVENT.UPDATE_UI.toString(), false, true);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        pcs.firePropertyChange(AddImageWizardChooseDataSourceVisual.EVENT.UPDATE_UI.toString(), false, true);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        pcs.firePropertyChange(AddImageWizardChooseDataSourceVisual.EVENT.UPDATE_UI.toString(), false, true);
    }
    
    /**
     * Set the focus to the pathTextField.
     */
    public void select() {
        pathTextField.requestFocusInWindow();
    }
    
    
    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener pcl) {
	super.addPropertyChangeListener(pcl);

	if (pcs == null) {
	    pcs = new PropertyChangeSupport(this);
	}

        pcs.addPropertyChangeListener(pcl);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
	super.removePropertyChangeListener(pcl);

        pcs.removePropertyChangeListener(pcl);
    }

}
