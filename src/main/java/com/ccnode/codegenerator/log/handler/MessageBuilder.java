package com.ccnode.codegenerator.log.handler;

import com.ccnode.codegenerator.common.VersionManager;
import com.intellij.openapi.application.ApplicationInfo;
import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class MessageBuilder {
    static String buildMessage(String message, Exception e) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(message)) {
            builder.append("the message is empty");
        } else {
            builder.append("the message is " + message);
        }
        if (e != null) {
            builder.append(" the exception cause:");
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter( writer );
            e.printStackTrace( printWriter );
            printWriter.flush();
            builder.append(writer.toString());
        }
        builder.append(" the system os is:" + System.getProperty("os.name"));
        builder.append(" the intellij version is:" + ApplicationInfo.getInstance().getBuild().asString());
        builder.append(" the currentVersion are:" + VersionManager.getCurrentVersion());
        return builder.toString();
    }
}
