package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.database.GenClassInfo;
import com.ccnode.codegenerator.freemarker.TemplateConstants;
import com.ccnode.codegenerator.freemarker.TemplateUtil;
import com.ccnode.codegenerator.util.FileUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/3/7
 * @Description
 */
public class GenClassService {
    public static void generateClassFileUsingFtl(GenClassInfo classInfo) {
        Map<String, Object> root = Maps.newHashMap();
        root.put(TemplateConstants.CLASS_NAME, classInfo.getClassName());
        root.put(TemplateConstants.CLASS_PACKAGE, classInfo.getClassPackageName());
        root.put(TemplateConstants.FIELDS, classInfo.getClassFieldInfos());
        root.put(TemplateConstants.IMPORT_LIST, classInfo.getImportList());
        String genClassString = TemplateUtil.processToString(TemplateConstants.GEN_CLASS, root);
        List<String> lines = Lists.newArrayList();
        lines.add(genClassString);
        FileUtils.writeFiles(classInfo.getClassFullPath(), lines, classInfo.getClassName());
    }
}
