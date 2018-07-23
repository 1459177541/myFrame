module myFrame.factory {
    exports factory.build;

    requires myFrame.frame;
    requires myFrame.proxy;
    requires myFrame.dao;

    uses dao.service.Dao;
    uses factory.Factory;
    uses util.Build;

    provides util.Build with factory.build.FactoryBuilder;
    provides factory.Factory with factory.build.BeanBufferFactory;
}