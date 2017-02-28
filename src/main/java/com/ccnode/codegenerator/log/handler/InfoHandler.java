package com.ccnode.codegenerator.log.handler;

import com.ccnode.codegenerator.log.LogEvent;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class InfoHandler extends LoggerHandler {
    public InfoHandler(LoggerHandler next) {
        super(next);
    }

    @Override
    public void handleRequest(LogEvent req) {
        super.handleRequest(req);
    }
}
