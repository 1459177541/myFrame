package log.impl;

import log.layout.Layout;
import log.config.LogConfig;
import log.log.LoggerLevel;

import java.util.Arrays;
import java.util.Optional;

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
        String t = text;
        Object[] arg;
        if (null != config.getFormat(clazz, level)){
            if (null == objects){
                arg = new Object[]{layout.layout(text, level, null)};
            }else {
                arg = Arrays.copyOf(objects, objects.length + 1);
                arg[objects.length] = layout.layout(text, level, objects);
            }
            t = config.getFormat(clazz, level).replaceAll("text",Optional.ofNullable(objects).map(obj->obj.length).orElse(0).toString());
        }else {
            arg = objects;
        }
        config.getAppender(clazz, level).out(layout.layout(t, level, arg));
    }

}
