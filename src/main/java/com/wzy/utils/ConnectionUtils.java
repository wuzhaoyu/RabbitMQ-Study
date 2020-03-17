package com.wzy.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * 类功能说明: 连接工具
 * 类修改者	创建日期2020/3/16
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class ConnectionUtils {

    /**
     * 获取连接对象
     * @return
     */
    public static Connection getConnection() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        //内部通信端口
        factory.setPort(5672);
        factory.setUsername("user_mmr");
        factory.setPassword("123");
        // VirtualHost。虚拟消息服务器 类似mysql数据库 相互独立
        factory.setVirtualHost("/mmr_queue");
        return factory.newConnection();
    }
}
