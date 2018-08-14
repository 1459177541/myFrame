package log.config;

import log.appender.Appender;
import log.appender.AppenderMethod;
import log.log.LoggerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractLogConfig implements LogConfig {

    protected Map<Class, LogDefinition> classLogDefinitionMap = new HashMap<>();

    protected LogDefinition defaultLogDefinition = new LogDefinition();

    {
        init();
    }

    @Override
    public Appender getAppender(Class clazz, LoggerLevel level) {
        AppenderMethod appenderMethod = null;
        if (Objects.requireNonNull(classLogDefinitionMap).containsKey(clazz)){
            appenderMethod = classLogDefinitionMap.get(clazz).getAppender(level);
        }
        if(null == appenderMethod){
            appenderMethod = defaultLogDefinition.getAppender(level);
        }
        return Objects.requireNonNull(appenderMethod, "未找到配置").getAppender();
    }

    @Override
    public String getFormat(Class clazz, LoggerLevel level) {
        String format = null;
        if (Objects.requireNonNull(classLogDefinitionMap).containsKey(clazz)){
            format = classLogDefinitionMap.get(clazz).getFormat(level);
        }
        if (null == format){
            format = defaultLogDefinition.getFormat(level);
        }
        return format;
    }

    public LogConfig addConfig(Class clazz, LoggerLevel level, AppenderMethod appenderMethod){
        LogDefinition logDefinition = classLogDefinitionMap.computeIfAbsent(clazz, c->new LogDefinition().setClazz(c));
        logDefinition.addAppender(level, appenderMethod);
        return this;
    }

    public LogConfig addConfig(Class clazz, LoggerLevel level, String format){
        LogDefinition logDefinition = classLogDefinitionMap.computeIfAbsent(clazz, c->new LogDefinition().setClazz(c));
        logDefinition.addFormat(level, format);
        return this;
    }

    public LogConfig addConfig(Class clazz, LoggerLevel level, AppenderMethod appenderMethod, String format){
        LogDefinition logDefinition = classLogDefinitionMap.computeIfAbsent(clazz, c->new LogDefinition().setClazz(c));
        logDefinition.addAppender(level, appenderMethod);
        logDefinition.addFormat(level, format);
        return this;
    }

    public LogConfig addDefaultConfig(LoggerLevel level, AppenderMethod appenderMethod){
        defaultLogDefinition.addAppender(level, appenderMethod);
        return this;
    }

    public LogConfig addDefaultConfig(LoggerLevel level, String format){
        defaultLogDefinition.addFormat(level, format);
        return this;
    }

    public LogConfig addDefaultConfig(LoggerLevel level, AppenderMethod appenderMethod, String format){
        defaultLogDefinition.addAppender(level, appenderMethod);
        defaultLogDefinition.addFormat(level, format);
        return this;
    }


    public abstract void init();

}
