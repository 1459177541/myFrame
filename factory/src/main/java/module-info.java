import factory.BeanFactoryHandler;

module myFrame.factory {
    exports factory.build;
    exports factory.factory;

    requires transitive myFrame.frame;
    requires transitive myFrame.dao;

    uses dao.service.Dao;
    uses BeanFactoryHandler;
}