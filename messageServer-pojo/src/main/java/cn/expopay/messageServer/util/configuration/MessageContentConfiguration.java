package cn.expopay.messageServer.util.configuration;

import cn.expopay.messageServer.util.configuration.yumconfigure.MessageContentBack;
import cn.expopay.messageServer.util.configuration.yumconfigure.MessageContentSend;
import cn.expopay.messageServer.util.configuration.yumconfigure.MessageContentGeneralState;
import cn.expopay.messageServer.util.configuration.yumconfigure.MessageContentHttp;
import cn.expopay.messageServer.util.configuration.yumconfigure.MessageContentNotExecut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "messageContentConfiguration")
@EnableConfigurationProperties({MessageContentSend.class, MessageContentBack.class, MessageContentGeneralState.class,
        MessageContentHttp.class, MessageContentNotExecut.class})
public class MessageContentConfiguration {

    @Autowired
    private MessageContentSend messageContentSend;

    @Autowired
    private MessageContentBack messageContentBack;

    @Autowired
    private MessageContentGeneralState messageContentGeneralState;

    @Autowired
    private MessageContentHttp messageContentHttp;

    @Autowired
    private MessageContentNotExecut messageContentNotExecut;

    public MessageContentSend getMessageContentSend() {
        return messageContentSend;
    }

    public MessageContentBack getMessageContentBack() {
        return messageContentBack;
    }

    public MessageContentGeneralState getMessageContentGeneralState() {
        return messageContentGeneralState;
    }

    public MessageContentHttp getMessageContentHttp() {
        return messageContentHttp;
    }

    public MessageContentNotExecut getMessageContentNotExecut() {
        return messageContentNotExecut;
    }
}
