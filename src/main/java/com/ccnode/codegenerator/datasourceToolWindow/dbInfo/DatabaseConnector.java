package com.ccnode.codegenerator.datasourceToolWindow.dbInfo;

import com.ccnode.codegenerator.myconfigurable.DataBaseConstants;
import com.google.common.collect.Lists;

import java.sql.*;
import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/26
 * @Description
 */
public class DatabaseConnector {
    public static DatabaseInfo getDataBaseInfoFromConnection(String databaseType,String url, String userName, String password,String database) {
        Connection conn = null;
        Statement stmt = null;
        DatabaseInfo databaseInfo = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to databaseType...");
            String realUrl = buildUrl(databaseType,url,database);
            conn = DriverManager.getConnection(realUrl, userName, password);
            DatabaseMetaData metaData = conn.getMetaData();
            String schemaName = database;
            databaseInfo = new DatabaseInfo();
            databaseInfo.setDatabaseName(schemaName);
            List<TableInfo> tableInfos = Lists.newArrayList();

            ResultSet tableTypes = metaData.getTableTypes();
            List<String> tableTypeList = Lists.newArrayList();
            while (tableTypes.next()) {
                tableTypeList.add(tableTypes.getString(1));
            }
            ResultSet tables = metaData.getTables(null, schemaName, "%", null);
            List<String> tableList = Lists.newArrayList();
            while (tables.next()) {
                tableList.add(tables.getString(3));
            }

            for (String table : tableList) {
                TableInfo info1 = new TableInfo();
                info1.setTableName(table);
                List<TableColumnInfo> tableColumnInfos = Lists.newArrayList();
                ResultSet columns = metaData.getColumns(null, schemaName, table, "%");
                while (columns.next()) {
                    String columnName = columns.getString(4);
                    TableColumnInfo columnInfo = new TableColumnInfo();
                    String columnType = columns.getString(6);
                    columnInfo.setFieldName(columnName);
                    columnInfo.setFieldType(columnType);
                    tableColumnInfos.add(columnInfo);

                }
                info1.setTableColumnInfos(tableColumnInfos);
                tableInfos.add(info1);
            }
            databaseInfo.setTableInfoList(tableInfos);
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            //STEP 6: Clean-up environment
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        return databaseInfo;
    }

    private static String exatractScheme(String url) {
        return "world";
    }

    public static boolean checkConnection(String databaseType,String url, String userName, String password,String database) {
        String realUrl = buildUrl(databaseType,url,database);
        //display connect message.
        Connection conn = null;
        Statement stmt = null;
        DatabaseInfo databaseInfo = null;
        try {
            //STEP 2: Register JDBC driver
            //load different driver for different databaseType.
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to databaseType...");
            conn = DriverManager.getConnection(realUrl, userName, password);
        }catch (Exception e){
            return false;
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    private static String buildUrl(String databaseType, String text,String database) {
        if(databaseType.equals(DataBaseConstants.MYSQL)){
            //
            return "jdbc:mysql://"+text+"/"+database;
        } else{
            return "jdbc:mysql://"+text+"/"+database;
        }
    }
}
