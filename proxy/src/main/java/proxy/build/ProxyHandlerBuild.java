package proxy.build;


import factory.ConfigFactory;
import frame.Build;
import proxy.AopProxyHandler;
import proxy.AsyncProxyHandler;
import proxy.DefaultProxyHandler;
import proxy.ProxyHandler;

import java.util.Objects;

public class ProxyHandlerBuild<T> implements Build<ProxyHandler<T>> {

    private ConfigFactory configFactory;
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

    public ProxyHandlerBuild<T> setConfigFactory(ConfigFactory configFactory){
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
