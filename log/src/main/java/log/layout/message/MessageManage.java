package log.layout.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageManage {

    private static Map<String, Message> messageMap;

    static{
        messageMap = new HashMap<>();

        // TODO 未添加信息源
        messageMap.put("level", null);

        messageMap.put("line", ClassInfoMessage.getInstance());
        messageMap.put("method", ClassInfoMessage.getInstance());
        messageMap.put("class", ClassInfoMessage.getInstance());
        messageMap.put("module", ClassInfoMessage.getInstance());
        messageMap.put("module_version", ClassInfoMessage.getInstance());
        messageMap.put("thread", ClassInfoMessage.getInstance());

        messageMap.put("year", DataMessage.getInstance());
        messageMap.put("month", DataMessage.getInstance());
        messageMap.put("week_of_year", DataMessage.getInstance());
        messageMap.put("week_of_month", DataMessage.getInstance());

        messageMap.put("date", DataMessage.getInstance());
        messageMap.put("date_of_year", DataMessage.getInstance());
        messageMap.put("date_of_month", DataMessage.getInstance());
        messageMap.put("date_of_week", DataMessage.getInstance());

        messageMap.put("am_or_pm", DataMessage.getInstance());
        messageMap.put("hour", DataMessage.getInstance());
        messageMap.put("hour_of_day", DataMessage.getInstance());
        messageMap.put("minute", DataMessage.getInstance());
        messageMap.put("second", DataMessage.getInstance());
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
