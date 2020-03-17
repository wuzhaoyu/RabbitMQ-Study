package com.wzy.workfair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wzy.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/3/16
 * 修改说明
 *  队列为 公平分发（fair dispatch）应答的方式 每次消费消息时在
 *  *             完成消息时应答，从进行下一消费
 *  解决 ：由于业务中按照效率进行消息的分发 而不是一致的为轮询方式
 *
 * @author wzy
 * @version V1.0
 **/
public class Send {
    /**
     *                   | 消费者1
     *  *     生产 -----|
     *  *               | 消费者2
     */
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException {

        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        //创建申明队列
        // 防止rabbit服务停止 使用持久化机制 durable 将channel中消息修改持久化 true（第二个参数）
        // 注意：已经申请过存在的队列不能在重新进行申请持久化 。可将其删除或者重新申请队列
         channel.queueDeclare(QUEUE_NAME, false, false, false, null);

         for (int i=0; i< 50 ;i++){
             String msg = "发送消息" + i;
             channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
         }

        channel.close();
        connection.close();
    }
}
