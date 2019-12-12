package myNetty.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import myNetty.protocol.RpcDecoder;
import myNetty.protocol.RpcEncoder;
import myNetty.protocol.RpcRequest;
import myNetty.protocol.RpcResponse;

public class NettyClientUseChannelInboundHandlerAdapter {
    /*
     * 服务器端口号
     */
    private int port;

    /*
     * 服务器IP
     */
    private String host;
    private volatile Channel channel;

    public NettyClientUseChannelInboundHandlerAdapter(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
        start();
    }

    public void send(RpcRequest request) {
        channel.writeAndFlush(request);
        //阻塞等待消息
//        synchronized (NettyClient.class) {
//            wait();
//        }
    }

    private void start() throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host, port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    socketChannel.pipeline()
                            .addLast(new RpcDecoder(RpcResponse.class))//解码
                            .addLast(new RpcEncoder(RpcRequest.class))//编码
                            .addLast(new ClientHandler());

                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if (channelFuture.isSuccess()) {
                System.err.println("连接服务器成功");
                channel = channelFuture.channel();
            }
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClientUseChannelInboundHandlerAdapter(10086, "localhost");

    }
}
