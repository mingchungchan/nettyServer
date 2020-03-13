package myNetty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import myNetty.protocol.RpcRequest;
import myNetty.protocol.RpcResponse;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

/**
 * 用来实现Server端接收和处理消息的逻辑
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    final String impl = "Impl";


    //接受client发送的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RpcRequest request = (RpcRequest) msg;
        RpcResponse response = new RpcResponse();

        //调用请求类的请求方法执行并返回执行结果
        Object invoke = null;
        try {
//            Object requestBean = serviceMap.get(request.getClassName());
            Class<?> requestClazz = Class.forName(request.getClassName()+impl);
            Object requestBean=requestClazz.getConstructor().newInstance();
            Method method = requestClazz.getMethod(request.getMethodName(), request.getParameterTypes());
            invoke = method.invoke(requestBean, request.getParameters());
            response.setRequestId(UUID.randomUUID().toString());
            response.setData(invoke);
            response.setStatus(1);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(0);
            response.setRequestId(UUID.randomUUID().toString());
        }
        //返回执行结果
        ctx.writeAndFlush(response);
    }

    /**
     *
     * 从ByteBuf中获取信息 使用UTF-8编码返回
     */
    private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {
        byte[] req = message.getBytes("UTF-8");
        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(req);

        return pingMessage;
    }

    //通知处理器最后的channelRead()是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("服务端接收数据完毕..");
        ctx.flush();
    }

    //读操作时捕获到异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    //客户端去和服务端连接成功时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush("hello client");
    }
}
