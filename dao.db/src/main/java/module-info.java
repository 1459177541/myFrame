module myFrame.daodb {
    exports dao.db.annotation;
    exports dao.db.imp;

    requires myFrame.frame;
    requires myFrame.dao;
    requires myFrame.asynchronous;
    requires java.sql;
}