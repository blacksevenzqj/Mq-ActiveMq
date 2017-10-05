package cn.expopay.messageServer.controller;

import cn.expopay.messageServer.Interface.dbservce.IMessageService;
import cn.expopay.messageServer.model.back.BackMessage;
import cn.expopay.messageServer.model.queue.QueueMessage;
import cn.expopay.messageServer.model.returninfo.ReturnT;
import cn.expopay.messageServer.model.rsa.RsaConfigModel;
import cn.expopay.messageServer.model.send.SendMessage;
import cn.expopay.messageServer.model.store.QueueMessageStore;
import cn.expopay.messageServer.model.returninfo.ReturnSender;
import cn.expopay.messageServer.util.encryption.RsaParameterValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class TestControl {

    private Logger logger = LoggerFactory.getLogger(TestControl.class);

    @Autowired
    IMessageService messageService;

    //模拟第三个系统：消息接收方，接收消息
    @RequestMapping(value = "/queuemq/receive")
    @ResponseBody
//    public String queueMqReceive(@RequestBody OrderInfo orderInfo) {  //Post Json
//    public String queueMqReceive(OrderInfo orderInfo) {  //Post Form
    public String queueMqReceive(String dataContent) {  //Get请求
//        logger.info("queueMqReceive is " + orderInfo);
        logger.info("queueMqReceive is " + dataContent);
        try {
            Thread.currentThread().sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "ActiveMqReceive is good";
//        return JacksonUtil.writeValueAsString(ReturnT.SUCCESS);
    }

    //消息回复：模拟第一个系统：消息发送方，接收消息回复。
    @RequestMapping(value = "/queuemq/back", method = RequestMethod.POST)
    @ResponseBody
    public ReturnSender queueMqBack (@RequestBody BackMessage backMessage) {
        logger.info("backMessage is " + backMessage);
        ReturnSender rs = null;
        try {
            RsaConfigModel rsaConfigModel = RsaParameterValidation.getRsaProperties("one");
            rs = RsaParameterValidation.checkParamterBackSender(backMessage, rsaConfigModel.getPublicKey(), rsaConfigModel.getPrivateKey());
//            Thread.currentThread().sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    @RequestMapping(value = "/queuemq/myTestInsert")
    public String myTestInsert(HttpServletRequest request) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setSendId("9999999999999999999999");
        sendMessage.setBackUrl("www.baidu.com");
        sendMessage.setDataContent("asdasdasda");
        sendMessage.setHeaderContent("lllllllllllllll");
        sendMessage.setKeyVersion("1");
        sendMessage.setSignSendSender("ppppppppppppppppppssssssssssssssssssssssssssssssssss");
        sendMessage.setReceptionUrl("www.sina.com");
        sendMessage.setReqSucessMatch("very good");
        sendMessage.setTotalNum(6);
        sendMessage.setTotalBackNum(3);
        sendMessage.setSendTime(new Date().toString());

        QueueMessage queueMessage = new QueueMessage();
        queueMessage.setSendMessage(sendMessage);
        queueMessage.setAgainTime(new Date().toString());
        queueMessage.setCurrentNum(3);

        QueueMessageStore queueMessageStore = new QueueMessageStore();
        queueMessageStore.setQueueMessage(queueMessage);
        queueMessageStore.setCurrentStage(2);
        queueMessageStore.setDelayValue(200000);
        queueMessageStore.setProcessEndSend(2);

        ReturnT returnT = new ReturnT();
        returnT.setCode(300);
        returnT.setMsg("99999999");
        returnT.setContent("3333333");

        queueMessageStore.setReturnOne(returnT);
        queueMessageStore.setReturnAgain(returnT);

        queueMessageStore.setBackTime(new Date().toString());
        queueMessageStore.setBackAgainTime(new Date().toString());
        queueMessageStore.setCurrentBackNum(3);
        queueMessageStore.setDelayBackValue(20000);

        ReturnSender returnSender = new ReturnSender();
        returnSender.setCode(300);
        returnSender.setMsg("回復消息。。。");
        returnSender.setContent("啦啦啦啦綠綠綠綠綠綠綠綠綠");
        returnSender.setSignBackSender("ssasdasdllllllllllllkkkkkkkkkkkkkkksssbbbbbbbbbbb");
        queueMessageStore.setReturnBack(returnSender);
        queueMessageStore.setReturnBackAgain(returnSender);

        queueMessageStore.setEndTime(new Date().toString());

        messageService.insertMessageInfo(queueMessageStore);

//        messageService.updateMessageInfo(queueMessageStore);

        return "测试Inster";
    }


    @RequestMapping(value = "/queuemq/myTestRead")
    public String myTestRead(HttpServletRequest request) {
        return "123";
    }


}
