package log.log;

public enum  LoggerLevel {
    TRACE("trace"),
    DEBUG("debug"),
    INFO("info"),
    WARN("warn"),
    ERROR("error"),
    FATAL("fatal");
    private String name;
    LoggerLevel(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
