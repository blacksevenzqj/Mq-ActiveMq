package cn.expopay.messageServer.util.configuration.yumconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "messageContent.notExecut")
public class MessageContentNotExecut {

    private int httpCodeUnMediaType;
    private int httpSendRequestParameterError;
    private String httpSendRequestParameterErrorStr;
    private int httpSendRequestTypeError;
    private String httpSendRequestTypeErrorStr;
    private int httpBackRequestParameterError;
    private int backMessageFromSenderSignatureError;
    private int backMessageFromSenderResponseNotJSON;
    private int backMessageFromSenderHttpEntityIsNull;
    private int sendMessageSignatureFail;
    private String sendMessageSignatureFailStr;
    private int sendMessageKeyVersionIsError;
    private String sendMessageKeyVersionIsErrorStr;
    private String returnSignParamterLastErrorStr;

    public int getHttpCodeUnMediaType() {
        return httpCodeUnMediaType;
    }

    public void setHttpCodeUnMediaType(int httpCodeUnMediaType) {
        this.httpCodeUnMediaType = httpCodeUnMediaType;
    }

    public int getHttpSendRequestParameterError() {
        return httpSendRequestParameterError;
    }

    public void setHttpSendRequestParameterError(int httpSendRequestParameterError) {
        this.httpSendRequestParameterError = httpSendRequestParameterError;
    }

    public String getHttpSendRequestParameterErrorStr() {
        return httpSendRequestParameterErrorStr;
    }

    public void setHttpSendRequestParameterErrorStr(String httpSendRequestParameterErrorStr) {
        this.httpSendRequestParameterErrorStr = httpSendRequestParameterErrorStr;
    }

    public int getHttpSendRequestTypeError() {
        return httpSendRequestTypeError;
    }

    public void setHttpSendRequestTypeError(int httpSendRequestTypeError) {
        this.httpSendRequestTypeError = httpSendRequestTypeError;
    }

    public String getHttpSendRequestTypeErrorStr() {
        return httpSendRequestTypeErrorStr;
    }

    public void setHttpSendRequestTypeErrorStr(String httpSendRequestTypeErrorStr) {
        this.httpSendRequestTypeErrorStr = httpSendRequestTypeErrorStr;
    }

    public int getHttpBackRequestParameterError() {
        return httpBackRequestParameterError;
    }

    public void setHttpBackRequestParameterError(int httpBackRequestParameterError) {
        this.httpBackRequestParameterError = httpBackRequestParameterError;
    }

    public int getBackMessageFromSenderSignatureError() {
        return backMessageFromSenderSignatureError;
    }

    public void setBackMessageFromSenderSignatureError(int backMessageFromSenderSignatureError) {
        this.backMessageFromSenderSignatureError = backMessageFromSenderSignatureError;
    }

    public int getBackMessageFromSenderResponseNotJSON() {
        return backMessageFromSenderResponseNotJSON;
    }

    public void setBackMessageFromSenderResponseNotJSON(int backMessageFromSenderResponseNotJSON) {
        this.backMessageFromSenderResponseNotJSON = backMessageFromSenderResponseNotJSON;
    }

    public int getBackMessageFromSenderHttpEntityIsNull() {
        return backMessageFromSenderHttpEntityIsNull;
    }

    public void setBackMessageFromSenderHttpEntityIsNull(int backMessageFromSenderHttpEntityIsNull) {
        this.backMessageFromSenderHttpEntityIsNull = backMessageFromSenderHttpEntityIsNull;
    }

    public int getSendMessageSignatureFail() {
        return sendMessageSignatureFail;
    }

    public void setSendMessageSignatureFail(int sendMessageSignatureFail) {
        this.sendMessageSignatureFail = sendMessageSignatureFail;
    }

    public String getSendMessageSignatureFailStr() {
        return sendMessageSignatureFailStr;
    }

    public void setSendMessageSignatureFailStr(String sendMessageSignatureFailStr) {
        this.sendMessageSignatureFailStr = sendMessageSignatureFailStr;
    }

    public int getSendMessageKeyVersionIsError() {
        return sendMessageKeyVersionIsError;
    }

    public void setSendMessageKeyVersionIsError(int sendMessageKeyVersionIsError) {
        this.sendMessageKeyVersionIsError = sendMessageKeyVersionIsError;
    }

    public String getSendMessageKeyVersionIsErrorStr() {
        return sendMessageKeyVersionIsErrorStr;
    }

    public void setSendMessageKeyVersionIsErrorStr(String sendMessageKeyVersionIsErrorStr) {
        this.sendMessageKeyVersionIsErrorStr = sendMessageKeyVersionIsErrorStr;
    }

    public String getReturnSignParamterLastErrorStr() {
        return returnSignParamterLastErrorStr;
    }

    public void setReturnSignParamterLastErrorStr(String returnSignParamterLastErrorStr) {
        this.returnSignParamterLastErrorStr = returnSignParamterLastErrorStr;
    }

    @Override
    public String toString() {
        return "MessageContentNotExecut{" +
                "httpCodeUnMediaType=" + httpCodeUnMediaType +
                ", httpSendRequestParameterError=" + httpSendRequestParameterError +
                ", httpSendRequestParameterErrorStr='" + httpSendRequestParameterErrorStr + '\'' +
                ", httpSendRequestTypeError=" + httpSendRequestTypeError +
                ", httpSendRequestTypeErrorStr='" + httpSendRequestTypeErrorStr + '\'' +
                ", httpBackRequestParameterError=" + httpBackRequestParameterError +
                ", backMessageFromSenderSignatureError=" + backMessageFromSenderSignatureError +
                ", backMessageFromSenderResponseNotJSON=" + backMessageFromSenderResponseNotJSON +
                ", backMessageFromSenderHttpEntityIsNull=" + backMessageFromSenderHttpEntityIsNull +
                ", sendMessageSignatureFail=" + sendMessageSignatureFail +
                ", sendMessageSignatureFailStr='" + sendMessageSignatureFailStr + '\'' +
                ", sendMessageKeyVersionIsError=" + sendMessageKeyVersionIsError +
                ", sendMessageKeyVersionIsErrorStr='" + sendMessageKeyVersionIsErrorStr + '\'' +
                ", returnSignParamterLastErrorStr='" + returnSignParamterLastErrorStr + '\'' +
                '}';
    }
}
