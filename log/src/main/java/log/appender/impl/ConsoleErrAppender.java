package log.appender.impl;

import log.appender.Appender;

public class ConsoleErrAppender implements Appender {
    @Override
    public void out(String text) {
        System.out.println(text);
    }
}
