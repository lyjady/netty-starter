package org.augustus.rpc.provider;

import io.netty.util.internal.StringUtil;
import org.augustus.rpc.api.HelloService;

/**
 * @author LinYongJin
 * @date 2020/4/28 21:12
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            return "服务器收到你的请求";
        } else {
            return "服务器收到你的请求: <" + msg + ">";
        }
    }
}
