package com.wzy.client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wzy.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/3/16
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class Send {

    public static final String QUEUE_NAME = "mmr_queue";
    public static void main(String[] args) throws IOException {

        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        //创建申明队列
         channel.queueDeclare(QUEUE_NAME, false, false, false, null);

         String msg = "发送消息";

         channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        channel.close();
        connection.close();
    }
}
