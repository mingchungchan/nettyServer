package myNetty.client;

import myNetty.service.CommonService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RPC客户端启动服务，每次调用都要连接一次
 */
public class ClientStart {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(()->{
                RPCProxy proxy = new RPCProxy("localhost", 10086);
                CommonService service = (CommonService) proxy.proxy(CommonService.class);
                for (int j = 0; j < 100; j++) {
                    //远程调用，并返回结果
                    System.out.println(service.getContext(Thread.currentThread().getName()+"="+j));
                }
            });
        }
        executorService.shutdown();




    }

}
