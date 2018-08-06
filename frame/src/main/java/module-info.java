import factory.BeanFactoryHandler;
import factory.SingleFactory;

module myFrame.frame {
    exports frame;
    exports factory;
    exports config;
    exports util;
    exports proxyhandler to myFrame.proxy, myFrame.dao;

    provides BeanFactoryHandler with SingleFactory;
}