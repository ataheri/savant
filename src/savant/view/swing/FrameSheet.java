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

package savant.view.swing;


import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import savant.controller.FrameController;
import savant.controller.event.FrameRemovedEvent;
import savant.controller.event.FrameAddedListener;
import savant.controller.event.FrameChangedEvent;
import savant.controller.event.FrameRemovedListener;
import savant.controller.event.FrameAddedEvent;

/**
 *
 * @author mfiume
 */
class FrameSheet implements FrameAddedListener, FrameRemovedListener {

    Savant parent;
    JPanel panel;
    JList list;
    //private Hashtable frame2track;
    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;

    public FrameSheet(Savant parent, JPanel panel) {
        this.panel = panel;
        this.parent = parent;
        initFrameSheet();
        updateFrameSheet();
        //FrameController.getInstance().addFrameAddedListener(this);
        //FrameController.getInstance().addFrameRemovedListener(this);
    }

    private void updateFrameSheetList() {
        DefaultListModel model = (DefaultListModel) this.list.getModel();
        for (int i = 0; i < FrameController.getInstance().getFrames().size(); i++) {
            model.add(i, FrameController.getInstance().getFrames().get(i).getTracks()[0].getName());
        }
    }

    private void updateFrameSheet() {
        addObject("New Node");
    }

    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            //There is no selection. Default to the root node.
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode =
                new DefaultMutableTreeNode(child);

        treeModel.insertNodeInto(childNode, parent,
                                 parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    private Map<String, String[]> getFrameTree() {

        List<Frame> frames = FrameController.getInstance().getFrames();
        Map<String, String[]> framesTree = new HashMap<String, String[]>();

        for (int j = 0; j < frames.size(); j++) {
            Frame fr = frames.get(j);
            String[] trackNames = new String[fr.getTracks().length];
            for (int i = 0; i < fr.getTracks().length; i++) {
                trackNames[i] = fr.getTracks()[i].getName();
            }
            framesTree.put("Frame " + (j+1), trackNames);
        }

        printMap(framesTree);

        return framesTree;
    }

    public void frameChangedReceived(FrameChangedEvent event) {
        updateFrameSheet();
    }

    private void printMap(Map<String, String[]> framesTree) {

        for (String key : framesTree.keySet()) {
            String[] value = framesTree.get(key);
            for (String val : value) {
                //System.out.println("\t" + val);
            }
        }
    }

    private void initFrameSheetList() {
        list = new JList();
        list.setModel(new DefaultListModel());
        this.panel.add(list, BorderLayout.CENTER);
    }

    private void initFrameSheet() {
        rootNode = new DefaultMutableTreeNode("Root Node");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new FrameTreeModelListener());

        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        this.panel.add(tree,BorderLayout.CENTER);
    }

    @Override
    public void frameAddedReceived(FrameAddedEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void frameRemovedReceived(FrameRemovedEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
