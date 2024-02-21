package icu.buzz.rpc;

import java.lang.reflect.Proxy;

public class ServiceFactory {

    public static <T> T getService(Class<T> klass) {
        return (T) Proxy.newProxyInstance(klass.getClassLoader(), new Class<?>[]{klass}, new RpcHandler());
    }
}
