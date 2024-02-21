package icu.buzz.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcHandler implements InvocationHandler {
    private static final String HOST = "localhost";

    private static final int PORT = 8888;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Consumer consumer = new Consumer(HOST, PORT);
        try {
            consumer.init();
            RpcRequest request = new RpcRequest(method.getDeclaringClass().getName(), method.getName(), args);
            RpcResponse response = consumer.invoke(request);
            if (response == null) return null;
            return response.getRst();
        } catch (InterruptedException e) {
            return null;
        } finally {
            consumer.release();
        }
    }
}
