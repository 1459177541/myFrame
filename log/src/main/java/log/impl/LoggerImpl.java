package log.impl;

import log.layout.Layout;
import log.config.LogConfig;
import log.log.LoggerLevel;

public class LoggerImpl extends AbstractLogger {

    private LogConfig config;

    public LogConfig getConfig() {
        return config;
    }

    public void setConfig(LogConfig config) {
        this.config = config;
    }

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @Override
    void out(LoggerLevel level, String text, Object[] objects) {
        config.getAppender(clazz, level).out(layout.layout(text, objects));
    }

}
