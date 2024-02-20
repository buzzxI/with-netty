package icu.buzz.rpc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import icu.buzz.pojo.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class RpcCodec extends ByteToMessageCodec<RpcResponse> {
    private final Kryo kryo;

    public RpcCodec() {
        this.kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        kryo.register(Object[].class);
        kryo.register(User.class);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse msg, ByteBuf out) {
        try (Output output = new Output(1024, -1)) {
            kryo.writeObject(output, msg);
            int len = output.position();
            out.writeInt(len);
            out.writeBytes(output.getBuffer(), 0, len);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) return;
        // mark head of the rpc request object
        in.markReaderIndex();
        // the length of the rpc request object
        int len = in.readInt();
        if (in.readableBytes() < len) {
            // reset the reader index to the head of the rpc request object
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        // try with resources will close the input automatically
        try (Input input = new Input(bytes)) {
            RpcRequest request = kryo.readObject(input, RpcRequest.class);
            out.add(request);
        }

    }
}
