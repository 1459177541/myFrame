module myFrame.daoFileStore {
    exports dao.fileStore.annotation;
    exports dao.fileStore.impl;

    requires myFrame.frame;
    requires myFrame.dao;
    requires myFrame.asynchronous;

    provides dao.service.Dao with dao.fileStore.impl.FileStoreDao;
}