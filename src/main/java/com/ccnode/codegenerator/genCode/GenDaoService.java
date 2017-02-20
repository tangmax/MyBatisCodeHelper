package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.freemarker.TemplateConstants;
import com.ccnode.codegenerator.freemarker.TemplateUtil;
import com.ccnode.codegenerator.myconfigurable.MyBatisCodeHelperApplicationComponent;
import com.ccnode.codegenerator.pojo.ClassInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
public class GenDaoService {


    public static void generateDaoFileUsingFtl(InsertFileProp daoProp, ClassInfo srcClass) {
        Map<String, Object> root = Maps.newHashMap();
        root.put(TemplateConstants.DAO_PACKAGE_NAME,daoProp.getPackageName());
        root.put(TemplateConstants.POJO_FULL_TYPE,srcClass.getQualifiedName());
        root.put(TemplateConstants.DAO_TYPE,daoProp.getName());
        root.put(TemplateConstants.POJO_TYPE,srcClass.getName());
        root.put(TemplateConstants.ADD_MAPPER_ANNOTATION, MyBatisCodeHelperApplicationComponent.getInstance().getState().getProfile().getAddMapperAnnotation());
        String genDaoString = TemplateUtil.processToString(TemplateConstants.GENCODE_DAO, root);
        List<String> lines = Lists.newArrayList();
        lines.add(genDaoString);
        try {
            String filePath = daoProp.getFullPath();
            Files.write(Paths.get(filePath), lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("can't write file " + daoProp.getName() + " to path " + daoProp.getFullPath(),e);
        }
    }
}
