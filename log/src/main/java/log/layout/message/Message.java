package log.layout.message;

public interface Message {

    String get();

    String get(String name);

    String get(Object[] objects);

    String get(String name, Object[] objects);


}
