package factory.build;

import dao.service.Dao;
import util.Build;

import java.util.Objects;
import java.util.ServiceLoader;

class DaoBuild implements Build<Dao> {

    public static Dao getDao(Class<?> key){
        return ServiceLoader.load(Dao.class).stream().map(ServiceLoader.Provider::get).filter(dao->dao.isCanLoad(key)).findFirst().get();
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
