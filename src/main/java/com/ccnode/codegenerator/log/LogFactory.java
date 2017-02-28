package com.ccnode.codegenerator.log;

import com.ccnode.codegenerator.log.handler.ErrorHandler;
import com.ccnode.codegenerator.log.handler.InfoHandler;
import com.ccnode.codegenerator.log.handler.LoggerHandler;
import com.ccnode.codegenerator.log.handler.SendToServerHandler;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class LogFactory {
    private static LoggerHandler chain = new InfoHandler(new ErrorHandler(new SendToServerHandler(null)));

    public static Log getLogger(Class aClass) {
        return new LogImpl(aClass.getName(), chain);
    }

    public static Log getLogger(String name) {
        return new LogImpl(name, chain);
    }
}
