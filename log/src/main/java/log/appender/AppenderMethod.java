package log.appender;

import log.appender.impl.ConsoleErrAppender;
import log.appender.impl.ConsoleOutAppender;
import log.appender.impl.FileStoreAppender;

import java.util.function.Supplier;

public enum AppenderMethod {

    FILE_STORT(FileStoreAppender.getAppender()),
    CONSOLE_OUT(ConsoleOutAppender.getAppender()),
    CONSOLE_ERR(ConsoleErrAppender.getAppender());

    private Supplier<Appender> appender;

    AppenderMethod(Supplier<Appender> appender){
        this.appender = appender;
    }

    public Appender getAppender(){
        return appender.get();
    }

}
