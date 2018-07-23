package factory.build;

import dao.db.annotation.DB_table;
import dao.db.imp.DatabaseDao;
import dao.fileStore.annotation.SystemFile;
import dao.fileStore.imp.FileStoreDao;
import dao.service.Dao;
import util.Build;

import java.util.Objects;

class DaoBuild implements Build<Dao> {

    public static Dao getDao(Class<?> key){
        if (key.isAnnotationPresent(DB_table.class)) {
            return new DatabaseDao();
        } else if (key.isAnnotationPresent(SystemFile.class)) {
            return new FileStoreDao();
        } else {
            return null;
        }
    }

    private Class<?> clazz;

    public DaoBuild setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public Dao build() {
        return Objects.requireNonNull(getDao(clazz),"未添加相应注解");
    }
}
