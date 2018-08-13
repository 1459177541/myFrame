package log.layout.message;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DateMessage implements Message{

    private static Message instance = new DateMessage();

    public static Message getInstance(){
        return instance;
    }
    private static Map<String, Integer> timeMap;


    static{
        timeMap = new HashMap<>(12);
        timeMap.put("year", 1);
        timeMap.put("month", 2);
        timeMap.put("week_of_year", 3);
        timeMap.put("week_of_month", 4);
        timeMap.put("day", 5);
        timeMap.put("day_of_year", 6);
        timeMap.put("day_of_week", 7);
        timeMap.put("am_or_pm", 9);
        timeMap.put("hour", 10);
        timeMap.put("hour_of_day", 11);
        timeMap.put("minute", 12);
        timeMap.put("second", 13);
    }

    @Override
    public String get() {
        return Calendar.getInstance().getTime().toString();
    }

    @Override
    public String get(String name) {
        Calendar calendar = Calendar.getInstance();
        int value = calendar.get(timeMap.get(name));
        if ("month".equals(name)){
            value++;
        }
        return value+"";
    }

    @Override
    public String get(Object[] objects) {
        return null;
    }

    @Override
    public String get(String name, Object[] objects) {
        return null;
    }

}
