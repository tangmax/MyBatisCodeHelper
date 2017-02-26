package com.ccnode.codegenerator.myconfigurable;

import javax.swing.*;
import java.awt.*;

/**
 * @Author bruce.ge
 * @Date 2017/2/20
 * @Description
 */
public class SettingDialog {
    private JPanel rootComponent;

    private JLabel databaseLabel;

    private JComboBox databaseCombox;

    private JCheckBox addMapperAnnotationCheckBox;

    private JCheckBox useGeneratedKeysCheckBox;

    private PluginState settings;

    public SettingDialog(PluginState state) {
        init(state);
    }

    private void init(PluginState state) {
        this.settings = state;
        Profile profile = state.getProfile();
        rootComponent = new JPanel(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        bag.anchor = GridBagConstraints.NORTHWEST;
        bag.gridx = 0;
        bag.gridy = 0;
        bag.weightx = 0.1;
        bag.weighty = 1;
        databaseLabel = new JLabel("database:");
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 15);
        databaseLabel.setFont(font);
        rootComponent.add(databaseLabel, bag);
        databaseCombox = new JComboBox();
        databaseCombox.addItem(DataBaseConstants.MYSQL);
        databaseCombox.addItem(DataBaseConstants.ORACLE);
        for (int i = 0; i < databaseCombox.getItemCount(); i++) {
            Object itemAt = databaseCombox.getItemAt(i);
            if (itemAt.equals(profile.getDatabase())) {
                databaseCombox.setSelectedIndex(i);
                break;
            }
        }
        bag.gridx = 1;
        bag.weightx = 0.5;
        rootComponent.add(databaseCombox, bag);
        bag.gridy = 1;
        bag.gridx = 0;
        bag.weighty = 1;
        addMapperAnnotationCheckBox = new JCheckBox("add @Mapper to mybatis interface", profile.getAddMapperAnnotation());
        rootComponent.add(addMapperAnnotationCheckBox, bag);

        bag.gridy = 2;
        bag.gridx = 0;
        useGeneratedKeysCheckBox = new JCheckBox("use generated keys", profile.getUseGeneratedKeys());
        rootComponent.add(useGeneratedKeysCheckBox, bag);

        bag.gridy = 3;
        bag.gridx = 0;
        bag.weighty = 10000;
        rootComponent.add(new JPanel(), bag);
    }

    public JPanel getRootComponent() {
        return rootComponent;
    }

    public void importFrom(PluginState setting) {
        this.settings = setting;
        setData(setting.getDefaultProfile());
    }

    public void setData(Profile data) {
        databaseCombox.setSelectedItem(data.getDatabase());
        addMapperAnnotationCheckBox.setSelected(data.getAddMapperAnnotation());
    }

    public boolean isSettingModified(PluginState state) {
        getData(settings.getDefaultProfile());
        return !settings.equals(state);
    }

    public PluginState getSettings() {
        getData(settings.getDefaultProfile());
        return settings;
    }

    private void getData(Profile defaultProfile) {
        defaultProfile.setAddMapperAnnotation(addMapperAnnotationCheckBox.isSelected());
        defaultProfile.setDatabase((String) databaseCombox.getSelectedItem());
        defaultProfile.setUseGeneratedKeys(useGeneratedKeysCheckBox.isSelected());
    }
}
