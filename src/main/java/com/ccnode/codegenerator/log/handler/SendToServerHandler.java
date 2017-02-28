package com.ccnode.codegenerator.log.handler;

import com.ccnode.codegenerator.log.LogEvent;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class SendToServerHandler extends LoggerHandler {

    public SendToServerHandler(LoggerHandler next) {
        super(next);
    }

    @Override
    public void handleRequest(LogEvent req) {
        sendLogToServer(req);
        super.handleRequest(req);
    }

    public void sendLogToServer(LogEvent req) {
        MybatisLogRequest request = new MybatisLogRequest();
        request.setClassName(req.getName());
        request.setLoggerLevel(req.getLevel().name());
        request.setMessages(MessageBuilder.buildMessage(req.getMessage(), req.getE()));
        HttpClient.sendDataToLog(request);
    }
}
