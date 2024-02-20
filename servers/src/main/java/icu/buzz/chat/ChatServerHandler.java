package icu.buzz.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.time.format.DateTimeFormatter;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    private final ChannelGroup channelGroup;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ChatServerHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.WRITER_IDLE) {
                // send ping
                ctx.writeAndFlush("ping\n");
            } else if (event.state() == IdleState.READER_IDLE) {
                // close connection
                ctx.close();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String timestamp = "[" + FORMATTER.format(java.time.LocalDateTime.now()) + "]";
        String content = timestamp + "client:" + channel.remoteAddress() + " is online\n";
        System.out.print(content);
        channelGroup.writeAndFlush(content);
        channelGroup.add(channel);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String timestamp = "[" + FORMATTER.format(java.time.LocalDateTime.now()) + "]";
        String content = timestamp + "client:" + channel.remoteAddress() + " is offline\n";
        System.out.print(content);
        channelGroup.writeAndFlush(content, ch -> ch != channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        Channel channel = ctx.channel();
        String timestamp = "[" + FORMATTER.format(java.time.LocalDateTime.now()) + "]";
        String content = timestamp + "client:" + channel.remoteAddress() + " says: " + msg + "\n";
        System.out.print(content);
        if (!msg.equals("pong")) channelGroup.writeAndFlush(content, ch -> ch != channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}

