package log.impl;

import log.log.Logger;
import log.log.LoggerLevel;

import java.util.Optional;

public abstract class AbstractLogger implements Logger {

    protected Class clazz;

    public AbstractLogger() {

    }

    public AbstractLogger(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public Logger setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public void trace(String text) {
        out(LoggerLevel.TRACE, text, null);
    }

    @Override
    public void trace(Object obj) {
        out(LoggerLevel.TRACE,Optional.ofNullable(obj).map(Object::toString).orElse("null"),null);
    }

    @Override
    public void trace(String text, Object... objects) {
        out(LoggerLevel.TRACE, text, objects);
    }

    @Override
    public void debug(String text) {
        out(LoggerLevel.DEBUG, text, null);
    }

    @Override
    public void debug(Object obj) {
        out(LoggerLevel.DEBUG,Optional.ofNullable(obj).map(Object::toString).orElse("null"),null);
    }

    @Override
    public void debug(String text, Object... objects) {
        out(LoggerLevel.DEBUG, text, objects);
    }

    @Override
    public void info(String text) {
        out(LoggerLevel.INFO, text, null);
    }

    @Override
    public void info(Object obj) {
       out(LoggerLevel.INFO,Optional.ofNullable(obj).map(Object::toString).orElse("null"),null);
    }

    @Override
    public void info(String text, Object... objects) {
        out(LoggerLevel.INFO, text, objects);
    }

    @Override
    public void warn(String text) {
        out(LoggerLevel.WARN, text, null);
    }

    @Override
    public void warn(Object obj) {
       out(LoggerLevel.WARN,Optional.ofNullable(obj).map(Object::toString).orElse("null"),null);
    }

    @Override
    public void warn(String text, Object... objects) {
        out(LoggerLevel.WARN, text, objects);
    }

    @Override
    public void error(String text) {
        out(LoggerLevel.ERROR, text, null);
    }

    @Override
    public void error(Object obj) {
        out(LoggerLevel.ERROR,Optional.ofNullable(obj).map(Object::toString).orElse("null"),null);
    }

    @Override
    public void error(String text, Object... objects) {
        out(LoggerLevel.ERROR, text, objects);
    }

    @Override
    public void fatal(String text) {
        out(LoggerLevel.FATAL, text, null);
    }

    @Override
    public void fatal(Object obj) {
        out(LoggerLevel.FATAL,Optional.ofNullable(obj).map(Object::toString).orElse("null"),null);
    }

    @Override
    public void fatal(String text, Object... objects) {
        out(LoggerLevel.FATAL, text, objects);
    }

    abstract void out(LoggerLevel level, String text, Object[] objects);

}
