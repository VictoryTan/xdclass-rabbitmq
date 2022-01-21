package net.xdclass.xdclasssp;

import net.xdclass.xdclasssp.config.RabbitMqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XdclassSpApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testSend() {
        for (int i = 0; i < 5; i++) {

            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "order.new", "你好"+i);
        }

    }

}
