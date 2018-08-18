package log.config;


import log.appender.Appender;
import log.appender.AppenderMethod;
import log.appender.impl.FileStoreAppender;
import log.log.LoggerLevel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LogDefinition {

    private Class clazz;

    private Map<LoggerLevel, String> formatMap = new HashMap<>(LoggerLevel.LENGTH);

    private Map<LoggerLevel, Appender> appenderMap = new HashMap<>(LoggerLevel.LENGTH);


    public Class getClazz() {
        return clazz;
    }

    LogDefinition setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    LogDefinition addFormat(LoggerLevel level, String formatString){
        formatMap.put(level, formatString);
        return this;
    }

    LogDefinition addAppender(LoggerLevel level, AppenderMethod appenderMethod){
        appenderMap.put(level, appenderMethod.getAppender());
        return this;
    }

    LogDefinition addFile(LoggerLevel level, String fileName){
        FileStoreAppender appender = (FileStoreAppender) AppenderMethod.FILE_STORT.getAppender();
        appender.setFile(new File(fileName));
        appenderMap.put(level, appender);
        return this;
    }

    String getFormat(LoggerLevel level){
        return get(level, formatMap);
    }

    Appender getAppender(LoggerLevel level){
        return get(level, appenderMap);
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
