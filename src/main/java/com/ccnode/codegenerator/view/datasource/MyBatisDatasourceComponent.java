package com.ccnode.codegenerator.view.datasource;

import com.ccnode.codegenerator.datasourceToolWindow.DatasourceState;
import com.ccnode.codegenerator.datasourceToolWindow.NewDatabaseInfo;
import com.ccnode.codegenerator.view.completion.MysqlCompleteCacheInteface;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
@State(name = "MyBatisCodeHelperDatasource", storages = {@Storage(id = "MyBatisCodeHelperDatasource", file = "MyBatisCodeHelperDatasource.xml")})
public class MyBatisDatasourceComponent implements ProjectComponent, PersistentStateComponent<DatasourceState> {


    private DatasourceState state;

    public MyBatisDatasourceComponent(){

    }

    @Override
    public void projectOpened() {

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
        return "MyBatisDataSourceComponent";
    }

    @Nullable
    @Override
    public DatasourceState getState() {
        if (state == null) {
            DatasourceState datasourceState = new DatasourceState();
            datasourceState.setDatabaseInfos(new ArrayList<>());
            state = datasourceState;
        }
        if(state.getDatabaseInfos()==null){
            state.setDatabaseInfos(new ArrayList<>());
        }
        return state;
    }

    @Override
    public void loadState(DatasourceState state) {
        this.state = state;
    }
}
