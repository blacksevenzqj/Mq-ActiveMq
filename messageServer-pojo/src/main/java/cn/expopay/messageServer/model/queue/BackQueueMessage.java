package cn.expopay.messageServer.model.queue;

import cn.expopay.messageServer.model.back.BackMessage;

import java.io.Serializable;

/**
 * 回复消息流转类
 */
public class BackQueueMessage implements Serializable {

    private BackMessage backMessage;
    private String backUrl;
    private String keyVersion;
    private String backAgainTime;
    private int totalBackNum;
    private int currentBackNum;

    public BackMessage getBackMessage() {
        return backMessage;
    }

    public void setBackMessage(BackMessage backMessage) {
        this.backMessage = backMessage;
    }

    public int getTotalBackNum() {
        return totalBackNum;
    }

    public void setTotalBackNum(int totalBackNum) {
        this.totalBackNum = totalBackNum;
    }

    public int getCurrentBackNum() {
        return currentBackNum;
    }

    public void setCurrentBackNum(int currentBackNum) {
        this.currentBackNum = currentBackNum;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getBackAgainTime() {
        return backAgainTime;
    }

    public void setBackAgainTime(String backAgainTime) {
        this.backAgainTime = backAgainTime;
    }

    public String getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(String keyVersion) {
        this.keyVersion = keyVersion;
    }

    @Override
    public String toString() {
        return "BackQueueMessage{" +
                "backMessage=" + backMessage +
                ", backUrl='" + backUrl + '\'' +
                ", keyVersion='" + keyVersion + '\'' +
                ", backAgainTime='" + backAgainTime + '\'' +
                ", totalBackNum=" + totalBackNum +
                ", currentBackNum=" + currentBackNum +
                '}';
    }
}
