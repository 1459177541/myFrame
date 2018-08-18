package log.config;

import log.appender.Appender;
import log.appender.AppenderMethod;
import log.log.LoggerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractLogConfig implements LogConfig {

    protected Map<Class, LogDefinition> classLogDefinitionMap = new HashMap<>();

    protected LogDefinition defaultLogDefinition = new LogDefinition();

    protected static LogConfig defaultLogConfig = null;

    {
        init();
    }

    public LogConfig toDefault(){
        defaultLogConfig = this;
        return this;
    }

    public static LogConfig getDefaultLogConfig(){
        return Objects.requireNonNull(defaultLogConfig, "未设置默认配置");
    }

    @Override
    public Appender getAppender(Class clazz, LoggerLevel level) {
        Appender appender = Optional.ofNullable(classLogDefinitionMap.get(clazz))
                .map(logDefinition -> logDefinition.getAppender(level))
                .orElse(defaultLogDefinition.getAppender(level));
        if (null == appender && defaultLogConfig != this){
            return defaultLogConfig.getAppender(clazz, level);
        }
        return Objects.requireNonNull(appender, "未找到配置");
    }

    @Override
    public String getFormat(Class clazz, LoggerLevel level) {
        String format = Optional.ofNullable(classLogDefinitionMap.get(clazz))
                .map(logDefinition -> logDefinition.getFormat(level))
                .orElse(defaultLogDefinition.getFormat(level));
        if (null == format && null != defaultLogConfig && defaultLogConfig != this){
            return defaultLogConfig.getFormat(clazz, level);
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
        LogDefinition logDefinition = classLogDefinitionMap.computeIfAbsent(clazz, c->new LogDefinition().setClazz(c))
                .addFormat(level, format)
                .addFile(level, fileName);
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
        defaultLogDefinition.addAppender(level, appenderMethod)
                .addFormat(level, format);
        return this;
    }

    public LogConfig addDefaultConfigByConfig(LoggerLevel level, String fileName){
        defaultLogDefinition.addFile(level, fileName);
        return this;
    }

    public LogConfig addDefaultConfigByFileStore(LoggerLevel level, String fileName, String format){
        defaultLogDefinition.addFormat(level, format)
                .addFile(level, fileName);
        return this;
    }

    public abstract void init();

}
