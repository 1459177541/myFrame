import factory.BeanFactoryHandler;
import proxy.build.ProxyFactory;

module myFrame.proxy {
    exports proxy.action;
    exports proxy.annotation;
    exports proxy.build;

    requires myFrame.frame;
    requires myFrame.asynchronous;

    provides BeanFactoryHandler with ProxyFactory;
}