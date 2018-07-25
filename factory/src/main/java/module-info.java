module myFrame.factory {
    exports factory.build;

    requires myFrame.frame;
    requires myFrame.proxy;
    requires myFrame.dao;

    uses dao.service.Dao;
}