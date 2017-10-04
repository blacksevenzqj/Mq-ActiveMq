package cn.expopay.messageServer.model.returninfo;


public class ReturnSender extends ReturnT {

    private String signBackSender;

    public ReturnSender() {
    }

    public ReturnSender(int code, String msg) {
        super(code, msg);
    }

    public ReturnSender(int code, String msg, String signBackSender) {
        super(code, msg);
        this.signBackSender = signBackSender;
    }

    public String getSignBackSender() {
        return signBackSender;
    }

    public void setSignBackSender(String signBackSender) {
        this.signBackSender = signBackSender;
    }

    @Override
    public String toString() {
        return "ReturnSender{" +
                "signBackSender='" + signBackSender + '\'' +
                '}';
    }
}
