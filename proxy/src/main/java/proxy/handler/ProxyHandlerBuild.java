package proxy.handler;


import factory.BeanFactory;
import proxyhandler.ProxyHandler;
import util.Build;

import java.util.Objects;

public class ProxyHandlerBuild<T> implements Build<ProxyHandler<T>> {

    private BeanFactory configFactory;
    private boolean isAop = true;
    private boolean isAsync = true;

    public ProxyHandlerBuild<T> setAop(boolean isAop){
        this.isAop = isAop;
        return this;
    }

    public ProxyHandlerBuild<T> setAsync(boolean isAsync){
        this.isAsync = isAsync;
        return this;
    }

    public ProxyHandlerBuild<T> setBeanFactory(BeanFactory configFactory){
        isAop = true;
        this.configFactory = Objects.requireNonNull(configFactory);
        return this;
    }

    @Override
    public ProxyHandler<T> build() {
        ProxyHandler<T> proxyHandler = new DefaultProxyHandler<>();
        if (isAsync){
            proxyHandler = new AsyncProxyHandler<>(proxyHandler);
        }
        if (isAop){
            proxyHandler = new AopProxyHandler<>(proxyHandler);
            ((AopProxyHandler<T>)proxyHandler).setFactory(Objects.requireNonNull(this.configFactory));
        }
        return proxyHandler;
    }
}
