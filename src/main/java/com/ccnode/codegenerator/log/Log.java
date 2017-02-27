package com.ccnode.codegenerator.log;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public interface Log {
    String getName();

    void info(Exception e);

    void error(Exception e);

    void info(String message);

    void info(String message,Exception e);

    void error(String message,Exception e);

    void error(String message);
}
