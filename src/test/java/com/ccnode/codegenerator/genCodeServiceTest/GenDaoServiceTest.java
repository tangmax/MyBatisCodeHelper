package com.ccnode.codegenerator.genCodeServiceTest;

import com.ccnode.codegenerator.dialog.InsertFileProp;
import com.ccnode.codegenerator.genCode.GenDaoService;
import com.ccnode.codegenerator.pojo.ClassInfo;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @Author bruce.ge
 * @Date 2017/2/18
 * @Description
 */
public class GenDaoServiceTest {
    @Test
    public void testGenDaoUsingFtl() {
        InsertFileProp prop = new InsertFileProp();
        prop.setQutifiedName("com.dao.CarDao");
        prop.setName("CarDao");
        prop.setFolderPath("dao/hello/");
        prop.setPackageName("com.dao");
        String fullPath = "dao/hello/";
        prop.setFullPath(fullPath);
        ClassInfo info = new ClassInfo();
        info.setName("Car");
        info.setQualifiedName("com.domain.Car");
        GenDaoService.generateDaoFileUsingFtl(prop, info);
        File file = new File(fullPath);
        Assert.assertTrue(file.exists());
        file.delete();
    }
}
