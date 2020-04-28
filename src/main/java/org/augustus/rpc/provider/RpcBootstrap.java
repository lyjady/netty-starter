package org.augustus.rpc.provider;

/**
 * @author LinYongJin
 * @date 2020/4/28 21:17
 */
public class RpcBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 8080);
    }
}
