package log.layout;

import log.layout.message.MessageManage;

import java.util.ArrayList;
import java.util.Formatter;

public class LayoutImp implements Layout {
    @Override
    public String layout(String text, Object[] objects) {
        return layout(new StringBuilder(text), objects);
    }

    private String layout(StringBuilder text, Object[] objects){
        int left = text.indexOf("{");
        int right = text.indexOf("}");
        ArrayList<String> args = new ArrayList<>();
        while (-1 != left && -1 != right) {
            String sub = text.substring(left+1, right);
            try {
                int index = Integer.parseInt(sub);
                args.add(objects[index].toString());
            }catch (NumberFormatException e) {
                args.add(MessageManage.get(sub));
            }
            text.delete(left, right + 1);
            left = text.indexOf("{");
            right = text.indexOf("}");
        }
        Object[] strings =  args.toArray();
        return new Formatter().format(text.toString(),strings).toString();
    }

}
