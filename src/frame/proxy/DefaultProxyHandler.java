package frame.proxy;

import java.lang.reflect.Method;

public class DefaultProxyHandler<T> extends ProxyHandler<T> {

    public DefaultProxyHandler(ProxyHandler<T> parent){
        setParent(parent);
    }

    public DefaultProxyHandler(){

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target,args);
    }

}
