package com.ccnode.codegenerator.log.handler;

import org.apache.commons.lang.StringUtils;

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
            Throwable throwable = e;
            while (throwable != null) {
                builder.append(throwable.getMessage() + "\n");
                throwable = throwable.getCause();
            }
        }
        return builder.toString();
    }
}
