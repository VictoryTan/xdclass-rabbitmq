package net.xdclass.xdclasssp.config;


import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitMqConfig {

//    public static final String EXCHANGE_NAME = "order_exchange";
//
//    public static final String QUEUE = "order_queue";
//
//    @Bean
//    public Exchange orderExchange(){
//        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
//    }
//
//    @Bean Queue orderQueue(){
//        return QueueBuilder.durable(QUEUE).build();
//    }
//
//    /**
//     * 交换机和队列绑定
//     */
//    @Bean
//    public Binding orderBinding(Queue queue,Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("order.#").noargs();
//    }

    /**
     * 死信队列
     */
    public static final String LOCK_MERCHART_DEAD_QUEUE = "lock_merchart_dead_queue";

    /**
     * 死信交换机
     */
    public static final String LOCK_MERCHART_DEAD_EXCHANGE = "lock_merchart_dead_exchange";

    /**
     * 进入死信队列的路由key
     */
    public static final String LOCK_MERCHART_ROUTING_KEY = "lock_merchart_routing_key";

    /**
     * 创建死信交换机
     */
    @Bean
    public Exchange lockMerchartDeadExchange(){
        return new TopicExchange(LOCK_MERCHART_DEAD_EXCHANGE,true,false);
    }

    /**
     * 创建死信队列
     */
    @Bean
    public Queue lockMerchartDeadQueue(){
        return QueueBuilder.durable(LOCK_MERCHART_DEAD_QUEUE).build();
    }

    @Bean
    public Binding lockMerchantBinding(@Qualifier(value = "lockMerchartDeadExchange")Exchange exchange,@Qualifier(value = "lockMerchartDeadQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(LOCK_MERCHART_ROUTING_KEY).noargs();
    }


    /**
     * 普通队列，绑定的个死信交换机
     */
    public static final String NEW_MERCHART_QUEUE = "new_merchart_queue";

    /**
     * 普通交换机
     */
    public static final String NEW_MERCHART_EXCHANGE = "new_merchart_exchange";

    /**
     * 路由key
     */
    public static final String NEW_MERCHART_ROUTING_KEY = "new_merchart_routing_key";



    /**
     * 创建普通交换机
     */
    @Bean
    public Exchange newMerchartExchange(){
        return new TopicExchange(NEW_MERCHART_EXCHANGE,true,false);
    }

    /**
     * 创建普通队列
     */
    @Bean
    public Queue newMerchartQueue(){

        Map<String,Object> args = new HashMap<>();

        //消息过期后，进入到死信交换机
        args.put("x-dead-letter-exchange",LOCK_MERCHART_DEAD_EXCHANGE);

        //消息过期后，进入到死信交换机的路由key
        args.put("x-dead-letter-routing-key",LOCK_MERCHART_ROUTING_KEY);

        //过期时间，单位毫秒
        args.put("x-message-ttl",10000);


        return QueueBuilder.durable(NEW_MERCHART_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定交换机和队列
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding newMerchantBinding(@Qualifier(value = "newMerchartExchange")Exchange exchange,@Qualifier(value = "newMerchartQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(NEW_MERCHART_ROUTING_KEY).noargs();
    }

}