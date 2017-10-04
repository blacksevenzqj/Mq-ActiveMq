package cn.expopay.messageServer.util.http;

import cn.expopay.messageServer.model.returninfo.ReturnSender;
import cn.expopay.messageServer.model.returninfo.ReturnT;
import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;
import cn.expopay.messageServer.util.encryption.RsaParameterValidation;
import cn.expopay.messageServer.util.json.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component(value = "httpManagerBackClient")
public class HttpManagerBackClient {

    private final Logger logger = LoggerFactory.getLogger(HttpManagerBackClient.class);

    @Autowired
    HttpConnectionManager connManager;

    public ReturnSender sendRequest(String finalUrl, String backMessage, String publicKey) {
        return postJson(finalUrl, backMessage, publicKey);
    }

    public ReturnSender postJson(String finalUrl, String backMessage, String publicKey) {
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        CloseableHttpClient httpClient = null;
        try {
            // data
            if (StringUtils.isNotBlank(backMessage) && StringUtils.isNotBlank(publicKey)) {
                httpClient = connManager.getHttpClient();
                httpPost = new HttpPost(finalUrl);
                StringEntity entity = new StringEntity(backMessage, "UTF-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }else {
                logger.error("HttpManagerBackClient are postJson error is backMessage or publicKey is null");
                return new ReturnSender(IMessageContent.HttpBackRequestParameterError, "backMessage or publicKey is null");
            }
            // do post
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                if (response.getStatusLine().getStatusCode() != IMessageContent.HttpCodeSucess) {
                    EntityUtils.consume(entity);
                    return new ReturnSender(response.getStatusLine().getStatusCode(), "StatusCode Error.");
                }
                String responseMsg = EntityUtils.toString(entity, "UTF-8");
                logger.info("HttpManagerBackClient are responseMsg is " + responseMsg);
                EntityUtils.consume(entity);

                if (responseMsg != null && responseMsg.startsWith("{")) {
                    ReturnSender rs = JacksonUtil.readValue(responseMsg, ReturnSender.class);
                    boolean checkRestule = RsaParameterValidation.signatureParamterBack(rs, publicKey);
                    logger.info("HttpManagerBackClient boolean checkRestule is " + checkRestule);
                    if(checkRestule) {
                        return rs;
                    }else {
                        rs.setCode(IMessageContent.BackMessageFromSenderSignatureError);
                        rs.setContent(IMessageContent.BackMessageFromSenderSignatureFailStr);
                        return rs;
                    }
                }else {
                    new ReturnSender(IMessageContent.BackMessageFromSenderResponseNotJSON, "responseMsg is not JSON");
                }
            }
            return new ReturnSender(IMessageContent.BackMessageFromSenderHttpEntityIsNull, "Response Back HttpEntity is null");
        } catch (Exception e) {
            logger.error("HttpManagerBackClient are postJson error is " + e.getMessage());
            return new ReturnSender(ReturnT.FAIL_CODE, e.getMessage());
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
