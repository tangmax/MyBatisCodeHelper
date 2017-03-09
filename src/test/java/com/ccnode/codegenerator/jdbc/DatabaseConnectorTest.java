package com.ccnode.codegenerator.jdbc;

import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseInfo;
import com.ccnode.codegenerator.datasourceToolWindow.dbInfo.DatabaseConnector;
import com.ccnode.codegenerator.myconfigurable.DataBaseConstants;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author bruce.ge
 * @Date 2017/2/26
 * @Description
 */
public class DatabaseConnectorTest {
    @Test
    public void test() throws IOException {
        DatabaseInfo root = DatabaseConnector.getDataBaseInfoFromConnection(DataBaseConstants.MYSQL, "localhost:3306" ,"root", "","world");
        Gson gson = new Gson();
        String databaseJson = gson.toJson(root);
        List<String> lines = Lists.newArrayList();
        lines.add(databaseJson);
        Files.write(Paths.get("MyBatisCodeHelper_database.json"), lines, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
    }

    @Test
    public void testExetractBackJson() throws FileNotFoundException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Gson gson = new Gson();
        DatabaseInfo info = gson.fromJson((new InputStreamReader(new FileInputStream(new File("MyBatisCodeHelper_database.json")))), DatabaseInfo.class);
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(elapsed);
        System.out.println(info.getDatabaseName());
    }
}
