package log.layout;

import log.log.LoggerLevel;

public interface Layout {

    String layout(String text, LoggerLevel level, Object[] objects);

}
