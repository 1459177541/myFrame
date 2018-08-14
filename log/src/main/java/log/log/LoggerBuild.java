package log.log;

import log.config.LogConfig;
import log.impl.LoggerImpl;
import log.layout.Layout;
import log.layout.LayoutImp;
import util.Build;

import java.util.Objects;

public class LoggerBuild implements Build<Logger> {

    private LogConfig config;

    private Layout layout;

    private Class clazz;

    public LogConfig getConfig() {
        return config;
    }

    public LoggerBuild setConfig(LogConfig config) {
        this.config = config;
        return this;
    }

    public Layout getLayout() {
        return layout;
    }

    public LoggerBuild setLayout(Layout layout) {
        this.layout = layout;
        return this;
    }

    public Class getClazz() {
        return clazz;
    }

    public LoggerBuild setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public Logger build() {
        LoggerImpl logger = new LoggerImpl();
        logger.setClazz(Objects.requireNonNull(clazz));
        logger.setLayout(Objects.requireNonNullElseGet(layout,LayoutImp::new));
        logger.setConfig(Objects.requireNonNull(config));
        return logger;
    }

}
