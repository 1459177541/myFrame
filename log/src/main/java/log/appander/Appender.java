package log.appander;

import log.log.LoggerLevel;

public interface Appender {

    void out(LoggerLevel level, Class clazz, String text);

}
