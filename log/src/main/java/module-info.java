/**
 * 模仿Log4j
 */
module myFrame.log {
    exports log.layout;
    exports log.appender;
    exports log.config;
    exports log.log;

    requires myFrame.frame;

}