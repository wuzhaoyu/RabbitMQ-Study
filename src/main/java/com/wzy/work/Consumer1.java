package com.wzy.work;

import com.rabbitmq.client.*;
import com.wzy.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 类功能说明: 工作队列
 * 类修改者	创建日期2020/3/16
 * 修改说明 ：    | 消费者1
 *     生产 -----|
 *               | 消费者2
 * @author wzy
 * @version V1.0
 **/
public class Consumer1 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();

        //获取通道
        Channel channel = connection.createChannel();

        // 申明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //定义队列消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("接收消息" + msg);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME,autoAck,defaultConsumer);

    }
    public  void oldMethod() throws IOException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        //过期的方法

        //定义队列消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        channel.basicConsume(QUEUE_NAME,true,queueingConsumer);

        while (true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));
        }

    }
}
