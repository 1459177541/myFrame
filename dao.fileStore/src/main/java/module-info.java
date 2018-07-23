module myFrame.fileStore {
    exports dao.fileStore.imp;
    exports dao.fileStore.annotation;

    requires myFrame.dao;
    requires myFrame.asynchronous;
}