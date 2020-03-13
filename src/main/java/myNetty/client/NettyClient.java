package myNetty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import myNetty.protocol.RpcDecoder;
import myNetty.protocol.RpcEncoder;
import myNetty.protocol.RpcRequest;
import myNetty.protocol.RpcResponse;

/**
 * 消息接收逻辑处理 和 创建netty客户端
 */
public class NettyClient extends SimpleChannelInboundHandler{
    /*
     * 服务器端口号
     */
    private int port;

    /*
     * 服务器IP
     */
    private String host;
    private volatile Channel channel;
    private RpcResponse response;

    public NettyClient(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
    }

    public RpcResponse start(RpcRequest request) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
//            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host, port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    socketChannel.pipeline()
                            .addLast(new RpcDecoder(RpcResponse.class))//解码
                            .addLast(new RpcEncoder(RpcRequest.class))//编码
                            .addLast(NettyClient.this);

                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if (channelFuture.isSuccess()) {
                System.err.println("连接服务器成功");
                channel = channelFuture.channel();
                channel.writeAndFlush(request);
            }

            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        return response;
    }


    //接受信息
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        response = (RpcResponse) o;
        //断开连接
        channelHandlerContext.channel().close();

    }
}
