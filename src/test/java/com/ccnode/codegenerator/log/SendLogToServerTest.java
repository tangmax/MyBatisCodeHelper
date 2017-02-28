package com.ccnode.codegenerator.log;

import com.ccnode.codegenerator.log.handler.SendToServerHandler;
import org.junit.Test;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class SendLogToServerTest {
    @Test
    public void test() {
        SendToServerHandler handler = new SendToServerHandler(null);
        LogEvent req = new LogEvent();
        req.setName("hello nimeiya");
        req.setLevel(LoggerLevel.ERROR);
        req.setE(new RuntimeException("nimaya"));
        handler.sendLogToServer(req);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
