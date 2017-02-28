package com.ccnode.codegenerator.log.handler;

import com.ccnode.codegenerator.log.LogEvent;
import com.ccnode.codegenerator.log.LoggerLevel;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class ErrorHandler extends LoggerHandler {


    public ErrorHandler(LoggerHandler next) {
        super(next);
    }

    @Override
    public void handleRequest(LogEvent req) {
        if(req.getLevel()== LoggerLevel.ERROR){
        }
        super.handleRequest(req);
    }
}
