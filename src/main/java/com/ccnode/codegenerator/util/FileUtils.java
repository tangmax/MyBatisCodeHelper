package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.dialog.InsertFileProp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/2
 * @Description
 */
public class FileUtils {

    public static void writeFiles(InsertFileProp prop, List<String> retList) {
        try {
            File file = new File(prop.getFullPath());
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            Files.write(Paths.get(prop.getFullPath()), retList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("can't write file " + prop.getName() + " to path " + prop.getFullPath(), e);
        }
    }


    public static void writeFiles(String  fullPath, List<String> retList,String fileName) {
        try {
            File file = new File(fullPath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            Files.write(Paths.get(fullPath), retList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("can't write file " + fileName + " to path " + fullPath, e);
        }
    }
}
