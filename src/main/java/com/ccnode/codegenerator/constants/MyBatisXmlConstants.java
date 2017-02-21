package com.ccnode.codegenerator.constants;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author bruce.ge
 * @Date 2017/2/18
 * @Description
 */
public class MyBatisXmlConstants {


    public static final String RESULTMAP = "resultMap";


    public static final String RESULT_TYPE = "resultType";

    public static final String SELECT = "select";

    public static final String INSERT = "insert";

    public static final String UPDATE = "update";

    public static final String DELETE = "delete";

    public static final String SQL = "sql";

    public static final String REFID = "refid";

    public static final String INCLUDE = "include";

    public static final String ID = "id";

    public static final String IF = "if";


    public static final String PROPERTY = "property";

    public static final String COLUMN = "column";

    public static final String RESULT = "result";

    public static final String TEST = "test";

    public static final String TYPE = "type";

    public static final String COLLECTION = "collection";


    public static final String NAMESPACE = "namespace";
    public static final String MAPPER = "mapper";

    public static Set<String> mapperMethodSet = new HashSet<String>() {{
        add(INSERT);
        add(UPDATE);
        add(DELETE);
        add(INSERT);
    }};
}
