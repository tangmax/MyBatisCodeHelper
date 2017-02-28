package com.ccnode.codegenerator.database;

import com.ccnode.codegenerator.database.handler.DatabaseFactory;
import com.ccnode.codegenerator.database.handler.mysql.MysqlDatabaseFactory;
import com.ccnode.codegenerator.database.handler.oracle.OracleDatabaseFactory;
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
public class DatabaseComponenent {
    private static Map<String, DatabaseFactory> databaseHandlerMap = new HashMap<String, DatabaseFactory>() {{
        put(DataBaseConstants.MYSQL, new MysqlDatabaseFactory());
        put(DataBaseConstants.ORACLE, new OracleDatabaseFactory());
    }};

    private static MyBatisCodeHelperApplicationComponent myBatisCodeHelperApplicationComponent = MyBatisCodeHelperApplicationComponent.getInstance();

    @NotNull
    public static DatabaseFactory currentHandler() {
        String database = myBatisCodeHelperApplicationComponent.getState().getDefaultProfile().getDatabase();
        return databaseHandlerMap.get(database);
    }


    public static String currentDatabase() {
        return myBatisCodeHelperApplicationComponent.getState().getDefaultProfile().getDatabase();
    }


    public static String formatColumn(String column) {
        if (currentDatabase().equals(DataBaseConstants.MYSQL)) {
            return "`" + column + "`";
        } else {
            return column;
        }
    }
}
