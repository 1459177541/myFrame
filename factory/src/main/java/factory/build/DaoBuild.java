package factory.build;

import dao.service.Dao;
import util.Build;

import java.util.*;
import java.util.stream.Collectors;

public class DaoBuild implements Build<Dao> {

    private static Map<Dao, Integer> daoList;

    private static int size = 0;

    static{
        ServiceLoader.load(Dao.class, DaoBuild.class.getClassLoader())
                .forEach(dao -> daoList.put(dao,++size));
    }

    public static Dao getDao(Class<?> key){
        if (daoList.size()==0){
            throw new NullPointerException("找不到可用服务");
        }
        return Objects.requireNonNull(
//                daoList.values().stream().filter(dao -> dao.isCanLoad(key)).findFirst().orElse(null)
                daoList.entrySet().stream()
                        .sorted(Comparator.comparingInt(Map.Entry::getValue))
                        .map(Map.Entry::getKey)
                        .filter(dao -> dao.isCanLoad(key))
                        .findFirst().orElse(null)
                ,"未找到适合的服务");
    }

    public static Set<String> getDaoNameList(){
        return daoList.values().stream().map(dao -> dao.getClass().getSimpleName()).collect(Collectors.toSet());
    }

    public static int getSize(){
        return size;
    }

    public static void setDaoOrder(Dao dao, int order){
        if (daoList.containsKey(dao)){
            final int index = daoList.get(dao);
            daoList.entrySet().stream()
                    .filter(daoIntegerEntry ->
                            (daoIntegerEntry.getValue() <= Math.max(order,index))
                                    && (daoIntegerEntry.getValue() >= Math.min(order,index)))
                    .forEach(daoIntegerEntry -> daoIntegerEntry.setValue(daoIntegerEntry.getValue()+1));
        }else {
            daoList.entrySet().stream()
                    .filter(daoIntegerEntry -> daoIntegerEntry.getValue()>=order)
                    .forEach(daoIntegerEntry -> daoIntegerEntry.setValue(daoIntegerEntry.getValue()+1));
        }
        daoList.put(dao,order);
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
