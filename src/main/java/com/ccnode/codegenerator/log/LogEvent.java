package com.ccnode.codegenerator.log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class LogEvent {
    private String message;

    private Exception e;

    private String name;

    private LoggerLevel level;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    @NotNull
    public LoggerLevel getLevel() {
        return level;
    }

    public void setLevel(LoggerLevel level) {
        this.level = level;
    }
}
