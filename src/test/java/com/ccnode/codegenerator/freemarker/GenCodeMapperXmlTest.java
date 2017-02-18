package com.ccnode.codegenerator.freemarker;

import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndField;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/2/15
 * @Description
 */
public class GenCodeMapperXmlTest {
    @Test
    public void testGenerateMapperXml() {
        Map<String, Object> rootMap = Maps.newHashMap();
        rootMap.put("mybatisInterfaceFullName", "com.codehelper.mapper.CarDao");
        rootMap.put("domainClassFullName", "com.codehelper.domain.Car");
        List<ColumnAndField> columnAndFieldList = Lists.newArrayList();
        columnAndFieldList.add(new ColumnAndField("car_id", "carId"));
        columnAndFieldList.add(new ColumnAndField("price", "price"));
        columnAndFieldList.add(new ColumnAndField("length", "length"));
        columnAndFieldList.add(new ColumnAndField("width", "width"));
        columnAndFieldList.add(new ColumnAndField("nimei", "nimei"));
        columnAndFieldList.add(new ColumnAndField("bbbbb", "bbbbb"));
        columnAndFieldList.add(new ColumnAndField("ccccc", "ccccc"));
        rootMap.put("fieldAndColumns", columnAndFieldList);
        rootMap.put("tableName", "car");
        rootMap.put("primaryField", "carId");
        rootMap.put("primaryColumn", "car_id");
        String s = TemplateUtil.processToString("gencode/mapperxml.ftl", rootMap);
        System.out.println(s);
    }

}
