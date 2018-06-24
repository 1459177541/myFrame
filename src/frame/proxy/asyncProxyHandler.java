package frame.proxy;

import java.lang.reflect.Method;

import frame.proxy.annotation.Async;
import util.asynchronized.AsynResult;
import util.asynchronized.AsyncStaticExecuter;

public class asyncProxyHandler<T> extends ProxyHandler<T>{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Async.class)){
            return myInvoke(method,args);
        }
        AsynResult rs = AsyncStaticExecuter.startResult(()->{
            return myInvoke(method, args);
        });
        return rs;
    }
}
