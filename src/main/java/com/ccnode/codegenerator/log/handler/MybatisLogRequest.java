package com.ccnode.codegenerator.log.handler;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class MybatisLogRequest {
    private String className;
    private String messages;
    private String loggerLevel;

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
