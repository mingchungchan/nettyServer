package myNetty.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * RPC服务端启动服务
 */
public class ServerStart {
    public static void main(String[] args) {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");

        new NettyServer(10086);
    }

}
