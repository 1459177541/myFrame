package log.layout.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageManage {

    private static Map<String, Message> messageMap;

    static{
        messageMap = new HashMap<>(20);

        messageMap.put("level", MessageImpl.getInstance());

        messageMap.put("line", ClassInfoMessage.getInstance());
        messageMap.put("method", ClassInfoMessage.getInstance());
        messageMap.put("class", ClassInfoMessage.getInstance());
        messageMap.put("module", ClassInfoMessage.getInstance());
        messageMap.put("module_version", ClassInfoMessage.getInstance());
        messageMap.put("thread", ClassInfoMessage.getInstance());

        messageMap.put("year", DateMessage.getInstance());
        messageMap.put("month", DateMessage.getInstance());
        messageMap.put("week_of_year", DateMessage.getInstance());
        messageMap.put("week_of_month", DateMessage.getInstance());

        messageMap.put("date", DateMessage.getInstance());
        messageMap.put("day", DateMessage.getInstance());
        messageMap.put("day_of_year", DateMessage.getInstance());
        messageMap.put("day_of_week", DateMessage.getInstance());

        messageMap.put("am_or_pm", DateMessage.getInstance());
        messageMap.put("hour", DateMessage.getInstance());
        messageMap.put("hour_of_day", DateMessage.getInstance());
        messageMap.put("minute", DateMessage.getInstance());
        messageMap.put("second", DateMessage.getInstance());
    }

    public static String get(String string){
        return Optional.ofNullable(messageMap.get(string.toLowerCase()))
                .map(message -> message.get(string.toLowerCase()))
                .orElse(string);
    }


    public static String get(String string, Object[] objects){
        if (null == objects || 0 == objects.length){
            return get(string);
        }
        return Optional.ofNullable(messageMap.get(string.toLowerCase()))
                .map(message -> message.get(string.toLowerCase(), objects))
                .orElse(string);
    }

}
