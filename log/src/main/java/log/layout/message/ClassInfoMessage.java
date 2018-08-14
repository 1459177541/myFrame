package log.layout.message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassInfoMessage implements Message{

    /**
     * 堆栈层数，未经测试
     */
    private static final int STACK_NUMBER = 9;

    private static Message instance = new ClassInfoMessage();

    public static Message getInstance() {
        return instance;
    }

    private static Map<String, Method> methodMap;

    static{
        Map<String, Method> stringMethodMap = Stream.of(StackTraceElement.class.getMethods())
                .filter(method -> 0 == method.getParameterCount())
                .collect(Collectors.toMap(Method::getName, Function.identity()));
        methodMap = Map.of(
                "line", stringMethodMap.get("getLineNumber"),
                "method", stringMethodMap.get("getMethodName"),
                "class", stringMethodMap.get("getClassName"),
                "module", stringMethodMap.get("getModuleName"),
                "module_version", stringMethodMap.get("getModuleVersion")
        );
    }

    @Override
    public String get() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[STACK_NUMBER];
        return stackTraceElement.toString();
    }

    @Override
    public String get(String name) {
        if ("thread".equals(name)){
            return Thread.currentThread().getName();
        }
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[STACK_NUMBER];
        try {
            return Optional.ofNullable(methodMap.get(name).invoke(stackTraceElement)).map(Object::toString).orElse("null");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "null";
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
