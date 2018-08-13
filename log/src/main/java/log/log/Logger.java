package log.log;

public interface Logger {

    void trace(String text);

    void trace(Object obj);

    void trace(String text, Object...objects);

    void debug(String text);

    void debug(Object obj);

    void debug(String text, Object...objects);

    void info(String text);

    void info(Object obj);

    void info(String text, Object...objects);

    void warn(String text);

    void warn(Object obj);

    void warn(String text, Object...objects);

    void error(String text);

    void error(Object obj);

    void error(String text, Object...objects);

    void fatal(String text);

    void fatal(Object obj);

    void fatal(String text, Object...objects);

}
