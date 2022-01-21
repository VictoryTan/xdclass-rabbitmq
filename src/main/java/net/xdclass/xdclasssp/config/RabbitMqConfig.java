package net.xdclass.xdclasssp.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "order_exchange";

    public static final String QUEUE = "order_queue";

    @Bean
    public Exchange orderExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean Queue orderQueue(){
        return QueueBuilder.durable(QUEUE).build();
    }

    /**
     * 交换机和队列绑定
     */
    @Bean
    public Binding orderBinding(Queue queue,Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("order.#").noargs();
    }

}