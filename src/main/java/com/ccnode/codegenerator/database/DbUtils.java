package com.ccnode.codegenerator.database;

import com.ccnode.codegenerator.methodnameparser.KeyWordConstants;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.FetchProp;

import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/3/6
 * @Description
 */
public class DbUtils {
    public static String buildSelectFunctionVal(FetchProp prop) {
        return prop.getFetchFunction() + prop.getFetchProp();
    }

    public static String getReturnClassFromFunction(Map<String, String> fieldMap, String fetchFunction, String fetchProp) {
        switch (fetchFunction) {
            case KeyWordConstants.MAX:
            case KeyWordConstants.MIN: {
                return fieldMap.get(fetchProp);
            }
            case KeyWordConstants.AVG:
            case KeyWordConstants.SUM: {
                return "java.math.BigDecimal";
            }
        }
        return null;
    }
}
