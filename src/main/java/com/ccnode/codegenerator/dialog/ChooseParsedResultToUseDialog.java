package com.ccnode.codegenerator.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/8
 * @Description
 */
public class ChooseParsedResultToUseDialog extends DialogWrapper{
    private List<String> xmlTagAndInfos;

    private Integer choosedIndex = 0;

    public Integer getChoosedIndex() {
        return choosedIndex;
    }

    public ChooseParsedResultToUseDialog(@Nullable Project project, List<String> tags) {
        super(project, true);
        this.xmlTagAndInfos = tags;
        setTitle("exist mutiple parse result");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel jpanel = new JPanel(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        bag.fill = GridBagConstraints.HORIZONTAL;
        bag.anchor = GridBagConstraints.WEST;
        bag.gridx = 0;
        bag.gridy = 0;
        bag.insets = new Insets(0, 0, 5, 0);
        jpanel.add(new JLabel("please choose one"), bag);
        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < xmlTagAndInfos.size(); i++) {
            bag.gridx = 0;
            bag.gridy = bag.gridy + 1;
            JTextArea comp = new JTextArea(xmlTagAndInfos.get(i));
            comp.setEditable(false);
            jpanel.add(comp, bag);
            bag.gridx = 1;
            JRadioButton jRadioButton = new JRadioButton("", true);
            jRadioButton.setActionCommand(String.valueOf(i));
            jRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String actionCommand = e.getActionCommand();
                    choosedIndex = Integer.parseInt(actionCommand);
                }
            });
            buttonGroup.add(jRadioButton);
            jpanel.add(jRadioButton, bag);
        }
        bag.fill = GridBagConstraints.HORIZONTAL;
        bag.gridy = 2;
        return jpanel;

    }
}
