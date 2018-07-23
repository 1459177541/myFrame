module myFrame.dao.database {
    exports dao.db.annotation;
    exports dao.db.imp;

    requires myFrame.frame;
    requires transitive myFrame.dao;
    requires myFrame.asynchronous;
    requires java.sql;

    provides dao.service.Dao with dao.db.imp.DatabaseDao;
}