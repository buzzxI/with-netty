package icu.buzz.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClient {
    private String host;
    private int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("line based decoder", new LineBasedFrameDecoder(1024))
                                .addLast("String content decoder", new StringDecoder(StandardCharsets.UTF_8))
                                .addLast("String content encoder", new StringEncoder(StandardCharsets.UTF_8))
                                .addLast("Chat client handler", new ChatClientHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture future = bootstrap.connect(this.host, this.port).sync();
            Scanner in = new Scanner(System.in);
            while (in.hasNextLine()) {
                String line = in.nextLine() + "\n";
                future.channel().writeAndFlush(line);
            }
            // shutdown after eof
            future.channel().close().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
