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
        Appender appenderMethod = null;
        if (Objects.requireNonNull(classLogDefinitionMap).containsKey(clazz)){
            appenderMethod = classLogDefinitionMap.get(clazz).getAppender(level);
        }
        if(null == appenderMethod){
            appenderMethod = defaultLogDefinition.getAppender(level);
        }
        return Objects.requireNonNull(appenderMethod, "未找到配置");
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

    public LogConfig addConfigByFileStore(Class clazz, LoggerLevel level, String fileName){
        LogDefinition logDefinition = classLogDefinitionMap.computeIfAbsent(clazz, c->new LogDefinition().setClazz(c));
        logDefinition.addFile(level, fileName);
        return this;
    }

    public LogConfig addConfigByFileStore(Class clazz, LoggerLevel level, String fileName, String format){
        LogDefinition logDefinition = classLogDefinitionMap.computeIfAbsent(clazz, c->new LogDefinition().setClazz(c));
        logDefinition.addFormat(level, format);
        logDefinition.addFile(level, format);
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

    public LogConfig addDefaultConfigByConfig(LoggerLevel level, String fileName){
        defaultLogDefinition.addFile(level, fileName);
        return this;
    }

    public LogConfig addDefaultConfigByFileStore(LoggerLevel level, String fileName, String format){
        defaultLogDefinition.addFormat(level, format);
        defaultLogDefinition.addFile(level, fileName);
        return this;
    }

    public abstract void init();

}
