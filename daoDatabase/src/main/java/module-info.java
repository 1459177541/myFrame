module myFrame.daoDatabase {
    exports dao.db.annotation;
    exports dao.db.impl;

    requires myFrame.frame;
    requires myFrame.dao;
    requires myFrame.asynchronous;
    requires java.sql;

    provides dao.service.Dao with dao.db.impl.DatabaseDao;
}