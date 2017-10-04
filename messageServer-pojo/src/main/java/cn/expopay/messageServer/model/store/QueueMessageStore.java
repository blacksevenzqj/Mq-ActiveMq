package cn.expopay.messageServer.model.store;

import cn.expopay.messageServer.model.queue.QueueMessage;
import cn.expopay.messageServer.model.returninfo.ReturnMq;
import cn.expopay.messageServer.model.returninfo.ReturnT;
import cn.expopay.messageServer.model.returninfo.ReturnSender;

import java.io.Serializable;

public class QueueMessageStore implements Serializable {

    private long id;
    private QueueMessage queueMessage;
    private ReturnMq returnRequest;
    private long delayValue;
    private ReturnT returnOne;
    private ReturnT returnAgain;
    private String backTime;
    private String backAgainTime;
    private int currentBackNum;
    private long delayBackValue;
    private ReturnSender returnBack;
    private ReturnSender returnBackAgain;
    private String endTime;
    // 1:首次发送阶段，2:重试阶段，3:首次回复阶段，4:重试回复阶段
    private int currentStage;
    // 消息发送：1:未结束，2:成功结束，3:失败结束
    private int processEndSend;
    // 消息回复：1:未结束，2:成功结束，3:失败结束
    private int processEndBack;
    private String updateTime;
    // 操作数据库临时ID
    private String tempId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public QueueMessage getQueueMessage() {
        return queueMessage;
    }

    public void setQueueMessage(QueueMessage queueMessage) {
        this.queueMessage = queueMessage;
    }

    public long getDelayValue() {
        return delayValue;
    }

    public void setDelayValue(long delayValue) {
        this.delayValue = delayValue;
    }

    public ReturnT getReturnOne() {
        return returnOne;
    }

    public void setReturnOne(ReturnT returnOne) {
        this.returnOne = returnOne;
    }

    public ReturnT getReturnAgain() {
        return returnAgain;
    }

    public void setReturnAgain(ReturnT returnAgain) {
        this.returnAgain = returnAgain;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getBackAgainTime() {
        return backAgainTime;
    }

    public void setBackAgainTime(String backAgainTime) {
        this.backAgainTime = backAgainTime;
    }

    public int getCurrentBackNum() {
        return currentBackNum;
    }

    public void setCurrentBackNum(int currentBackNum) {
        this.currentBackNum = currentBackNum;
    }

    public long getDelayBackValue() {
        return delayBackValue;
    }

    public void setDelayBackValue(long delayBackValue) {
        this.delayBackValue = delayBackValue;
    }

    public ReturnSender getReturnBack() {
        return returnBack;
    }

    public void setReturnBack(ReturnSender returnBack) {
        this.returnBack = returnBack;
    }

    public ReturnSender getReturnBackAgain() {
        return returnBackAgain;
    }

    public void setReturnBackAgain(ReturnSender returnBackAgain) {
        this.returnBackAgain = returnBackAgain;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getProcessEndSend() {
        return processEndSend;
    }

    public void setProcessEndSend(int processEndSend) {
        this.processEndSend = processEndSend;
    }

    public int getProcessEndBack() {
        return processEndBack;
    }

    public void setProcessEndBack(int processEndBack) {
        this.processEndBack = processEndBack;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public ReturnMq getReturnRequest() {
        return returnRequest;
    }
    public void setReturnRequest(ReturnMq returnRequest) {
        this.returnRequest = returnRequest;
    }

    @Override
    public String toString() {
        return "QueueMessageStore{" +
                "id=" + id +
                ", queueMessage=" + queueMessage +
                ", returnRequest=" + returnRequest +
                ", delayValue=" + delayValue +
                ", returnOne=" + returnOne +
                ", returnAgain=" + returnAgain +
                ", backTime='" + backTime + '\'' +
                ", backAgainTime='" + backAgainTime + '\'' +
                ", currentBackNum=" + currentBackNum +
                ", delayBackValue=" + delayBackValue +
                ", returnBack=" + returnBack +
                ", returnBackAgain=" + returnBackAgain +
                ", endTime='" + endTime + '\'' +
                ", currentStage=" + currentStage +
                ", processEndSend=" + processEndSend +
                ", processEndBack=" + processEndBack +
                ", updateTime='" + updateTime + '\'' +
                ", tempId='" + tempId + '\'' +
                '}';
    }
}
