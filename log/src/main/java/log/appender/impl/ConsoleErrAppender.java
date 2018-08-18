package log.appender.impl;

import log.appender.Appender;

import java.util.function.Supplier;

public class ConsoleErrAppender implements Appender {

    private static final Appender INSTANCE = new ConsoleErrAppender();

    @Override
    public void out(String text) {
        System.out.println(text);
    }

    public static Supplier<Appender> getAppender(){
        return ()->INSTANCE;
    }

}
