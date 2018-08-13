package log.layout.message;

import java.util.Calendar;
import java.util.Map;

public class DataMessage implements Message{

    private static Message instance = new DataMessage();

    public static Message getInstance(){
        return instance;
    }
    private static Map<String, Integer> timeMap;


    static{
        timeMap = Map.of(
                "year", 1,
                "month", 2,
                "week_of_year", 3,
                "week_of_month", 4,

                "date", 5,
                "date_of_month", 5,
                "date_of_year", 6,
                "date_of_week", 7,

                "am_or_pm", 9,
                "hour", 10
        );
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
        return calendar.get(timeMap.get(name))+"";
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
