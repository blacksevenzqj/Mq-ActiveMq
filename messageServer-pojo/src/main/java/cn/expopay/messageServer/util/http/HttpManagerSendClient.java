package cn.expopay.messageServer.util.http;

import cn.expopay.messageServer.model.returninfo.ReturnT;
import cn.expopay.messageServer.model.send.SendMessage;
import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(value = "httpManagerSendClient")
public class HttpManagerSendClient {

    private final Logger logger = LoggerFactory.getLogger(HttpManagerSendClient.class);

    @Autowired
    HttpConnectionManager connManager;

    public ReturnT sendRequest(String finalUrl, Object object) {
        try {
            if (object != null && object instanceof SendMessage) {
                if (!finalUrl.toLowerCase().startsWith("http://")) {
                    finalUrl = "http://" + finalUrl.toLowerCase();
                }
                SendMessage sendMessage = (SendMessage) object;
                int requestMode = sendMessage.getRequestMode();
                if (requestMode == 0 || requestMode == 1) {
                    return postJson(finalUrl, sendMessage.getDataContent(), sendMessage.getReqSucessMatch(), sendMessage.getHeaderContent());
                } else if(requestMode == 2) {
                    logger.info("postform running !!!");
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String> map = mapper.readValue(sendMessage.getDataContent(), Map.class);
                    return postForm(finalUrl, map, sendMessage.getReqSucessMatch(), sendMessage.getHeaderContent());
                } else if(requestMode == 3){
                    return getJson(finalUrl, sendMessage.getDataContent(), sendMessage.getReqSucessMatch(), sendMessage.getHeaderContent());
                }
            }
        }catch (Exception e){
            logger.error("HttpManagerClient sendRequest parameter is error " + e.getMessage());
            return new ReturnT<String>(IMessageContent.HttpSendRequestParameterError, IMessageContent.HttpSendRequestParameterErrorStr + e.getMessage());
        }
        return new ReturnT<String>(IMessageContent.HttpSendRequestTypeError, IMessageContent.HttpSendRequestTypeErrorStr);
    }

    public ReturnT getJson(String finalUrl, String dataContent, String reqSucessMatch, String headerContent){
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        CloseableHttpClient httpClient = null;
        try {
            if (StringUtils.isNotBlank(dataContent) && StringUtils.isNotBlank(reqSucessMatch)) {
                String requestUrl = finalUrl + "?dataContent=" + URLEncoder.encode(dataContent,"UTF-8");
                httpClient = connManager.getHttpClient();
                httpGet = new HttpGet(requestUrl);
                setHeader(httpGet, headerContent, "getJson");
            }else {
                logger.error("HttpManagerClient are getJson error is dataContent or reqSucessMatch is null");
                return new ReturnT<String>(IMessageContent.HttpSendRequestParameterError, "dataContent or reqSucessMatch is null");
            }
            // do get
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                if (response.getStatusLine().getStatusCode() != IMessageContent.HttpCodeSucess) {
                    EntityUtils.consume(entity);
                    return new ReturnT<String>(response.getStatusLine().getStatusCode(), "HttpResponse getJson StatusCode Error");
                }
                String responseMsg = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);  //关闭InputStream，释放资源
                if(responseMsg.equalsIgnoreCase(reqSucessMatch)){
                    return new ReturnT<String>(ReturnT.SUCCESS_CODE, "SUCESS");
                }else{
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "FAIL, Result Not Match reqSucessMatch");
                }
            }
            return new ReturnT<String>(ReturnT.FAIL_CODE, "HttpResponse getJson HttpResponse'HttpEntity is null");
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnT<String>(ReturnT.FAIL_CODE, e.getMessage());
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

    public ReturnT postJson(String finalUrl, String dataContent, String reqSucessMatch, String headerContent) {
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        CloseableHttpClient httpClient = null;
        try {
            // data
            if (StringUtils.isNotBlank(dataContent) && StringUtils.isNotBlank(reqSucessMatch)) {
                httpClient = connManager.getHttpClient();
                httpPost = new HttpPost(finalUrl);
                StringEntity entity = new StringEntity(dataContent, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                setHeader(httpPost, headerContent, "postJson");
            }else {
                logger.error("HttpManagerClient are postJson error is dataContent or reqSucessMatch is null");
                return new ReturnT<String>(IMessageContent.HttpSendRequestParameterError, "dataContent or reqSucessMatch is null");
            }
            // do post
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                if (response.getStatusLine().getStatusCode() != IMessageContent.HttpCodeSucess) {
                    EntityUtils.consume(entity);
                    return new ReturnT<String>(response.getStatusLine().getStatusCode(), "HttpResponse postJson StatusCode Error");
                }
                String responseMsg = EntityUtils.toString(entity, "UTF-8");
//                logger.info("AdminApiUtil are responseMsg is " + responseMsg);
                EntityUtils.consume(entity);  //关闭InputStream，释放资源
                if(responseMsg.equalsIgnoreCase(reqSucessMatch)){
                    return new ReturnT<String>(ReturnT.SUCCESS_CODE, "SUCESS");
                }else{
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "FAIL, Result Not Match reqSucessMatch");
                }
            }
            return new ReturnT<String>(ReturnT.FAIL_CODE, "HttpResponse postJson HttpResponse'HttpEntity is null");
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnT<String>(ReturnT.FAIL_CODE, e.getMessage());
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

    public ReturnT postForm(String finalUrl, Map<String, String> content, String reqSucessMatch, String headerContent) {
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        CloseableHttpClient httpClient = null;
        try {
            // data
            if (content != null && !content.isEmpty() && StringUtils.isNotBlank(reqSucessMatch)) {
                httpClient = connManager.getHttpClient();
                httpPost = new HttpPost(finalUrl);
                List<BasicNameValuePair> values = new ArrayList<BasicNameValuePair>();
                for (Map.Entry<String, String> entry : content.entrySet()) {
                    values.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                HttpEntity entity = new UrlEncodedFormEntity(values, "UTF-8");
                httpPost.setEntity(entity);
                setHeader(httpPost, headerContent, "postForm");
            }else {
                logger.error("HttpManagerClient are postForm error is dataContent or reqSucessMatch is null");
                return new ReturnT<String>(IMessageContent.HttpSendRequestParameterError, "dataContent or reqSucessMatch is null");
            }
            // do post
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                if (response.getStatusLine().getStatusCode() != IMessageContent.HttpCodeSucess) {
                    EntityUtils.consume(entity);
                    return new ReturnT<String>(response.getStatusLine().getStatusCode(), "HttpResponse postForm StatusCode Error");
                }
                String responseMsg = EntityUtils.toString(entity, "UTF-8");
//                logger.info("AdminApiUtil are responseMsg is " + responseMsg);
                EntityUtils.consume(entity);  //关闭InputStream，释放资源
                if(responseMsg.equalsIgnoreCase(reqSucessMatch)){
                    return new ReturnT<String>(ReturnT.SUCCESS_CODE, "SUCESS");
                }else{
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "FAIL, Result Not Match reqSucessMatch");
                }
            }
            return new ReturnT<String>(ReturnT.FAIL_CODE, "HttpResponse postForm HttpResponse'HttpEntity is null");
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnT<String>(ReturnT.FAIL_CODE, e.getMessage());
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

    private void setHeader(Object obj, String headerContent, String methodType) throws Exception{
        if(StringUtils.isNotBlank(headerContent)){
            ObjectMapper om = new ObjectMapper();
            Map<String, String> map = om.readValue(headerContent, Map.class);
            if (obj instanceof HttpPost) {
                HttpPost httpPost = (HttpPost)obj;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if("postJson".equalsIgnoreCase(methodType)){
                        if (!"Content-Type".equalsIgnoreCase(entry.getKey())) {
                            logger.info("Header key = " + entry.getKey() + " and Header value = " + entry.getValue());
                            httpPost.addHeader(entry.getKey(), entry.getValue());
                        }
                    }else{
                        httpPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            } else if (obj instanceof HttpGet) {
                HttpGet httpGet = (HttpGet)obj;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    logger.info("Header key = " + entry.getKey() + " and Header value = " + entry.getValue());
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
    }


}