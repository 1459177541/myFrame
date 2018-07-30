import factory.BeanFactoryHandler;
import factory.SingleFactory;

module myFrame.frame {
    exports frame;
    exports factory;
    exports config;
    exports util;

    provides BeanFactoryHandler with SingleFactory;
}