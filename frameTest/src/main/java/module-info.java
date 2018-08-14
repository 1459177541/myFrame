module myFrame.frameTest {
    requires myFrame.factory;
    requires myFrame.asynchronous;
    requires myFrame.dao;
    requires myFrame.daoFileStore;
    requires myFrame.proxy;
    requires myFrame.log;
    requires org.junit.jupiter.api;
}