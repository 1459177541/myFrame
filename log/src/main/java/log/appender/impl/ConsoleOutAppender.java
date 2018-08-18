package log.appender.impl;

import log.appender.Appender;

import java.util.function.Supplier;

public class ConsoleOutAppender implements Appender {
    private static final Appender INSTANCE = new ConsoleOutAppender();

    @Override
    public void out(String text) {
        System.out.println(text);
    }

    public static Supplier<Appender> getAppender(){
        return ()->INSTANCE;
    }
}
