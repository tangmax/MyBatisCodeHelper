package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.freemarker.TemplateConstants;
import com.ccnode.codegenerator.freemarker.TemplateUtil;
import com.ccnode.codegenerator.pojo.ClassInfo;
import com.ccnode.codegenerator.util.FileUtils;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/28 21:14
 */
public class GenServiceService {


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
        FileUtils.writeFiles(fileProp,retList);
    }
}
