package myNetty.Client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import myNetty.protocol.RpcResponse;

import java.io.UnsupportedEncodingException;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    RpcResponse response;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush("服务端，你好哦，嘻嘻嘻");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        response = (RpcResponse) msg;
        System.out.println(response.getData());
        ctx.channel().close();
    }



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
}
