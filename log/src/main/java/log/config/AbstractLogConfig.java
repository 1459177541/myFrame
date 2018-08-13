package log.config;

import log.appender.Appender;
import log.appender.AppenderMethod;
import log.log.LoggerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractLogConfig implements LogConfig {

    protected Map<Class, Map<LoggerLevel, AppenderMethod>> appenderMap;

    protected Map<LoggerLevel, AppenderMethod> defaultAppender;

    @Override
    public Appender getAppender(Class clazz, LoggerLevel level) {
        AppenderMethod appenderMethod = null;
        if (Objects.requireNonNull(appenderMap).containsKey(clazz)){
            appenderMethod = appenderMap.get(clazz).get(level);
        }
        if(null == appenderMethod){
            appenderMethod = Objects.requireNonNull(defaultAppender).get(level);
        }
        return Objects.requireNonNull(appenderMethod, "未找到配置").getAppender();
    }

    public LogConfig addConfig(Class clazz, LoggerLevel level, AppenderMethod appenderMethod){
        Map<LoggerLevel, AppenderMethod> classMap = appenderMap.computeIfAbsent(clazz, k -> new HashMap<>(6));
        classMap.put(level, appenderMethod);
        return this;
    }

}
