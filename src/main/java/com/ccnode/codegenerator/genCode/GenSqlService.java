package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.database.DatabaseComponenent;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.util.FileUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 20:12
 */
public class GenSqlService {

    public static void generateSqlFile(InsertFileProp prop, List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        String sql = DatabaseComponenent.currentHandler().getGenerateFileHandler().generateSql(propList, primaryKey, tableName);
        List<String> retList = Lists.newArrayList(sql);
        FileUtils.writeFiles(prop, retList);
        //then go write to the file.
    }

}


