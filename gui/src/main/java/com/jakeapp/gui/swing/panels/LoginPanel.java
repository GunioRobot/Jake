/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LoginRegisterPanel.java
 *
 * Created on Nov 25, 2008, 5:01:39 PM
 */

package com.jakeapp.gui.swing.panels;

import com.jakeapp.gui.swing.JakeMainView;
import com.jakeapp.gui.swing.callbacks.ConnectionStatus;
import com.jakeapp.gui.swing.callbacks.RegistrationStatus;
import com.jakeapp.gui.swing.helpers.Platform;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.LinkAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author studpete
 */
public class LoginPanel extends javax.swing.JPanel implements RegistrationStatus, ConnectionStatus {
    private ResourceMap resourceMap;
    private static final Logger log = Logger.getLogger(LoginPanel.class);
    private JPanel loginSuccessPanel;


    /**
     * Creates new form LoginRegisterPanel
     */
    public LoginPanel() {
        initComponents();
        setResourceMap(org.jdesktop.application.Application.getInstance(com.jakeapp.gui.swing.JakeMainApp.class).getContext().getResourceMap(LoginPanel.class));

        // register the connection & reg status callback!
        JakeMainView.getMainView().getCore().registerConnectionStatusCallback(this);
        JakeMainView.getMainView().getCore().registerRegistrationStatusCallback(this);

        // fill the registraton info panel
        registrationInfoPanel.setLayout(new MigLayout("wrap 2"));
        JLabel registrationLabel1 = new JLabel(getResourceMap().getString("registrationLabel1"));
        registrationLabel1.setForeground(Color.DARK_GRAY);
        registrationInfoPanel.add(registrationLabel1, "span 2, wrap");
        JLabel registrationLabel2 = new JLabel(getResourceMap().getString("registrationLabel2"));
        registrationLabel2.setForeground(Color.DARK_GRAY);
        registrationInfoPanel.add(registrationLabel2);
        LinkAction linkAction = new LinkAction(getResourceMap().getString("registrationLabel3")) {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(getResourceMap().getString("registrationLabelHyperlink")));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                setVisited(true);
            }
        };
        registrationInfoPanel.add(new JXHyperlink(linkAction));
        registrationInfoPanel.setOpaque(false);
        registrationInfoPanel.updateUI();

        // make controls transparent
        loginRadioButton.setOpaque(false);
        registerRadioButton.setOpaque(false);
        rememberPasswordCheckBox.setOpaque(false);

        // install event listener for user text field
        // TODO: need an event listerer that works as key changes!
        usernameComboBox.getModel().addListDataListener(new ListDataListener() {

            public void intervalAdded(ListDataEvent listDataEvent) {
                updateSignInRegisterButton();
            }

            public void intervalRemoved(ListDataEvent listDataEvent) {
                updateSignInRegisterButton();
            }

            public void contentsChanged(ListDataEvent listDataEvent) {
                updateSignInRegisterButton();
            }
        });

        // instlal event listener for password text field
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent documentEvent) {
                updateSignInRegisterButton();
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                updateSignInRegisterButton();
            }

            public void changedUpdate(DocumentEvent documentEvent) {
                updateSignInRegisterButton();
            }
        });

        // set the background painter
        loginPanel.setBackgroundPainter(Platform.getStyler().getContentPanelBackgroundPainter());

        // make initial update
        updateView();
    }

    /**
     * Updates the main view.
     * Shows the signIn-Register/signInStatus Panel, evaluates state.
     */
    private void updateView() {
        log.info("updating login view. signedIn=" + JakeMainView.getMainView().getCore().isSignedIn());

        // load the default name list
        usernameComboBox.setModel(new DefaultComboBoxModel(JakeMainView.getMainView().getCore().getLastSignInNames()));


        // update the view (maybe already logged in)
        if (JakeMainView.getMainView().getCore().isSignedIn()) {
            showSignInSuccess();
        } else {
            showSignInForm();
        }
    }

    /**
     * Shows the Sign In Form.
     */
    private void showSignInForm() {
        log.info("Show Sign In Form...");

        // set signIn button
        selectionChanged();

        // set the welcome jake-text
        messageLabel.setText(getResourceMap().getString("loginMessageLabel"));

        loginPanel.remove(getLoginSuccessPanel());

        if (loginControlPanel.getParent() != mainLoginPanel) {
            mainLoginPanel.add(loginControlPanel);
        }

        mainLoginPanel.updateUI();
    }


    /**
     * Show Sign In success.
     */
    private void showSignInSuccess() {
        log.info("Show Sign In Success.");

        // change the message on top
        messageLabel.setText(getResourceMap().getString("signInSuccessMessage"));

        loginPanel.add(getLoginSuccessPanel(), BorderLayout.SOUTH);
        mainLoginPanel.remove(loginControlPanel);
        mainLoginPanel.updateUI();
    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents
            () {

        buttonGroup1 = new javax.swing.ButtonGroup();
        loginPanel = new org.jdesktop.swingx.JXPanel();
        mainLoginPanel = new javax.swing.JPanel();
        messagePanel = new javax.swing.JPanel();
        messageLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        loginControlPanel = new org.jdesktop.swingx.JXPanel();
        signInRegisterButton = new javax.swing.JButton();
        passwordLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        usernameComboBox = new javax.swing.JComboBox();
        rememberPasswordCheckBox = new javax.swing.JCheckBox();
        registerRadioButton = new javax.swing.JRadioButton();
        loginRadioButton = new javax.swing.JRadioButton();
        advancedServerOptionsHolderPanel = new javax.swing.JPanel();
        advancedServerOptionsPanel = new javax.swing.JPanel();
        serverLabel = new javax.swing.JLabel();
        serverComboBox = new javax.swing.JComboBox();
        advancedServerButton = new javax.swing.JButton();
        registrationInfoPanel = new javax.swing.JPanel();
        passwordField = new javax.swing.JPasswordField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.jakeapp.gui.swing.JakeMainApp.class).getContext().getResourceMap(LoginPanel.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        loginPanel.setBackground(resourceMap.getColor("loginPanel.background")); // NOI18N
        loginPanel.setName("loginPanel"); // NOI18N
        loginPanel.setLayout(new java.awt.BorderLayout());

        mainLoginPanel.setName("mainLoginPanel"); // NOI18N
        mainLoginPanel.setOpaque(false);
        mainLoginPanel.setLayout(new javax.swing.BoxLayout(mainLoginPanel, javax.swing.BoxLayout.Y_AXIS));

        messagePanel.setMaximumSize(new java.awt.Dimension(494, 140));
        messagePanel.setMinimumSize(new java.awt.Dimension(494, 140));
        messagePanel.setName("messagePanel"); // NOI18N
        messagePanel.setOpaque(false);
        messagePanel.setPreferredSize(new java.awt.Dimension(494, 140));

        messageLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        messageLabel.setName("messageLabel"); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
                messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(messagePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(31, Short.MAX_VALUE))
        );
        messagePanelLayout.setVerticalGroup(
                messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messagePanelLayout.createSequentialGroup()
                        .addContainerGap(25, Short.MAX_VALUE)
                        .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
        );

        mainLoginPanel.add(messagePanel);

        loginControlPanel.setMaximumSize(new java.awt.Dimension(491, 32000));
        loginControlPanel.setMinimumSize(new java.awt.Dimension(491, 348));
        loginControlPanel.setName("loginControlPanel"); // NOI18N
        loginControlPanel.setOpaque(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.jakeapp.gui.swing.JakeMainApp.class).getContext().getActionMap(LoginPanel.class, this);
        signInRegisterButton.setAction(actionMap.get("signInRegisterButtonPressed")); // NOI18N
        signInRegisterButton.setText(resourceMap.getString("signInRegisterButton.text")); // NOI18N
        signInRegisterButton.setName("signInRegisterButton"); // NOI18N

        passwordLabel.setForeground(resourceMap.getColor("passwordLabel.foreground")); // NOI18N
        passwordLabel.setText(resourceMap.getString("passwordLabel.text")); // NOI18N
        passwordLabel.setName("passwordLabel"); // NOI18N

        usernameLabel.setForeground(resourceMap.getColor("usernameLabel.foreground")); // NOI18N
        usernameLabel.setText(resourceMap.getString("usernameLabel.text")); // NOI18N
        usernameLabel.setName("usernameLabel"); // NOI18N

        usernameComboBox.setEditable(true);
        usernameComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"   ", "pstein", "csutter"}));
        usernameComboBox.setName("usernameComboBox"); // NOI18N
        usernameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameComboBoxActionPerformed(evt);
            }
        });

        rememberPasswordCheckBox.setSelected(true);
        rememberPasswordCheckBox.setText(resourceMap.getString("rememberPasswordCheckBox.text")); // NOI18N
        rememberPasswordCheckBox.setName("rememberPasswordCheckBox"); // NOI18N

        registerRadioButton.setAction(actionMap.get("selectionChanged")); // NOI18N
        buttonGroup1.add(registerRadioButton);
        registerRadioButton.setText(resourceMap.getString("registerRadioButton.text")); // NOI18N
        registerRadioButton.setName("registerRadioButton"); // NOI18N

        loginRadioButton.setAction(actionMap.get("selectionChanged")); // NOI18N
        buttonGroup1.add(loginRadioButton);
        loginRadioButton.setSelected(true);
        loginRadioButton.setText(resourceMap.getString("loginRadioButton.text")); // NOI18N
        loginRadioButton.setName("loginRadioButton"); // NOI18N

        advancedServerOptionsHolderPanel.setMinimumSize(new java.awt.Dimension(396, 63));
        advancedServerOptionsHolderPanel.setName("advancedServerOptionsHolderPanel"); // NOI18N
        advancedServerOptionsHolderPanel.setOpaque(false);
        advancedServerOptionsHolderPanel.setLayout(new java.awt.BorderLayout());

        advancedServerOptionsPanel.setMinimumSize(new java.awt.Dimension(358, 46));
        advancedServerOptionsPanel.setName("advancedServerOptionsPanel"); // NOI18N
        advancedServerOptionsPanel.setOpaque(false);

        serverLabel.setText(resourceMap.getString("serverLabel.text")); // NOI18N
        serverLabel.setName("serverLabel"); // NOI18N

        serverComboBox.setEditable(true);
        serverComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"jabber.fsinf.at", "jabber.org", "jabber.ccc.de", "macjabber.de", "swissjabber.ch", "binaryfreedom.info"}));
        serverComboBox.setName("serverComboBox"); // NOI18N

        advancedServerButton.setText(resourceMap.getString("advancedServerButton.text")); // NOI18N
        advancedServerButton.setName("advancedServerButton"); // NOI18N

        registrationInfoPanel.setName("registrationInfoPanel"); // NOI18N
        registrationInfoPanel.setOpaque(false);

        javax.swing.GroupLayout advancedServerOptionsPanelLayout = new javax.swing.GroupLayout(advancedServerOptionsPanel);
        advancedServerOptionsPanel.setLayout(advancedServerOptionsPanelLayout);
        advancedServerOptionsPanelLayout.setHorizontalGroup(
                advancedServerOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(advancedServerOptionsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(advancedServerOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(registrationInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(advancedServerOptionsPanelLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(serverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverComboBox, 0, 183, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(advancedServerButton)))
                        .addContainerGap())
        );
        advancedServerOptionsPanelLayout.setVerticalGroup(
                advancedServerOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(advancedServerOptionsPanelLayout.createSequentialGroup()
                        .addComponent(registrationInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(advancedServerOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(serverComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(advancedServerButton)
                        .addComponent(serverLabel)))
        );

        advancedServerOptionsHolderPanel.add(advancedServerOptionsPanel, java.awt.BorderLayout.CENTER);

        passwordField.setText(resourceMap.getString("passwordField.text")); // NOI18N
        passwordField.setName("passwordField"); // NOI18N
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginControlPanelLayout = new javax.swing.GroupLayout(loginControlPanel);
        loginControlPanel.setLayout(loginControlPanelLayout);
        loginControlPanelLayout.setHorizontalGroup(
                loginControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginControlPanelLayout.createSequentialGroup()
                        .addContainerGap(35, Short.MAX_VALUE)
                        .addGroup(loginControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, loginControlPanelLayout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(loginControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(usernameComboBox, 0, 376, Short.MAX_VALUE)
                                        .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(loginControlPanelLayout.createSequentialGroup()
                                                .addComponent(rememberPasswordCheckBox)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(signInRegisterButton))
                                        .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                                        .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(registerRadioButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(loginRadioButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(advancedServerOptionsHolderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
        );
        loginControlPanelLayout.setVerticalGroup(
                loginControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginControlPanelLayout.createSequentialGroup()
                        .addContainerGap(88, Short.MAX_VALUE)
                        .addComponent(loginRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(registerRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(advancedServerOptionsHolderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameLabel)
                        .addGap(1, 1, 1)
                        .addComponent(usernameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordLabel)
                        .addGap(2, 2, 2)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(loginControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(loginControlPanelLayout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(rememberPasswordCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(loginControlPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(signInRegisterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(32, 32, 32))
        );

        mainLoginPanel.add(loginControlPanel);

        loginPanel.add(mainLoginPanel, java.awt.BorderLayout.CENTER);

        add(loginPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Returns true if Sign In / Registering is possible.
     *
     * @return
     */

    private boolean isSignInRegisterButtonEnabled() {
        return !(usernameComboBox.getSelectedItem().toString().isEmpty() || passwordField.getPassword().length == 0);
    }

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        updateSignInRegisterButton();
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void usernameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameComboBoxActionPerformed
        signInRegisterButtonPressed();
    }//GEN-LAST:event_usernameComboBoxActionPerformed

    @Action
    public void selectionChanged() {
        advancedServerOptionsPanel.setVisible(registerRadioButton.isSelected());

        // update the button
        updateSignInRegisterButton();
    }

    private void updateSignInRegisterButton() {
        log.info("updating signin-button.");
        if (isModeSignIn()) {
            signInRegisterButton.setText(getResourceMap().getString("loginSignIn"));
        } else {
            signInRegisterButton.setText(getResourceMap().getString("loginRegister"));
        }

        // disable the button as long as no credidentals are entered
        signInRegisterButton.setEnabled(isSignInRegisterButtonEnabled());
    }

    private boolean isModeSignIn() {
        return loginRadioButton.isSelected();
    }

    @Action
    public void signInRegisterButtonPressed() {
        if (isSignInRegisterButtonEnabled()) {

            if (isModeSignIn()) {
                JakeMainView.getMainView().getCore().signIn(usernameLabel.getText(), passwordLabel.getText());
            } else {
                JakeMainView.getMainView().getCore().register(usernameLabel.getText(), passwordLabel.getText());
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton advancedServerButton;
    private javax.swing.JPanel advancedServerOptionsHolderPanel;
    private javax.swing.JPanel advancedServerOptionsPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private org.jdesktop.swingx.JXPanel loginControlPanel;
    private org.jdesktop.swingx.JXPanel loginPanel;
    private javax.swing.JRadioButton loginRadioButton;
    private javax.swing.JPanel mainLoginPanel;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JRadioButton registerRadioButton;
    private javax.swing.JPanel registrationInfoPanel;
    private javax.swing.JCheckBox rememberPasswordCheckBox;
    private javax.swing.JComboBox serverComboBox;
    private javax.swing.JLabel serverLabel;
    private javax.swing.JButton signInRegisterButton;
    private javax.swing.JComboBox usernameComboBox;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables


    public void setRegistrationStatus(final RegisterStati status, final String msg) {
        log.info("got registration status update: " + status);

        Runnable runner = new Runnable() {
            public void run() {
                updateView();

                if (status == RegisterStati.RegistrationActive) {
                    signInRegisterButton.setText(getResourceMap().getString("loginRegisterProceed"));
                    signInRegisterButton.setEnabled(false);
                }
            }
        };

        SwingUtilities.invokeLater(runner);
    }

    // TODO!
    private void showRegistrationSuccess() {
    }

    public void setConnectionStatus(final ConnectionStati status, final String msg) {
        log.info("got connection status update: " + status);

        Runnable runner = new Runnable() {
            public void run() {
                // always update view
                updateView();

                if (status == ConnectionStati.SigningIn) {
                    signInRegisterButton.setText(getResourceMap().getString("loginSignInProceed"));
                    signInRegisterButton.setEnabled(false);
                }
            }
        };

        SwingUtilities.invokeLater(runner);
    }


    private JPanel createSignInSuccessPanel() {
        // create the drag & drop hint
        JPanel loginSuccessPanel = new JPanel();
        loginSuccessPanel.setOpaque(false);
        loginSuccessPanel.setLayout(new MigLayout("nogrid, al center, fill"));

        // the sign out button
        JButton signOutButton = new JButton(getResourceMap().getString("signInSuccessSignOut"));
        signOutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                JakeMainView.getMainView().getCore().signOut();
            }
        });
        loginSuccessPanel.add(signOutButton, "wrap, al center");

        JLabel iconSuccess = new JLabel();
        iconSuccess.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/icons/dropfolder.png"))));

        loginSuccessPanel.add(iconSuccess, "wrap, al center");

        JLabel messageSuccess1 = new JLabel(getResourceMap().getString("dragDropHint1"));
        messageSuccess1.setFont(Platform.getStyler().getH1Font());
        messageSuccess1.setForeground(Color.DARK_GRAY);
        loginSuccessPanel.add(messageSuccess1, "wrap, al center");

        JLabel messageSuccess2 = new JLabel(getResourceMap().getString("dragDropHint2"));
        messageSuccess2.setFont(Platform.getStyler().getH1Font());
        messageSuccess2.setForeground(Color.DARK_GRAY);
        loginSuccessPanel.add(messageSuccess2, "al center");
        return loginSuccessPanel;
    }

    public ResourceMap getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(ResourceMap resourceMap) {
        this.resourceMap = resourceMap;
    }

    /**
     * Returns the Login Success Panel.
     * Uses lazy-loading to create the panel.
     */
    public JPanel getLoginSuccessPanel() {
        if (loginSuccessPanel == null) {
            loginSuccessPanel = createSignInSuccessPanel();
        }

        return loginSuccessPanel;
    }

    public void setLoginSuccessPanel(JPanel loginSuccessPanel) {
        this.loginSuccessPanel = loginSuccessPanel;
    }
}
