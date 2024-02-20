package icu.buzz.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcHandler implements InvocationHandler {
    private static final String HOST = "localhost";

    private static final int PORT = 8888;

    private static Consumer consumer;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (consumer == null) {
            consumer = new Consumer(HOST, PORT);
            try {
                consumer.init();
            } catch (InterruptedException e) {
                consumer.release();
                consumer = null;
            }
            if (consumer == null) return null;
        }

        RpcRequest request = new RpcRequest(method.getDeclaringClass().getName(), method.getName(), args);
        RpcResponse response = consumer.invoke(request);
        if (response == null) return null;
        return response.getRst();
    }
}
