package icu.buzz.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Consumer {
    private final String host;

    private final int port;

    private EventLoopGroup worker;
    private Channel channel;

    public Consumer(String host,int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws InterruptedException {
        this.worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("rpc coder/decoder", new RpcCodec())
                                .addLast("Consumer Handler", new ConsumerHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        this.channel = bootstrap.connect(host, port).sync().channel();
    }

    public void release() {
        try {
            if (channel != null && channel.isOpen()) this.channel.close().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.worker.shutdownGracefully();
        }
    }

    public RpcResponse invoke(RpcRequest request) {
        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        channel.pipeline().get(ConsumerHandler.class).setFuture(future);
        channel.writeAndFlush(request);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }
}
