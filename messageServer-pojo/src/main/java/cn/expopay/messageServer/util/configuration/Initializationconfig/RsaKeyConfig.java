package cn.expopay.messageServer.util.configuration.Initializationconfig;

import cn.expopay.messageServer.model.rsa.RsaConfigModel;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RsaKeyConfig {

    private Logger logger = LoggerFactory.getLogger(RsaKeyConfig.class);

    public static final Map<String, RsaConfigModel> keyModel = new HashMap<String, RsaConfigModel>();

    public RsaKeyConfig() {
    }

    public void init(){
        getContentByXML("config/RsaKeyConfig.xml", keyModel);
//        for (Map.Entry<String, RsaConfigModel> entry : keyModel.entrySet()) {
//           System.out.println("key= " + entry.getKey() + " and value= "
//              + entry.getValue());
//        }
    }

    public void getContentByXML(String fileName, Map<String, RsaConfigModel> map) {
        SAXReader reader = new SAXReader();
        try {
            String filePath = this.getClass().getClassLoader().getResource(fileName).getPath();
//            System.out.println(filePath);
            Document document = reader.read(new File(filePath));
            Element root = document.getRootElement();
            List<Element> elemList = root.elements();
            String keyVersion = "";
            if(elemList!=null && elemList.size()>0){
                for (Element element : elemList) {
//                    System.out.println("waild " + element.getName());
                    getElementVal(element, map, keyVersion);
                }
            }else{
                getElementVal(root, map, keyVersion);
            }
        } catch (Exception e) {
            logger.error("BackUrlMapKeyConfig 加载配置异常：" + e);
        }
    }

    public void getElementVal(Element element, Map<String, RsaConfigModel> map, String keyVersion) {
        try {
            Iterator<Element> iterator = element.elementIterator();
            if("payGateMerchant".equals(element.getName())){
                RsaConfigModel rsaConfigModel = new RsaConfigModel();
                String name = "";
//                System.out.println("inner " + element.getName());
                for ( Iterator k = element.attributeIterator(); k.hasNext(); ) {
                    Attribute attribute = (Attribute) k.next();
//                    p(attribute.getName() + "-" + attribute.getValue());
                    if("id".equals(attribute.getName())){
                        keyVersion = attribute.getValue();
                    }else if("name".equals(attribute.getName())){
                        name = attribute.getValue();
                    }
                }
                if(map.get(keyVersion) != null){
                    throw new RuntimeException("配置文件id重复了！");
                }
                rsaConfigModel.setKeyVersion(keyVersion);
                rsaConfigModel.setName(name);
                map.put(keyVersion, rsaConfigModel);
            }
            if (iterator.hasNext()) {
                while(iterator.hasNext()){
                    getElementVal(iterator.next(), map, keyVersion);
                }
            }else {
//                System.out.println("inner else " + element.getName());
                if (element.isTextOnly()) {
                    RsaConfigModel rsaConfigModel = map.get(keyVersion);
                    String name = element.getName();
                    String value = element.getTextTrim();
                    if(value != null && !"".equals(value)){
                        if("privateKey".equals(element.getName())){
//                            p("elementj is " + name + " value is " + value);
                            rsaConfigModel.setPrivateKey(value);
                        }else if("publicKey".equals(name)){
//                            p("elementj is " + name + " value is " + value);
                            rsaConfigModel.setPublicKey(value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("读取xml文件出错了.请检查xml文件是否完好! 错误信息：" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void p(Object o) {
        System.out.println(o);
    }

}
