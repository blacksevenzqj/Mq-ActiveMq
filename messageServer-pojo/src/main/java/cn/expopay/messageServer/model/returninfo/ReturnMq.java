package cn.expopay.messageServer.model.returninfo;


public class ReturnMq extends ReturnT {

    private String signSendMq;

    public ReturnMq() {
    }

    public ReturnMq(int code, String msg) {
        super(code, msg);
    }

    public ReturnMq(int code, String msg, String signSendMq) {
        super(code, msg);
        this.signSendMq = signSendMq;
    }

    public String getSignSendMq() {
        return signSendMq;
    }

    public void setSignSendMq(String signSendMq) {
        this.signSendMq = signSendMq;
    }

    @Override
    public String toString() {
        return "ReturnMq{" +
                "signSendMq='" + signSendMq + '\'' +
                '}';
    }
}
