/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NotesPanel.java
 *
 * Created on Dec 3, 2008, 2:00:15 AM
 */

package com.jakeapp.gui.swing.panels;

import com.jakeapp.core.domain.Project;
import com.jakeapp.gui.swing.ICoreAccess;
import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.callbacks.ProjectChanged;
import com.jakeapp.gui.swing.callbacks.ProjectSelectionChanged;
import com.jakeapp.gui.swing.controls.ETable;
import com.jakeapp.gui.swing.helpers.Colors;
import com.jakeapp.gui.swing.helpers.JakePopupMenu;
import com.jakeapp.gui.swing.listener.TableMouseListener;
import com.jakeapp.gui.swing.models.NotesTableModel;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * @author studpete, simon
 */
public class NotesPanel extends javax.swing.JPanel implements ProjectSelectionChanged, ProjectChanged, ListSelectionListener {

    private static final long serialVersionUID = -7703570005631651276L;
    private static Logger log = Logger.getLogger(NotesPanel.class);
    private NotesTableModel notesTableModel;
    private ResourceMap resourceMap;
    private JTextArea noteReader;

    /**
     * Creates new form NotesPanel
     */
    public NotesPanel() {
    	// init components
        initComponents();

        // set up table model
        this.notesTableModel = new NotesTableModel();
        this.notesTable.setModel(this.notesTableModel);

        // register the callbacks
        JakeMainApp.getApp().addProjectSelectionChangedListener(this);
        JakeMainApp.getApp().getCore().addProjectChangedCallbackListener(this);
        this.notesTable.getSelectionModel().addListSelectionListener(this);

        // get resource map
        this.resourceMap = org.jdesktop.application.Application.getInstance(
                com.jakeapp.gui.swing.JakeMainApp.class).getContext()
                .getResourceMap(NotesPanel.class);


        this.notesTable.setSortable(true);
        this.notesTable.setColumnControlVisible(true);
        //this.notesTable.setHighlighters(HighlighterFactory.createSimpleStriping());

        final JPopupMenu notesPopupMenu = new JakePopupMenu();

        //TODO wire context menu
        
        notesPopupMenu.add(new JMenuItem(this.getResourceMap().getString("contextMenuNewNote")));
        notesPopupMenu.add(new JMenuItem(this.getResourceMap().getString("contextMenuDeleteNote")));

        this.notesTable.addMouseListener(new TableMouseListener(this.notesTable) {

            @Override
            public void showPopup(JComponent comp, int x, int y) {
                notesPopupMenu.show(comp, x, y);
            }

            @Override
            public void editAction() {
            }
        });


        this.noteReader = new JTextArea();
        // noteReader.setBorder(new LineBorder(Color.BLACK, 1));
        this.noteReader.setLineWrap(true);
        this.noteReader.setOpaque(false);
        this.noteReader.setText("Enter your Note here.\nChanges will be saved automatically.");
        this.noteReader.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane noteReaderScrollPane = new JScrollPane(this.noteReader);
        noteReaderScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        noteReaderScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        noteReaderScrollPane.setOpaque(false);
        noteReaderScrollPane.getViewport().setOpaque(false);

        this.noteReadPanel.add(noteReaderScrollPane);

        // set the background painter
        MattePainter mp = new MattePainter(Colors.Yellow.alpha(0.5f));
        GlossPainter gp = new GlossPainter(Colors.White.alpha(0.3f),
                GlossPainter.GlossPosition.TOP);
        this.noteReadPanel.setBackgroundPainter(new CompoundPainter(mp, gp));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (this.notesTable.getSelectedRow() == -1) {
            this.noteReader.setText("");
        } else {
            String text;
            text = this.notesTableModel.getNoteAtRow(this.notesTable.getSelectedRow()).getContent();
            this.noteReader.setText(text);
        }
    }

    private ResourceMap getResourceMap() {
        return this.resourceMap;
    }

    @Override
    public void projectChanged(ProjectChangedEvent ignored) {
        this.notesTableModel.update();
    }

    @Override
    public void setProject(Project pr) {
        this.notesTableModel.update(pr);
    }

    private boolean isNoteSelected() {
        return this.notesTable.getSelectedRow() >= 0;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        notesTable = new ETable();
        noteReadPanel = new org.jdesktop.swingx.JXPanel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerSize(2);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"Gui redesign nicht vergessen...", "8.12.2008 18:00", "Peter"},
                        {"Blabla", "9.12.2008", "Simon"},
                        {"xxx", "10.12", "a"},
                        {"bbb", "11.12", "b"}
                },
                new String[]{
                        "Note", "Date", "User"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        notesTable.setName("notesTable"); // NOI18N
        jScrollPane2.setViewportView(notesTable);

        jSplitPane1.setLeftComponent(jScrollPane2);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.jakeapp.gui.swing.JakeMainApp.class).getContext().getResourceMap(NotesPanel.class);
        noteReadPanel.setBackground(resourceMap.getColor("noteReadPanel.background")); // NOI18N
        noteReadPanel.setName("noteReadPanel"); // NOI18N
        noteReadPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(noteReadPanel);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private org.jdesktop.swingx.JXPanel noteReadPanel;
    private org.jdesktop.swingx.JXTable notesTable;
    // End of variables declaration//GEN-END:variables
}
