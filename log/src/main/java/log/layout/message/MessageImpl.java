package log.layout.message;

import log.log.LoggerLevel;

public class MessageImpl implements Message{

    private static Message instance = new MessageImpl();

    public static Message getInstance(){
        return instance;
    }

    @Override
    public String get() {
        throw new IllegalArgumentException("无法推断");
    }

    @Override
    public String get(String name) {
        return name;
    }

    @Override
    public String get(Object[] objects) {
        if (null == objects || 0 == objects.length){
            return "null";
        }
        if (objects[0] instanceof LoggerLevel && 1 == objects.length){
            return objects[0].toString();
        }
        return null;
    }

    @Override
    public String get(String name, Object[] objects) {
        if ("level".equals(name)){
            return objects[0].toString();
        }
        return null;
    }
}
