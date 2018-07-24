package factory.build;

import dao.service.Dao;
import util.Build;

import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class DaoBuild implements Build<Dao> {

    private static Map<String, Dao> daoMap;

    static{
        daoMap = ServiceLoader.load(Dao.class)
                .stream()
                .collect(Collectors.toMap(provider->provider.type().getName(),ServiceLoader.Provider::get));
    }

    public static Dao getDao(Class<?> key){
        if (daoMap.size()==0){
            throw new NullPointerException("找不到可用服务");
        }
        return Objects.requireNonNull(
                daoMap.values().stream().filter(dao -> dao.isCanLoad(key)).findFirst().orElse(null)
                ,"未找到适合的服务");
    }

    private Class<?> clazz;

    public DaoBuild setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public Dao build() {
        return Objects.requireNonNull(getDao(clazz),"未找到适合的服务");
    }
}
