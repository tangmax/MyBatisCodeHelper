package com.ccnode.codegenerator.myconfigurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @Author bruce.ge
 * @Date 2017/2/20
 * @Description
 */
public class MyConfigurable implements Configurable {

    public MyBatisCodeHelperApplicationComponent applicationComponent = MyBatisCodeHelperApplicationComponent.getInstance();

    private SettingDialog form;

    @Nls
    @Override
    public String getDisplayName() {
        return "MyBatisCodeHelper";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (form == null) {
            form = new SettingDialog(applicationComponent.getState().clone());
        }
        return form.getRootComponent();
    }

    @Override
    public boolean isModified() {
        return form!=null && form.isSettingModified(applicationComponent.getState());
    }

    @Override
    public void apply() throws ConfigurationException {
        PluginState formSettings = form.getSettings();
        applicationComponent.loadState(formSettings);
    }

    @Override
    public void reset() {
        if (form != null) {
            form.importFrom(applicationComponent.getState().clone());
        }
    }

    @Override
    public void disposeUIResources() {
        form = null;
    }
}
