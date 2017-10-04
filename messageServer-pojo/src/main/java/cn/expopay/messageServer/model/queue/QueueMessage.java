package cn.expopay.messageServer.model.queue;

import cn.expopay.messageServer.model.send.SendMessage;

import java.io.Serializable;

public class QueueMessage implements Serializable {

    private SendMessage sendMessage;
    private String againTime;
    private int currentNum;

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public String getAgainTime() {
        return againTime;
    }

    public void setAgainTime(String againTime) {
        this.againTime = againTime;
    }

    @Override
    public String toString() {
        return "QueueMessage{" +
                "sendMessage=" + sendMessage +
                ", againTime='" + againTime + '\'' +
                ", currentNum=" + currentNum +
                '}';
    }
}
