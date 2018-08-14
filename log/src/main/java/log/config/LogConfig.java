package log.config;

import log.appender.Appender;
import log.log.LoggerLevel;

public interface LogConfig {

    Appender getAppender(Class clazz, LoggerLevel level);

    String getFormat(Class clazz, LoggerLevel level);

}
