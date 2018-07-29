module myFrame.factory {
    exports factory.build;
    exports factory.factory;

    requires myFrame.frame;
    requires myFrame.dao;

    uses dao.service.Dao;
    uses factory.BeanFactory;
}