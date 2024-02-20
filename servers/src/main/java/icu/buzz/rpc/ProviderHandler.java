package icu.buzz.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProviderHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
        RpcResponse response = new RpcResponse(null);
        try {
            // get class
            Class<?> klass = Class.forName(msg.getServiceName() + "Impl");
            // get method
            Object[] params = msg.getParams();
            Class<?>[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) paramTypes[i] = params[i].getClass();
            Method method = klass.getMethod(msg.getMethodName(), paramTypes);
            // new an instance
            Object instance = klass.getDeclaredConstructor().newInstance();

            Object result = method.invoke(instance, params);

            response.setRst(result);
            response.setMsg("all good");
        } catch (ClassNotFoundException e) {
            response.setMsg("service has not been provided");
        } catch (NoSuchMethodException | SecurityException e) {
            response.setMsg("function has not been provided");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
