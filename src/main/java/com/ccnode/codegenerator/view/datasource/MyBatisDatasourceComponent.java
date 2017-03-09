package com.ccnode.codegenerator.view.datasource;

import com.intellij.openapi.components.ProjectComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class MyBatisDatasourceComponent implements ProjectComponent{
    @Override
    public void projectOpened() {
        //do some thing to make view.
    }

    @Override
    public void projectClosed() {

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
        return null;
    }
}
