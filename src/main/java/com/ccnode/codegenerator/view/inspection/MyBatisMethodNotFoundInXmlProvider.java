package com.ccnode.codegenerator.view.inspection;

import com.intellij.codeInspection.InspectionToolProvider;

/**
 * @Author bruce.ge
 * @Date 2017/3/22
 * @Description
 */
public class MyBatisMethodNotFoundInXmlProvider implements InspectionToolProvider {
    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{MybatisMethodNotFindInXmlInspection.class};
    }
}
