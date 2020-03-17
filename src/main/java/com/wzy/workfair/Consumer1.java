package com.wzy.workfair;

import com.rabbitmq.client.*;
import com.wzy.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 类功能说明: 工作队列 应答的方式 每次消费消息时在
 *             完成消息时应答，从进行下一消费
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
        final Channel channel = connection.createChannel();

        // 申明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //一次应答一个
        channel.basicQos(1);

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
                }finally {
                    //手动回执
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //自动 true为自动 false 为手动应答
        boolean autoAck = false;
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
