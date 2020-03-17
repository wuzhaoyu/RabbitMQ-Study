package com.wzy.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wzy.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/3/16
 * 修改说明
 *  队列为轮询的方式 round-robin 消费者睡眠的时间不影响队列轮询的效果
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
         channel.queueDeclare(QUEUE_NAME, false, false, false, null);

         for (int i=0; i< 50 ;i++){
             String msg = "发送消息" + i;
             channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
         }

        channel.close();
        connection.close();
    }
}
