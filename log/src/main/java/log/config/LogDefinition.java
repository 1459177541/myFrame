package log.config;

import log.appender.AppenderMethod;
import log.log.LoggerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LogDefinition {

    private Class clazz;

    private Map<LoggerLevel, String> formatMap = new HashMap<>(LoggerLevel.LENGTH);

    private Map<LoggerLevel, AppenderMethod> appenderMap = new HashMap<>(LoggerLevel.LENGTH);

    public Class getClazz() {
        return clazz;
    }

    public LogDefinition setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public LogDefinition addFormat(LoggerLevel level, String formatString){
        formatMap.put(level, formatString);
        return this;
    }

    public LogDefinition addAppender(LoggerLevel level, AppenderMethod appenderMethod){
        appenderMap.put(level, appenderMethod);
        return this;
    }

    public String getFormat(LoggerLevel level){
        return get(level, formatMap);
    }

    public AppenderMethod getAppender(LoggerLevel level){
        return Objects.requireNonNull(get(level, appenderMap),"未找到配置");
    }

    private <T> T get(LoggerLevel level, Map<LoggerLevel, T> map){
        switch (level){
            case TRACE:
                if (map.containsKey(LoggerLevel.TRACE)){
                    return map.get(LoggerLevel.TRACE);
                }
            case DEBUG:
                if (map.containsKey(LoggerLevel.DEBUG)){
                    return map.get(LoggerLevel.DEBUG);
                }
            case INFO:
                if (map.containsKey(LoggerLevel.INFO)){
                    return map.get(LoggerLevel.INFO);
                }
            case WARN:
                if (map.containsKey(LoggerLevel.WARN)){
                    return map.get(LoggerLevel.WARN);
                }
            case ERROR:
                if (map.containsKey(LoggerLevel.ERROR)){
                    return map.get(LoggerLevel.ERROR);
                }
            case FATAL:
                if (map.containsKey(LoggerLevel.FATAL)){
                    return map.get(LoggerLevel.FATAL);
                }
        }
        return null;
    }

}
