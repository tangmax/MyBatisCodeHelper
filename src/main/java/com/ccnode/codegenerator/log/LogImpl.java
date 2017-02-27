package com.ccnode.codegenerator.log;

import com.ccnode.codegenerator.log.handler.LoggerHandler;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class LogImpl implements Log {

    private String name;

    private LoggerHandler chain;

    public LogImpl(String name,LoggerHandler chain) {
        this.name = name;
        this.chain = chain;
    }



    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void info(Exception e) {
        LogEvent event = new LogEvent();
        event.setName(name);
        event.setE(e);
        event.setLevel(LoggerLevel.INFO);
        chain.handleRequest(event);
    }

    @Override
    public void error(Exception e) {
        LogEvent event = new LogEvent();
        event.setName(name);
        event.setE(e);
        event.setLevel(LoggerLevel.ERROR);
        chain.handleRequest(event);
    }

    @Override
    public void info(String message) {
        LogEvent event = new LogEvent();
        event.setName(name);
        event.setMessage(message);
        event.setLevel(LoggerLevel.INFO);
        chain.handleRequest(event);
    }

    @Override
    public void info(String message, Exception e) {
        LogEvent event = new LogEvent();
        event.setName(name);
        event.setMessage(message);
        event.setE(e);
        event.setLevel(LoggerLevel.INFO);
        chain.handleRequest(event);
    }

    @Override
    public void error(String message, Exception e) {
        LogEvent event = new LogEvent();
        event.setMessage(message);
        event.setName(name);
        event.setE(e);
        event.setLevel(LoggerLevel.ERROR);
        chain.handleRequest(event);
    }

    @Override
    public void error(String message) {
        LogEvent event = new LogEvent();
        event.setName(name);
        event.setMessage(message);
        event.setLevel(LoggerLevel.ERROR);
        chain.handleRequest(event);
    }
}
