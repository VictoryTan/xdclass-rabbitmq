package net.xdclass.xdclasssp;

import net.xdclass.xdclasssp.config.RabbitMqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XdclassSpApplicationTests {

    @Autowired
    private RabbitTemplate template;


    @Test
    void testSend() {

        //for(int i=0;i<5;i++){
        template.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "order.new","新订单777");
        //}

    }


    /**
     * 生产者到交换机可靠性投递测试
     */
    @Test
    void testConfirmCallback(){

        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            /**
             * @param correlationData 配置
             * @param ack 交换机是否收到消息，true是成功，false是失败
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("ConfirmCallback======>");
                System.out.println("correlationData======>correlationData="+correlationData);
                System.out.println("ack======>ack="+ack);
                System.out.println("cause======>cause="+cause);

                if(ack){
                    System.out.println("发送成功");
                    //更新数据库 消息的状态为成功  TODO
                }else {
                    System.out.println("发送失败，记录到日志或者数据库");
                    //更新数据库 消息的状态为失败  TODO
                }

            }
        });

        //数据库新增一个消息记录，状态是发送  TODO

        //发送消息
        //template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "order.new","新订单");
        //模拟异常
        template.convertAndSend(RabbitMqConfig.EXCHANGE_NAME+" xdclass", "order.new","新订单");

    }


    /**
     * 交换机到队列可靠性投递测试
     */
    @Test
    void testReturnCallback(){

        template.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {

            @Override
            public void returnedMessage(ReturnedMessage returned) {
                int code = returned.getReplyCode();
                System.out.println("code="+code);
                System.out.println("returned="+returned.toString());

            }
        });

        //template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "order.new","新订单ReturnsCallback");
        //模拟异常
        template.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "xdclass.order.new","新订单ReturnsCallback");

    }

}
