package cn.expopay.messageServer.util.configuration.Initializationconfig;

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

public class BackUrlMapKeyConfig {

    private Logger logger = LoggerFactory.getLogger(BackUrlMapKeyConfig.class);

    public static final Map<String, String> url2Key = new HashMap<String, String>();

    public BackUrlMapKeyConfig() {
    }

    public void init(){
        getContentByXML("config/BackUrlMapKeyConfig.xml", url2Key);
    }

    public void getContentByXML(String fileName, Map<String, String> map) {
        SAXReader reader = new SAXReader();
        try {
            String filePath = this.getClass().getClassLoader().getResource(fileName).getPath();
//            System.out.println(filePath);
            Document document = reader.read(new File(filePath));
            Element root = document.getRootElement();
            List<Element> elemList = root.elements();
            if(elemList!=null && elemList.size()>0){
                for (Element element : elemList) {
                    getElementVal(element, map);
                }
            }else{
                getElementVal(root, map);
            }
        } catch (Exception e) {
            logger.error("BackUrlMapKeyConfig 加载配置异常：" + e);
        }
    }

    public void getElementVal(Element element, Map<String, String> map) {
        try {
            Iterator<Element> iterator = element.elementIterator();
            if (iterator.hasNext()) {
                while(iterator.hasNext()){
                    getElementVal(iterator.next(), map);
                }
            }else {
                if (element.isTextOnly()) {
//                    String name = element.getName();
                    String value = element.getTextTrim();
                    if(value != null && !"".equals(value)){
                        String tempId = "";
//                      p("elementj is " + element.getName() + " value is " + value);
                        for ( Iterator k = element.attributeIterator(); k.hasNext(); ) {
                            Attribute attribute = (Attribute) k.next();
//                        p(attribute.getName() + "-" + attribute.getValue());
                            if("id".equals(attribute.getName())){
                                tempId = attribute.getValue();
                            }
                        }
                        if(tempId == null || "".equals(tempId)){
                            throw new Exception("队列延迟XML配置文件异常，缺少id属性。");
                        }
                        map.put(tempId, value);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("读取xml文件出错了.请检查xml文件是否完好!");
        }
    }

    public static void p(Object o) {
        System.out.println(o);
    }

}
