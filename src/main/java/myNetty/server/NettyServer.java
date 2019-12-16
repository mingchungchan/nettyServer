package myNetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import myNetty.annotation.RPCService;
import myNetty.protocol.RpcDecoder;
import myNetty.protocol.RpcEncoder;
import myNetty.protocol.RpcRequest;
import myNetty.protocol.RpcResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyServer {
    private Map<String,Object> serviceMap = new HashMap<String,Object>();
    private int port;

    public NettyServer(int port) {
        this.port = port;
        bind();
    }

    private void bind() {
        EventLoopGroup boss = new NioEventLoopGroup();//bossGroup就是parentGroup，是负责处理TCP/IP连接的
        EventLoopGroup worker = new NioEventLoopGroup();//workerGroup就是childGroup,是负责处理Channel(通道)的I/O事件

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024); // 连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, true); // 不延迟，消息立即发送
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // 长连接
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {// 绑定客户端连接时候触发操作
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();
                    p.addLast(new RpcEncoder(RpcResponse.class))
                            .addLast(new RpcDecoder(RpcRequest.class))
                            .addLast(new ServerHandler());// 添加NettyServerHandler，用来处理Server端接收和处理消息的逻辑;
                }
            });
            //绑定监听端口，调用sync同步阻塞方法等待绑定操作完
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                System.err.println("启动Netty服务成功，端口号：" + this.port);
            }

            //成功绑定到端口之后,给channel增加一个 管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程。
            //关闭连接
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.err.println("启动Netty服务异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer(10086);
    }

}
