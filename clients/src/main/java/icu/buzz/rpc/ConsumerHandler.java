package icu.buzz.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;

public class ConsumerHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private CompletableFuture<RpcResponse> future;

    public void setFuture(CompletableFuture<RpcResponse> future) {
        this.future = future;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        future.complete(msg);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        future.completeExceptionally(cause);
        ctx.close();
    }
}
