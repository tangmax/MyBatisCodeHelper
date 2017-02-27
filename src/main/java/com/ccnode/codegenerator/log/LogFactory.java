package com.ccnode.codegenerator.log;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class LogFactory {
    public static Log getLogger(Class aClass) {
        return new LogImpl(aClass.getName());
    }
}
