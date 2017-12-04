package cn.expopay.messageServer.controller;

import cn.expopay.messageServer.model.returninfo.ReturnMq;
import cn.expopay.messageServer.model.send.SendMessage;
import cn.expopay.messageServer.service.IBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class QueueControl {

    private Logger logger = LoggerFactory.getLogger(QueueControl.class);

    @Autowired
    IBusinessService businessService;

    /**
     * 1、如果请求类型错误，如：使用POST的KV方式，则不会进入方法，而是由框架直接返回错误："status":415,"error":"Unsupported Media Type"给调用方。
     * 2、如果请求参数JSON字符串为""空字符串，则不会进入方法，而是由框架直接返回错误：Required request body is missing
     * 3、如果请求参数JOSN字符串为" "空格的空字符串，则不会进入方法，而是由框架直接返回错误：No content to map due to end-of-input(JackSon )
     */
    @RequestMapping(value = "/queuemq/send1", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMq queueMqSend1 (@RequestBody SendMessage sendMessage) {
        logger.info("QueueControl are SendMessage is " + sendMessage);
//        ReturnMq rmq = businessService.sendMessageOne(sendMessage);
//        return rmq;
        return null;
    }


    // K V方式1：
    @RequestMapping(value = "/queuemq/send2", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMq queueMqSend2 (String jsonParam) {
        logger.info("QueueControl are jsonParam is " + jsonParam);
//        ReturnMq rmq = businessService.sendMessageOne(sendMessage);
        return null;
    }
    // K V方式2：
    @RequestMapping(value = "/queuemq/send3", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMq queueMqSend3 (SendMessage sendMessage) {
        logger.info("QueueControl are SendMessage is " + sendMessage);
//        ReturnMq rmq = businessService.sendMessageOne(sendMessage);
        return null;
    }

}
