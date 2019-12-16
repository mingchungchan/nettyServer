package myNetty.service;

import myNetty.annotation.RPCService;

@RPCService("CommonService")
public class CommonServiceImpl  implements CommonService{

    public Integer getMoney(Integer num) {
        return num+1;
    }

    public String getContext(String context) {
        System.out.println("调用一次该方法："+context);
        return "成功调用了context方法，" + context;
    }


}
