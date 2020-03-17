package com.wzy.workfair;

import com.rabbitmq.client.*;
import com.wzy.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 类功能说明: 工作队列
 * 类修改者	创建日期2020/3/16
 * 修改说明 ：    | 消费者1
 *     生产 -----|
 *               | 消费者2
 *
 *     注意 1.一定要将自动应答修改手动应答
 *          2. Message Acknowledgment 消息确认
 *             一： rabbitMQ 默认队列会将消息分发给消费者，与此同时会删除在队列中的消息（存在内存中）
 *              但是消费者是否成功消费消息，改消息都会在内存中删除。这就导致消息可能丢失问题
 *              解决方式 一： 将autoAck修改为fase 手动应答。但是队列中消息分发给消费者时，队列此时消息
 *              并不会删除，但消费应答后删除。
 *              二： 将数据持久化在本地 durable在本地
 * @author wzy
 * @version V1.0
 **/
public class Consumer {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();

        //获取通道
        //在内部类当中不能引用本地变量s,需要被声明为常量
        final Channel channel = connection.createChannel();

        // 申明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //一次应答一个
        channel.basicQos(1);

        //定义队列消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            //消息到达触发回调方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("接收消息" + msg);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //手动应答 给队列 进行消息下次消息消费
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME,autoAck,defaultConsumer);

    }

}
