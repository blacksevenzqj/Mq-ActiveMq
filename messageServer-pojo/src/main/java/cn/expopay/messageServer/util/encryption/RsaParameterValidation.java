package cn.expopay.messageServer.util.encryption;

import cn.expopay.messageServer.model.back.BackMessage;
import cn.expopay.messageServer.model.returninfo.ReturnMq;
import cn.expopay.messageServer.model.returninfo.ReturnSender;
import cn.expopay.messageServer.model.rsa.RsaConfigModel;
import cn.expopay.messageServer.model.send.SendMessage;
import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;
import cn.expopay.messageServer.util.configuration.Initializationconfig.RsaKeyConfig;
import cn.expopay.messageServer.util.validate.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RsaParameterValidation {

    private static Logger logger = LoggerFactory.getLogger(RsaParameterValidation.class);

    public static ReturnMq checkParamter(Object obj){
        ReturnMq rmq = new ReturnMq();
        rmq.setCode(IMessageContent.HttpCodeFail);
        if (obj instanceof SendMessage) {
            RsaConfigModel rsaConfigModel = null;
            SendMessage sendMessage = (SendMessage) obj;
            if (StringUtils.isBlank(sendMessage.getKeyVersion())) {
                rmq.setMsg(IMessageContent.SendMessageKeyIsNull);
                return rmq;
            }
            // 拿取对应的KeyVersion版本号
            String keyVersion = sendMessage.getKeyVersion().toLowerCase();
            rsaConfigModel = getRsaProperties(keyVersion);
            if (rsaConfigModel == null) {
                logger.error("RsaParameterValidation are checkParamter error ");
                rmq.setMsg(IMessageContent.SendMessageKeyVersionIsError);
                return rmq;
            }
            String validateStr = ValidationUtil.validateModel(sendMessage);
            if (StringUtils.isNotBlank(validateStr)) {
                rmq.setMsg(validateStr);
            } else {
                // 验签
                boolean checkRestule = signatureParamter(obj, rsaConfigModel.getPublicKey());
                if (checkRestule) {
                    rmq.setCode(IMessageContent.HttpCodeSucess);
                    rmq.setMsg(IMessageContent.SendMessageSucess);
                    if (sendMessage.getTotalNum() == 0) {
                        sendMessage.setTotalNum(IMessageContent.SendMessageTotalNum);
                    }
                    if (sendMessage.getTotalBackNum() == 0) {
                        sendMessage.setTotalBackNum(IMessageContent.SendMessageTotalBackNum);
                    }
                } else {
                    // 验签失败，则不进行加签
                    rmq.setCode(IMessageContent.SendMessageSignatureFail);
                    rmq.setMsg(IMessageContent.SendMessageSignatureFailStr);
                }
            }
        }
        return rmq;
    }

    public static RsaConfigModel getRsaProperties(String keyVersion) throws RuntimeException{
        return RsaKeyConfig.keyModel.get(keyVersion);
    }

    // 请求消息：验签
    public static boolean signatureParamter(Object obj, String publicKey) {
        boolean checkRestule = false;
        if(obj instanceof SendMessage){
            SendMessage sendMessage = (SendMessage)obj;
            String content = sendMessage.getSendId() + sendMessage.getSendTime() +
                    sendMessage.getReceptionUrl() + sendMessage.getDataContent() +
                    sendMessage.getReqSucessMatch() + sendMessage.getKeyVersion();

            checkRestule =  RSASignature.doCheck(content, sendMessage.getSignSendSender(), publicKey);
        }
        return checkRestule;
    }

    // 返回加签（最后阶段）
    public static ReturnMq returnSignParamterLast(ReturnMq rmq, SendMessage sendMessage){
        if(StringUtils.isBlank(sendMessage.getKeyVersion())){
            rmq.setMsg(rmq.getMsg() + "。" + IMessageContent.SendMessageKeyIsNull);
            return rmq;
        }
        // 拿取对应的KeyVersion版本号
        String keyVersion = sendMessage.getKeyVersion().toLowerCase();
        RsaConfigModel rsaConfigModel = getRsaProperties(keyVersion);
        if(rsaConfigModel == null){
            logger.error("RsaParameterValidation are returnSignParamterLast error ");
            rmq.setMsg(rmq.getMsg() + "。" + IMessageContent.SendMessageKeyVersionIsError);
            return rmq;
        }
        String signStr = returnSignParamter(rmq, rsaConfigModel.getPrivateKey());
        if (StringUtils.isNotBlank(signStr)) {
            rmq.setSignSendMq(signStr);
        } else {
            rmq.setContent(IMessageContent.SendMessageBackSignFail);
        }
        return rmq;
    }

    // 请求消息：返回加签
    public static String returnSignParamter(Object obj, String privateKey){
        String signSendMq = null;
        if(obj instanceof ReturnMq){
            ReturnMq rmq = (ReturnMq)obj;
            String content = rmq.getCode() + rmq.getMsg();
            signSendMq = RSASignature.sign(content, privateKey);
        }
        return signSendMq;
    }



    // 回复消息：请求加签
    public static String signParamter(Object obj, String privateKey){
        String signBackMq = null;
        if(obj instanceof BackMessage){
            BackMessage backMessage = (BackMessage)obj;
            String content = backMessage.getSendId() + backMessage.getBackTime() +
                    backMessage.getCode() + backMessage.getMsg();

            signBackMq = RSASignature.sign(content, privateKey);
        }
        return signBackMq;
    }

    // 回复消息：返回验签
    public static boolean signatureParamterBack(Object obj, String publicKey) {
        boolean checkRestule = false;
        if(obj instanceof ReturnSender){
            ReturnSender rs = (ReturnSender)obj;
            String content = rs.getCode() + rs.getMsg();
            checkRestule =  RSASignature.doCheck(content, rs.getSignBackSender(), publicKey);
        }
        return checkRestule;
    }

//====================================================================================================

    // 模拟消息发送方接收到 回复消息 返回信息加签
    public static ReturnSender checkParamterBackSender(Object obj, String publicKey, String privateKey){
        ReturnSender rs = new ReturnSender();
        rs.setCode(500);
        if(obj instanceof BackMessage){
            BackMessage backMessage = (BackMessage)obj;
            if(backMessage.getSendId() == null || "".equals(backMessage.getSendId().trim())){
                rs.setMsg("sendId 为Null或空字符串");
            }else if(backMessage.getBackTime() == null || "".equals(backMessage.getBackTime().trim())){
                rs.setMsg("backTime 为Null或空字符串");
            }else if(backMessage.getCode() == 0){
                rs.setMsg("code 返回码异常");
            }else if(backMessage.getMsg() == null || "".equals(backMessage.getMsg())){
                rs.setMsg("msg 为Null或空字符串");
            }else{
                // 验签
                boolean checkRestule = signatureParamterBackSender(obj, publicKey);
                if(checkRestule){
                    rs.setCode(IMessageContent.HttpCodeSucess);
                    rs.setMsg("消息发送方：接收MQ的Back回复请求成功！");
                }else{
                    rs.setMsg("消息发送方：接收MQ的Back回复请求验签失败！");
                }
            }
            // 加签
            String signStr = returnSignParamterBackSender(rs, privateKey);
            if(!"".equals(signStr)){
                rs.setSignBackSender(signStr);
            }else {
                rs.setContent("消息发送方：接收请求并返回加签失败！");
            }
        }
        return rs;
    }

    public static boolean signatureParamterBackSender(Object obj, String publicKey){
        boolean checkRestule = false;
        if(obj instanceof BackMessage){
            BackMessage backMessage = (BackMessage)obj;
            String content = backMessage.getSendId() + backMessage.getBackTime() +
                    backMessage.getCode() + backMessage.getMsg();

            checkRestule = RSASignature.doCheck(content, backMessage.getSignBackMq(), publicKey);
        }
        return checkRestule;
    }

    // 回复消息：返回加签
    public static String returnSignParamterBackSender(Object obj, String privateKey){
        String signBackSender = "";
        if(obj instanceof ReturnSender){
            ReturnSender rs = (ReturnSender)obj;
            String content = rs.getCode() + rs.getMsg();
            signBackSender = RSASignature.sign(content, privateKey);
        }
        return signBackSender;
    }

}
