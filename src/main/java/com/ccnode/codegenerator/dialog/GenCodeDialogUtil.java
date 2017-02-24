package com.ccnode.codegenerator.dialog;

import com.ccnode.codegenerator.database.DataBaseHandlerFactory;
import com.ccnode.codegenerator.dialog.datatype.ClassFieldInfo;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/28.
 */
public class GenCodeDialogUtil {
    static Map<String, List<TypeProps>> extractMap(List<ClassFieldInfo> propFields) {
        Map<String, List<TypeProps>> fieldTypeMap = new HashMap<>();
        for (ClassFieldInfo info : propFields) {
            fieldTypeMap.put(info.getFieldName(), DataBaseHandlerFactory.currentHandler().getRecommendDatabaseTypeOfFieldType(info.getPsiField()));
        }
        return fieldTypeMap;
    }
}
