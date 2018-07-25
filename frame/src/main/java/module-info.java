module myFrame.frame {
    exports frame;
    exports factory;
    exports config;
    exports util;

    provides factory.BeanFactory with factory.SingleFactory;
}