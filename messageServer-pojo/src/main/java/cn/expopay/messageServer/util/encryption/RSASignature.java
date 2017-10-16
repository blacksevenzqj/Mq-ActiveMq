package cn.expopay.messageServer.util.encryption;

import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSASignature{

    private static Logger logger = LoggerFactory.getLogger(RSASignature.class);

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 商户私钥
     * @param encode 字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        try
        {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(encode));
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
//            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String content, String privateKey)
    {
        try
        {
            // 私钥的秘钥规范
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
     * @param publicKey 分配给开发商公钥
     * @param encode 字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));
            boolean bverify = signature.verify( Base64.getDecoder().decode(sign));
            return bverify;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
//            e.printStackTrace();
        }
        return false;
    }

    public static boolean doCheck(String content, String sign, String publicKey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
//            signature.update(content.getBytes());
            signature.update(content.getBytes("UTF-8"));
            boolean bverify = signature.verify( Base64.getDecoder().decode(sign));
            return bverify;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
//            e.printStackTrace();
        }
        return false;
    }


//    public static void main(String[] args) throws Exception{
//        String filepath="D:/temp/two/";
//        System.out.println("---------------私钥签名过程------------------");
////        String content="ihep_这是用于签名的原始数据";
//        String content = "200请求成功！";
//        String signstr=RSASignature.sign(content, RSAEncrypt.loadPrivateKeyByFile(filepath));
//        System.out.println("签名原串：" + content);
//        System.out.println("签名串：" + signstr);
//        System.out.println();
//
//        System.out.println("---------------公钥校验签名------------------");
//        System.out.println("签名原串：" + content);
//        System.out.println("签名串：" + signstr);
//
//        System.out.println("验签结果：" + RSASignature.doCheck(content, signstr, RSAEncrypt.loadPublicKeyByFile(filepath)));
//        System.out.println();
//    }

}