module myFrame.proxy {
    exports proxy.action;
    exports proxy.annotation;
    exports proxy.build;

    requires myFrame.frame;
    requires myFrame.asynchronous;

    provides factory.BeanFactory with proxy.build.ProxyFactory;
}