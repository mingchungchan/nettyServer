package myNetty.service;

import myNetty.annotation.RPCService;

@RPCService("CommonService")
public
interface CommonService {

    public Integer getMoney(Integer num);

}
