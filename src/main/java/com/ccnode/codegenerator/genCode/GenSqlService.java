package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.database.DataBaseHandlerFactory;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 20:12
 */
public class GenSqlService {

    public static void generateSqlFile(InsertFileProp prop, List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        String sql = DataBaseHandlerFactory.currentHandler().generateSql(propList, primaryKey, tableName);
        List<String> retList = Lists.newArrayList(sql);
        try {
            String filePath = prop.getFolderPath() + "/" + prop.getName();
            Files.write(Paths.get(filePath), retList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("can't write file " + prop.getName() + " to path " + prop.getFolderPath() + "/" + prop.getName(), e);
        }
        //then go write to the file.
    }
}


