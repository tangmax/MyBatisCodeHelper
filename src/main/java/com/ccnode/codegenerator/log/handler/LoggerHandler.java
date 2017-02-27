package com.ccnode.codegenerator.log.handler;

import com.ccnode.codegenerator.log.LogEvent;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public abstract class LoggerHandler {
    private LoggerHandler next;

    public LoggerHandler(LoggerHandler next) {
        this.next = next;
    }

    public void handleRequest(LogEvent req) {
        if (next != null) {
            next.handleRequest(req);
        }
    }
}
