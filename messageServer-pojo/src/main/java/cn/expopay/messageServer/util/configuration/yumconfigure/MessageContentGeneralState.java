package cn.expopay.messageServer.util.configuration.yumconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "messageContent.generalState")
public class MessageContentGeneralState {

    private int generalStateOne;
    private int generalStateTwo;
    private int generalStateThree;
    private int generalStateFour;

    public int getGeneralStateOne() {
        return generalStateOne;
    }

    public void setGeneralStateOne(int generalStateOne) {
        this.generalStateOne = generalStateOne;
    }

    public int getGeneralStateTwo() {
        return generalStateTwo;
    }

    public void setGeneralStateTwo(int generalStateTwo) {
        this.generalStateTwo = generalStateTwo;
    }

    public int getGeneralStateThree() {
        return generalStateThree;
    }

    public void setGeneralStateThree(int generalStateThree) {
        this.generalStateThree = generalStateThree;
    }

    public int getGeneralStateFour() {
        return generalStateFour;
    }

    public void setGeneralStateFour(int generalStateFour) {
        this.generalStateFour = generalStateFour;
    }

    @Override
    public String toString() {
        return "MessageContentGeneralState{" +
                "generalStateOne=" + generalStateOne +
                ", generalStateTwo=" + generalStateTwo +
                ", generalStateThree=" + generalStateThree +
                ", generalStateFour=" + generalStateFour +
                '}';
    }
}
