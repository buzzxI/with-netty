package icu.buzz.rpc;

import java.lang.reflect.Proxy;

public class ServiceFactory {

    private static final String HOST = "localhost";

    private static final int PORT = 8888;

    private static Consumer consumer;

    public static <T> T getService(Class<T> klass) {
        return (T) Proxy.newProxyInstance(klass.getClassLoader(), new Class<?>[]{klass}, new RpcHandler());
    }
}
