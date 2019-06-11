package test.com.springboot.autoconfig.nrpc.server.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import test.com.springboot.autoconfig.nrpc.server.serialize.SerializationUtil;

import java.util.List;

/**
 *反序列化
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> aClass;

    public RpcDecoder(Class<?> aClass) {
        this.aClass = aClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        System.out.println("RpcDecoder-------------->");
        //判断长度字段是否正确
        int i = byteBuf.readableBytes();
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        //标记当前下标
        byteBuf.markReaderIndex();
        //获取数据长度
        int dataLength = byteBuf.readInt();
        //长度小于0表示有异常
        if (dataLength < 0) {
            channelHandlerContext.close();
        }
        i = byteBuf.readableBytes();
        //判断剩余的字节长度是否大于数据长度
        if (byteBuf.readableBytes() < dataLength) {
            //如果剩余长度不够，还原到标记下标位置
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        //反序列化
        Object o = SerializationUtil.deserialize(data, this.aClass);
        list.add(o);
    }
}
