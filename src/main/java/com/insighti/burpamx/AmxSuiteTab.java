package com.insighti.burpamx;

import burp.BurpExtender;
import burp.ITab;

import javax.swing.*;
import java.awt.*;

public class AmxSuiteTab extends JPanel implements ITab {

    private JPanel guiHolder = new JPanel();

    private JLabel appIdLabel = new JLabel("App Id:");
    private JTextField appIdText = new JTextField(30);

    private JLabel appSecretLabel = new JLabel("App Secret:");
    private JTextField appSecretText = new JTextField(30);

    private JLabel intro = new JLabel("<html><h2>" + BurpExtender.EXTENSION_NAME + "</h2><center><em>" +
            "insighti (c) 2017</em></center><br/><em>Calculates and adds AMX Authorization headers to requests</em>" +
            "<br /><br />Fill out the inputs below and use <strong>Session Handling Rules</strong>.<br/><br/></html>");

    public AmxSuiteTab() {
        this.setLayout(new GridBagLayout());
        guiHolder.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 5, 2, 5);

        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        guiHolder.add(intro, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        guiHolder.add(appIdLabel, gbc);

        gbc.gridx++;
        guiHolder.add(appIdText, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        guiHolder.add(appSecretLabel, gbc);

        gbc.gridx++;
        guiHolder.add(appSecretText, gbc);

        // add guiHolder to the main panel
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 15, 0, 0);
        this.add(guiHolder, gbc);

        BurpExtender.callbacks.customizeUiComponent(this);
    }

    @Override
    public String getTabCaption() {
        return "AMX Auth";
    }

    @Override
    public Component getUiComponent() {
        return this;
    }

    public String getAppId() {
        return appIdText.getText();
    }

    public String getAppSecret() {
        return appSecretText.getText();
    }
}
