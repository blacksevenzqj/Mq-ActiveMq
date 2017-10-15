package cn.expopay.messageServer.util.configuration.interfice;

public interface IMessageContent {

    /**
     * 消息队列名称
     */
    public final static String QueueOne = "queueOne";
    public final static String QueueAgain = "queueAgain";
    public final static String QueueBack = "queueBack";
    public final static String QueueBackAgain = "queueBackAgain";

    /**
     * MQ的RSA名称
     */
    public final static String MqRsaId = "public";

    /**
     * Send
     */
    public static final int SendMessageTotalNum = 10;
    public static final int SendMessageTotalBackNum = 10;

    public static final String SendMessageSucess = "请求消息服务系统：成功";
    public static final String SendMessageBackSignFail = "请求消息服务系统：返回加签失败";

    /**
     * Back
     */
    public static final String BackMessageSignFail = "消息服务系统：回复加签失败";
    public static final String BackMessageKeyVersion = "消息服务系统：回复消息失败，keyVersion版本是 ";
    public static final String BackMessageFromSenderSignatureFailStr = "消息中间件系统：接收Sender返回信息验签失败";

    /**
     * GeneralState
     */
    public static final int GeneralStateOne = 1;
    public static final int GeneralStateTwo = 2;
    public static final int GeneralStateThree = 3;
    public static final int GeneralStateFour = 4;

    /**
     * Http
     */
    public static final int ConnectionRequestTimeout = 1000;
    public static final int SocketTimeout = 1000;
    public static final int ConnectTimeout = 1000;

    public static final int HttpCodeSucess = 200;
    public static final int HttpCodeFail = 500;

    /**
     * 不继续执行
     */
    // MIMIE请求类型错误
    public static final int HttpCodeUnMediaType = 415;
    public static final int HttpSendRequestParameterError = 10001;
    public static final String HttpSendRequestParameterErrorStr = "HttpManagerClient sendRequest parameter is error ";
    // 请求类型错误
    public static final int HttpSendRequestTypeError = 10002;
    public static final String HttpSendRequestTypeErrorStr = "HttpManagerClient sendRequest parameter requestMode is error : NotMatch RequestType";

    // 回复消息：参数错误
    public static final int HttpBackRequestParameterError = 10003;
    // 回复消息：消息中间件系统：接收Sender返回信息验签失败
    public static final int BackMessageFromSenderSignatureError = 10004;
    // 回复消息：发送方返回的为非JSON格式数据
    public static final int BackMessageFromSenderResponseNotJSON = 10005;
    // 回复消息：请求返回实体为Null
    public static final int BackMessageFromSenderHttpEntityIsNull = 10006;

    // 验签失败
    public static final int SendMessageSignatureFail = 10007;
    public static final String SendMessageSignatureFailStr = "请求消息服务系统：验签失败";

    public static final int SendMessageKeyVersionIsError = 10008;
    public static final String SendMessageKeyVersionIsErrorStr = "keyVersion 版本号异常，没有与之对应的版本号，请检查核对";
    public static final String ReturnSignParamterLastErrorStr = "返回加签：keyVersion 版本号异常，没有与之对应的版本号，请检查核对";

    public static final int SendMessageKeyIsNull = 10009;
    public static final String SendMessageKeyIsNullStr = "KeyVersion 参数为Null 或 空字符串";
}

