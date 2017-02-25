package com.ccnode.codegenerator.database;

import com.ccnode.codegenerator.database.handler.DatabaseHandler;
import com.ccnode.codegenerator.database.handler.mysql.MysqlDatabaseHandler;
import com.ccnode.codegenerator.myconfigurable.DataBaseConstants;
import com.ccnode.codegenerator.myconfigurable.MyBatisCodeHelperApplicationComponent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/23
 * @Description
 */
public class DataBaseHandlerFactory {
    private static Map<String, DatabaseHandler> databaseHandlerMap = new HashMap<String, DatabaseHandler>() {{
        put(DataBaseConstants.MYSQL, new MysqlDatabaseHandler());
    }};

    private static MyBatisCodeHelperApplicationComponent myBatisCodeHelperApplicationComponent = MyBatisCodeHelperApplicationComponent.getInstance();

    @NotNull
    public static DatabaseHandler currentHandler() {
        String database = myBatisCodeHelperApplicationComponent.getState().getDefaultProfile().getDatabase();
        return databaseHandlerMap.get(database);
    }
}
