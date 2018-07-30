package proxy.handler;

import asynchronous.executor.AsyncExecuteManage;
import proxy.annotation.Async;

import java.lang.reflect.Method;

public class AsyncProxyHandler<T> extends DefaultProxyHandler<T>{

    public AsyncProxyHandler(ProxyHandler<T> parent) {
        super(parent);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Async.class)){
            return myInvoke(proxy, method,args);
        }
        return AsyncExecuteManage.startResult(()-> {
            try {
                return myInvoke(proxy, method, args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        });
    }

}
