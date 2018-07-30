module myFrame.proxy {
    exports proxy.action;
    exports proxy.annotation;
    exports proxy.factory.impl;

    requires myFrame.frame;
    requires myFrame.asynchronous;

    provides factory.BeanFactoryHandler with proxy.factory.impl.ProxyFactory;
}