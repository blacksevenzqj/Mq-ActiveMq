package cn.expopay.messageServer.model.send;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发送消息流转类
 */
public class SendMessage implements Serializable {

    private String sendId;
    private String sendTime;
    private String receptionUrl;
    private String backUrl;
    private String dataContent;
    private String headerContent;
    private int totalNum;
    private int totalBackNum;
    private int requestMode;
    private String reqSucessMatch;
    private String keyVersion;
    private String signSendSender;

    @NotNull(message="sendId不能为null")
    @NotBlank(message="sendId不能为包含空格的空字符串")
    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    @NotNull(message="sendTime不能为null")
    @NotBlank(message="sendTime不能为包含空格的空字符串")
    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @NotNull(message="receptionUrl不能为null")
    @NotBlank(message="receptionUrl不能为包含空格的空字符串")
    public String getReceptionUrl() {
        return receptionUrl;
    }

    public void setReceptionUrl(String receptionUrl) {
        this.receptionUrl = receptionUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }
    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    @NotNull(message="dataContent不能为null")
    @NotBlank(message="dataContent不能为包含空格的空字符串")
    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getHeaderContent() {
        return headerContent;
    }
    public void setHeaderContent(String headerContent) {
        this.headerContent = headerContent;
    }

    @NotNull(message="reqSucessMatch不能为null")
    @NotBlank(message="reqSucessMatch不能为包含空格的空字符串")
    public String getReqSucessMatch() {
        return reqSucessMatch;
    }

    public void setReqSucessMatch(String reqSucessMatch) {
        this.reqSucessMatch = reqSucessMatch;
    }

    @NotNull(message="signSendSender不能为null")
    @NotBlank(message="signSendSender不能为包含空格的空字符串")
    public String getSignSendSender() {
        return signSendSender;
    }

    public void setSignSendSender(String signSendSender) {
        this.signSendSender = signSendSender;
    }

    @NotNull(message="keyVersion不能为null")
    @NotBlank(message="keyVersion不能为包含空格的空字符串")
    public String getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(String keyVersion) {
        this.keyVersion = keyVersion;
    }

    public int getTotalNum() {
        return totalNum;
    }
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalBackNum() {
        return totalBackNum;
    }
    public void setTotalBackNum(int totalBackNum) {
        this.totalBackNum = totalBackNum;
    }

    public int getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(int requestMode) {
        this.requestMode = requestMode;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "sendId='" + sendId + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", receptionUrl='" + receptionUrl + '\'' +
                ", backUrl='" + backUrl + '\'' +
                ", dataContent='" + dataContent + '\'' +
                ", headerContent='" + headerContent + '\'' +
                ", totalNum=" + totalNum +
                ", totalBackNum=" + totalBackNum +
                ", requestMode=" + requestMode +
                ", reqSucessMatch='" + reqSucessMatch + '\'' +
                ", keyVersion='" + keyVersion + '\'' +
                ", signSendSender='" + signSendSender + '\'' +
                '}';
    }
}
