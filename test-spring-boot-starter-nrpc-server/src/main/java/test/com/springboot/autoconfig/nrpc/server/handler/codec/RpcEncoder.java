package test.com.springboot.autoconfig.nrpc.server.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import test.com.springboot.autoconfig.nrpc.server.serialize.SerializationUtil;

/**
 *序列化
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> aClass;

    public RpcEncoder(Class<?> aClass) {
        this.aClass = aClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
//        System.out.println("RpcEncoder-------------->");
        //是否能够转化这个aClass
        //是否同一个对象
        //能否强转继承的类和实现接口
        if (aClass.isInstance(msg)) {
            byte[] data = SerializationUtil.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
