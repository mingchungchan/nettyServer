package myNetty.Client;

import myNetty.protocol.RpcRequest;
import myNetty.protocol.RpcResponse;
import myNetty.server.NettyServer;
import myNetty.service.CommonService;
import myNetty.service.CommonServiceImpl;


public class ClientStart {
    public static void main(String[] args) throws InterruptedException {
        RpcRequest request = new RpcRequest();
        request.setClassName("myNetty.service.CommonServiceImpl");
        request.setMethodName("getMoney");
        request.setRequestId("1");
        Class[] list = new Class[1];
        list[0] = Integer.class;
        request.setParameterTypes(list);
        Object[] objects = new Object[1];
        objects[0] = Integer.valueOf(1);
        request.setParameters(objects);

//        NettyClient nettyClient = new NettyClient(10086, "localhost");
//        RpcResponse response = nettyClient.start(request);
//        System.out.println(response.getData());
        RPCProxy rpcProxy = new RPCProxy("localhost", 10086);
        RpcResponse response = rpcProxy.proxy(CommonService.class);
        System.out.println(response.getData());


    }

}
