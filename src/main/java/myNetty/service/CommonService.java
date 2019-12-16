package myNetty.service;

import myNetty.annotation.RPCService;
import myNetty.protocol.RpcResponse;

@RPCService("CommonService")
public
interface CommonService {

    public Integer getMoney(Integer num);


    public String getContext(String context);

}
