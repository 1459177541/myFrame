package log.impl;

import log.appander.Appender;
import log.layout.Layout;
import log.log.LoggerLevel;

public class LoggerImpl extends AbstractLogger {

    private Appender appender;

    private Layout layout;

    public Appender getAppender() {
        return appender;
    }

    public void setAppender(Appender appender) {
        this.appender = appender;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @Override
    void out(LoggerLevel level, String text, Object[] objects) {
        appender.out(level, clazz, layout.layout(text, objects));
    }

}
