package myNetty.service;

import myNetty.annotation.RPCService;

@RPCService("CommonService")
public class CommonServiceImpl implements CommonService {

    public Integer getMoney(Integer num) {
        return num+1;
    }


}
