package dao.systemFile;

import dao.sf.annotation.SystemFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SFUtil {

    private SFUtil(){}

    public static <T> ArrayList<T> read(Class<T> clazz) throws FileNotFoundException {
        if (!clazz.isAnnotationPresent(SystemFile.class)){
            throw new RuntimeException("无法从文件中读取");
        }
        ArrayList<T> list = new ArrayList<>();
        File f = new File(clazz.getAnnotation(SystemFile.class).fileName());
        try(
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fs);
                ){
            T t;
            while (null!=(t = (T)ois.readObject())){
                list.add(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> void write(List<T> data) throws FileNotFoundException{
        if (0==data.size()){
            return;
        }
        Class<T> clazz = (Class<T>) data.get(0).getClass();
        if (!clazz.isAnnotationPresent(SystemFile.class)){
            throw new RuntimeException("无法从文件中读取");
        }
        File f = new File(clazz.getAnnotation(SystemFile.class).fileName());
        try(
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
                ){
            data.stream().forEach(e->{
                try {
                    oos.writeObject(e);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
