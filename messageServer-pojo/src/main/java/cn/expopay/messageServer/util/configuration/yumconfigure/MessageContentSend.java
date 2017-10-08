package cn.expopay.messageServer.util.configuration.yumconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "messageContent.send")
public class MessageContentSend {

    private int sendMessageTotalNum;
    private int sendMessageTotalBackNum;
    //KeyVersion 参数为Null 或 空字符串
    private String sendMessageKeyIsNull;
    //请求消息服务系统：成功
    private String sendMessageSucess;
    //请求消息服务系统：返回加签失败
    private String sendMessageBackSignFail;

    public int getSendMessageTotalNum() {
        return sendMessageTotalNum;
    }

    public void setSendMessageTotalNum(int sendMessageTotalNum) {
        this.sendMessageTotalNum = sendMessageTotalNum;
    }

    public int getSendMessageTotalBackNum() {
        return sendMessageTotalBackNum;
    }

    public void setSendMessageTotalBackNum(int sendMessageTotalBackNum) {
        this.sendMessageTotalBackNum = sendMessageTotalBackNum;
    }

    public String getSendMessageKeyIsNull() {
        return sendMessageKeyIsNull;
    }

    public void setSendMessageKeyIsNull(String sendMessageKeyIsNull) {
        this.sendMessageKeyIsNull = sendMessageKeyIsNull;
    }

    public String getSendMessageSucess() {
        return sendMessageSucess;
    }

    public void setSendMessageSucess(String sendMessageSucess) {
        this.sendMessageSucess = sendMessageSucess;
    }

    public String getSendMessageBackSignFail() {
        return sendMessageBackSignFail;
    }

    public void setSendMessageBackSignFail(String sendMessageBackSignFail) {
        this.sendMessageBackSignFail = sendMessageBackSignFail;
    }
}
