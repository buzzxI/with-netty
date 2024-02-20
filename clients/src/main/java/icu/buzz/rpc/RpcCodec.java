package icu.buzz.rpc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import icu.buzz.pojo.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class RpcCodec extends ByteToMessageCodec<RpcRequest> {
    private final Kryo kryo;

    public RpcCodec() {
        this.kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        kryo.register(Object[].class);
        kryo.register(User.class);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest msg, ByteBuf out) {
        // try with resources will close the output automatically
        // create a kryo output buffer with size 1024, maximum size of the buffer is -1 (on limit)
        try (Output output = new Output(1024, -1)) {
            // write object into kryo output
            kryo.writeObject(output, msg);
            // get output size
            int len = output.position();
            // write object length into ByteBuf
            out.writeInt(len);
            // write object self into ByteBuf
            out.writeBytes(output.getBuffer(), 0, len);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return;
        }
        // mark current position -> size of Object
        in.markReaderIndex();
        int len = in.readInt();
        if (in.readableBytes() < len) {
            // reset to the marked position
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        // try with resources will close the input automatically
        try (Input input = new Input(bytes)) {
            RpcResponse response = kryo.readObject(input, RpcResponse.class);
            out.add(response);
        }
    }
}
