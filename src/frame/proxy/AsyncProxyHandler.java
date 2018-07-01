package frame.proxy;

import java.lang.reflect.Method;

import frame.proxy.annotation.Async;
import util.asynchronized.AsyncExecuteManage;

public class AsyncProxyHandler<T> extends DefaultProxyHandler<T>{

    public AsyncProxyHandler(ProxyHandler<T> parent) {
        super(parent);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Async.class)){
            return myInvoke(method,args);
        }
        return AsyncExecuteManage.startResult(()-> {
            try {
                return myInvoke(method, args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        });
    }

}
