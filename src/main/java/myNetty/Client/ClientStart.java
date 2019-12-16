package myNetty.Client;

import myNetty.protocol.RpcRequest;
import myNetty.protocol.RpcResponse;
import myNetty.server.NettyServer;
import myNetty.service.CommonService;
import myNetty.service.CommonServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientStart {
    public static void main(String[] args) {
//        RPCClient client = new RPCClient(10086, "localhost");
//        client.start(request);

//        RPCProxy rpcProxy = new RPCProxy("localhost", 10086);
//        CommonService commonService = (CommonService) rpcProxy.proxy(CommonService.class);
//        System.out.println(commonService.getMoney(20));
//        System.out.println(commonService.getContext("haha"));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(()->{
                RPCProxy proxy = new RPCProxy("localhost", 10086);
                CommonService service = (CommonService) proxy.proxy(CommonService.class);
                for (int j = 0; j < 100; j++) {
                    System.out.println(service.getContext(Thread.currentThread().getName()+"="+j));
                }
            });
        }
        executorService.shutdown();




    }

}
