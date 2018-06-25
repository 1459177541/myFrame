package build;

import factory.ConfigFactory;
import frame.proxy.AopProxyHandler;
import frame.proxy.AsyncProxyHandler;
import frame.proxy.DefaultProxyHandler;
import frame.proxy.ProxyHandler;

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
        this.configFactory = configFactory;
        return this;
    }

    @Override
    public ProxyHandler<T> get() {
        ProxyHandler<T> proxyHandler = new DefaultProxyHandler<>();
        if (isAsync){
            proxyHandler = new AsyncProxyHandler<>(proxyHandler);
        }
        if (isAop){
            proxyHandler = new AopProxyHandler<>(proxyHandler);
            ((AopProxyHandler<T>)proxyHandler).setFactory(configFactory);
        }
        return proxyHandler;
    }
}
