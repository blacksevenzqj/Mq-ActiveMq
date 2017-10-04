package cn.expopay.messageServer.model.back;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BackMessage implements Serializable {

    private String sendId;
    private String backTime;
    private int code;
    private String msg;
    private String content;
    private String signBackMq;

    @NotNull(message="sendId不能为null")
    @NotBlank(message="sendId不能为包含空格的空字符串")
    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    @NotNull(message="backTime不能为null")
    @NotBlank(message="backTime不能为包含空格的空字符串")
    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    @NotNull(message="code不能为null")
    @NotBlank(message="code不能为包含空格的空字符串")
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @NotNull(message="msg不能为null")
    @NotBlank(message="msg不能为包含空格的空字符串")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @NotNull(message="signBackMq不能为null")
    @NotBlank(message="signBackMq不能为包含空格的空字符串")
    public String getSignBackMq() {
        return signBackMq;
    }

    public void setSignBackMq(String signBackMq) {
        this.signBackMq = signBackMq;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BackMessage{" +
                "sendId='" + sendId + '\'' +
                ", backTime='" + backTime + '\'' +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", content='" + content + '\'' +
                ", signBackMq='" + signBackMq + '\'' +
                '}';
    }
}
