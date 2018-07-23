package dao.fileStore.systemFile;

import dao.fileStore.annotation.SystemFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SFUtil {

    private SFUtil(){}

    public static <T> List<T> read(Class<T> clazz) throws FileNotFoundException {
        if (!clazz.isAnnotationPresent(SystemFile.class)){
            throw new IllegalArgumentException("无法从文件中读取");
        }
        return read(clazz.getAnnotation(SystemFile.class).fileName());
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> read(String fileName) throws FileNotFoundException{
        File f = new File(fileName);
        ArrayList<T> list = new ArrayList<>();
        try(
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fs)
                ){
            T t;
            while (null!=(t = (T)ois.readObject())){
                list.add(t);
            }
        }catch (EOFException e){
            //忽略
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> void write(List<T> data) throws FileNotFoundException{
        if (0==data.size()){
            return;
        }
        Class<T> clazz = (Class<T>) data.get(0).getClass();
        if (!clazz.isAnnotationPresent(SystemFile.class)){
            throw new IllegalArgumentException("未发现文件目录");
        }
        write(data, clazz.getAnnotation(SystemFile.class).fileName());
    }

    public static <T> void write(List<T> data, String fileName) throws FileNotFoundException{
        File f = new File(fileName);
        try(
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ){
            data.forEach(e->{
                try {
                    oos.writeObject(e);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (FileNotFoundException e){
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
