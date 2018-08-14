package logTest;

import log.appender.AppenderMethod;
import log.config.AbstractLogConfig;
import log.log.Logger;
import log.log.LoggerBuild;
import log.log.LoggerLevel;
import org.junit.jupiter.api.Test;

public class LogTest {
    private final Logger LOGGER = new LoggerBuild().setClazz(LogTest.class).setConfig(new AbstractLogConfig() {
        @Override
        public void init() {
            addConfig(LogTest.class, LoggerLevel.FATAL, AppenderMethod.CONSOLE_ERR);
            addConfig(LogTest.class, LoggerLevel.ERROR, AppenderMethod.CONSOLE_ERR);
            addConfig(LogTest.class, LoggerLevel.INFO, AppenderMethod.CONSOLE_OUT);
            addConfig(LogTest.class, LoggerLevel.DEBUG, AppenderMethod.CONSOLE_OUT
                    , "%s{year}-%s{month}-%s{day} %s{hour_of_day}:%s{minute}:%s{second} [%s{level}] - %s{text}");
        }
    }).build();

    @Test
    public void logTest(){
        LOGGER.info("info");
        LOGGER.info("%s{year}-%s{month}-%s{day} %s{hour_of_day}:%s{minute}:%s{second} [%s{level}] - %s{1} %s{0}", "World", "Hello");
        LOGGER.info("%s{module}.%s{class}.%s{method}:%s{line} [%s{thread}] - %s{0}", "Hello World");
        LOGGER.info("%s{class}.%s{method}:%s{line}");
        LOGGER.info(new StringBuilder("%s{class}.%s{method}:%s{line}"));

        LOGGER.debug("debug");
        LOGGER.debug("%s{0} %s{1}", "Hello", "World");
        LOGGER.debug(new StringBuilder("%s{class}.%s{method}:%s{line}"));
    }

}
