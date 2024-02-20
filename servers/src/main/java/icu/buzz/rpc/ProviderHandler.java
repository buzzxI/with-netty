package icu.buzz.rpc;

import icu.buzz.ServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProviderHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Map<String, Class<?>> SERVICES;

    static {
        SERVICES = new HashMap<>();
        SERVICES.put("Service", ServiceImpl.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        RpcResponse response = new RpcResponse(null);

        Class<?> klass = SERVICES.get(msg.getServiceName());
        if (klass == null) {
            ctx.writeAndFlush(response);
            return;
        }

        Object[] params = msg.getParams();
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) paramTypes[i] = params[i].getClass();
        Method method = klass.getMethod(msg.getMethodName(), paramTypes);

        Object instance = klass.getDeclaredConstructor().newInstance();
        Object result = method.invoke(instance, params);

        response.setRst(result);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
