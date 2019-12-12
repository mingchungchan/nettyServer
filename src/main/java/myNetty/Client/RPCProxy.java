package myNetty.Client;

import myNetty.protocol.RpcRequest;
import myNetty.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RPCProxy {
    private String address;
    private int port;

    public RPCProxy(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public <T>T proxy(Class<?> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest request = new RpcRequest();
                request.setClassName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParameters(args);
                request.setRequestId(UUID.randomUUID().toString());
                request.setParameterTypes(method.getParameterTypes());
                RPCClient client =new RPCClient(port,address);
                RpcResponse response = client.start(request);
                if (response.getStatus()!=0){
                    throw new NoSuchMethodException();
                }else{
                    return response;
                }
            }
        });
    }
}
