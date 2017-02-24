package com.ccnode.codegenerator.myconfigurable;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Author bruce.ge
 * @Date 2017/2/20
 * @Description
 */
@State(name = "MyBatisCodeHelper", storages = {@Storage(id = "MyBatisCodeHelper", file = "$APP_CONFIG$/MyBatisCodeHelper.xml")})
public class MyBatisCodeHelperApplicationComponent implements ApplicationComponent, PersistentStateComponent<PluginState> {


    private PluginState settings;


    public MyBatisCodeHelperApplicationComponent() {

    }


    public static MyBatisCodeHelperApplicationComponent getInstance() {
        return ApplicationManager.getApplication().getComponent(MyBatisCodeHelperApplicationComponent.class);
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "MyBatisCodeHelper";
    }

    @Override
    public PluginState getState() {
        if (settings == null) {
            settings = new PluginState();
            settings.setProfile(DefaultState.createDefault());
        }
        return settings;
    }

    @Override
    public void loadState(PluginState state) {
        this.settings = state;
    }
}
