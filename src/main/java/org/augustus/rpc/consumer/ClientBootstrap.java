package org.augustus.rpc.consumer;

import org.augustus.rpc.api.HelloService;

/**
 * @author LinYongJin
 * @date 2020/4/28 21:43
 */
public class ClientBootstrap {

    public static void main(String[] args) {
        NettyClient customer = new NettyClient();
        HelloService service = (HelloService) customer.getBean(HelloService.class, "HelloService#hello#");
        String res = service.hello("RPC~~~");
        System.out.println("调用的结果 res= " + res);
    }
}
