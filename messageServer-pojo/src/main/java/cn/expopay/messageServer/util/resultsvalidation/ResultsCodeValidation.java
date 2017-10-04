package cn.expopay.messageServer.util.resultsvalidation;

import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;

public class ResultsCodeValidation {

    public static boolean sendRsultsCodeValidation(int callbackResult){
        return callbackResult == IMessageContent.HttpCodeUnMediaType || callbackResult == IMessageContent.HttpSendRequestParameterError ||
                callbackResult == IMessageContent.HttpSendRequestTypeError;
    }

    public static boolean backRsultsCodeValidation(int callbackResult){
        return callbackResult == IMessageContent.HttpCodeUnMediaType || callbackResult == IMessageContent.HttpBackRequestParameterError ||
                callbackResult == IMessageContent.BackMessageFromSenderSignatureError || callbackResult == IMessageContent.BackMessageFromSenderResponseNotJSON;
    }

}
