module myFrame.factory {
    exports factory.build;

    requires myFrame.frame;
    requires myFrame.dao;

    uses dao.service.Dao;
    uses factory.BeanFactory;
}