module myFrame.factory {
    exports factory.build;

    requires myFrame.frame;
    requires myFrame.proxy;
    requires myFrame.dao;
    requires myFrame.fileStore;
    requires myFrame.daodb;
}