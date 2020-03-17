package com.wzy.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.wzy.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 类功能说明: 消费者
 * 类修改者	创建日期2020/3/16
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 **/
public class Consumer {
    public static final String QUEUE_NAME = "mmr_queue";
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        //定义队列消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        channel.basicConsume(QUEUE_NAME,true,queueingConsumer);

        while (true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));
        }

    }
}
