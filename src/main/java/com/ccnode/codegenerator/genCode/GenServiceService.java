package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.freemarker.TemplateConstants;
import com.ccnode.codegenerator.freemarker.TemplateUtil;
import com.ccnode.codegenerator.pojo.ClassInfo;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/28 21:14
 */
public class GenServiceService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(GenServiceService.class);

    public static void generateServiceUsingFtl(InsertFileProp fileProp, ClassInfo srcClass, InsertFileProp daoProp) {
        String daoName = GenCodeUtil.getLowerCamel(daoProp.getName());
        List<String> retList = Lists.newArrayList();
        Map<String, Object> root = Maps.newHashMap();
        root.put(TemplateConstants.SERVICE_PACKAGE, fileProp.getPackageName());
        root.put(TemplateConstants.POJO_FULL_TYPE, srcClass.getQualifiedName());
        root.put(TemplateConstants.DAO_FULLTYPE, daoProp.getQutifiedName());
        root.put(TemplateConstants.SERVICE_TYPE, fileProp.getName());
        root.put(TemplateConstants.DAO_TYPE, daoProp.getName());
        root.put(TemplateConstants.DAO_NAME, GenCodeUtil.getLowerCamel(daoProp.getName()));
        root.put(TemplateConstants.POJO_TYPE, srcClass.getName());
        String generateServiceString = TemplateUtil.processToString(TemplateConstants.GENCODE_SERVICE, root);
        retList.add(generateServiceString);
        try {
            String filePath = fileProp.getFullPath();
            Files.write(Paths.get(filePath), retList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("can't write file " + fileProp.getName() + " to path " + fileProp.getFullPath(),e);
        }
    }
}
