package log.appender;

import log.appender.impl.ConsoleErrAppender;
import log.appender.impl.ConsoleOutAppender;

public enum AppenderMethod {

    CONSOLE_OUT(new ConsoleOutAppender()),
    CONSOLE_ERR(new ConsoleErrAppender());

    private Appender appender;

    AppenderMethod(Appender appender){
        this.appender = appender;
    }

    public Appender getAppender(){
        return appender;
    }

}
