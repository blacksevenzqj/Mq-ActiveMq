package cn.expopay.messageServer.util.configuration.yumconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "messageContent.back")
public class MessageContentBack {

    //消息服务系统：回复加签失败
    private String backMessageSignFail;
    //消息服务系统：回复消息失败，keyVersion版本是
    private String backMessageKeyVersion;
    // 消息中间件系统：接收Sender返回信息验签失败
    private String backMessageFromSenderSignatureFailStr;

    public String getBackMessageSignFail() {
        return backMessageSignFail;
    }

    public void setBackMessageSignFail(String backMessageSignFail) {
        this.backMessageSignFail = backMessageSignFail;
    }

    public String getBackMessageKeyVersion() {
        return backMessageKeyVersion;
    }

    public void setBackMessageKeyVersion(String backMessageKeyVersion) {
        this.backMessageKeyVersion = backMessageKeyVersion;
    }

    public String getBackMessageFromSenderSignatureFailStr() {
        return backMessageFromSenderSignatureFailStr;
    }

    public void setBackMessageFromSenderSignatureFailStr(String backMessageFromSenderSignatureFailStr) {
        this.backMessageFromSenderSignatureFailStr = backMessageFromSenderSignatureFailStr;
    }

    @Override
    public String toString() {
        return "MessageContentBack{" +
                "backMessageSignFail='" + backMessageSignFail + '\'' +
                ", backMessageKeyVersion='" + backMessageKeyVersion + '\'' +
                ", backMessageFromSenderSignatureFailStr='" + backMessageFromSenderSignatureFailStr + '\'' +
                '}';
    }
}
