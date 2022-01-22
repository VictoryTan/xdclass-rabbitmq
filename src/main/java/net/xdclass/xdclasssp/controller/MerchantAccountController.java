package net.xdclass.xdclasssp.controller;

import net.xdclass.xdclasssp.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/merchant")
public class MerchantAccountController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("check")
    public Object check(){

        //修改数据库的商家账号状态  TODO

        rabbitTemplate.convertAndSend(RabbitMqConfig.NEW_MERCHART_EXCHANGE,RabbitMqConfig.NEW_MERCHART_ROUTING_KEY,"商家账号通过审核");

        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","账号审核通过，请10秒内上传1个商品");
        return map;
    }

}
