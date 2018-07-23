module myFrame.dao.fileStore {
    exports dao.fileStore.imp;
    exports dao.fileStore.annotation;

    requires transitive myFrame.dao;
    requires myFrame.asynchronous;

    provides dao.service.Dao with dao.fileStore.imp.FileStoreDao;
}