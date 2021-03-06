package netlog.tanping.com.i18nlib.util;
/*

                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG

*/

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 项目名称: z
 * 类描述：
 * 创建时间:2018/11/28 10:53
 *
 * @author tanping
 */
public class XmlParseUtil {


    public static List<String> parse(String path , List<List<String>> listData,int index,String tag) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        List<String> resultExits  = null;
        // 1.得到DOM解析器的工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // 2.从DOM工厂里获取DOM解析器
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            // 3.解析XML文档，得到DOM树
            File f=new File(path);
            Document docs = db.parse(f);


            boolean needWrite = isNeedWrite(listData,index,docs);
            //需要写内容
            if (needWrite) {
                Comment docment = docs.createComment(tag + "==start");
                if (StringUtil.isNotEmpty(tag)) {
                    docs.getDocumentElement().appendChild(docment);
                }

                resultExits = createBody(listData, index, docs);

                if (StringUtil.isNotEmpty(tag)) {
                    docment = docs.createComment(tag + "==end");
                    docs.getDocumentElement().appendChild(docment);
                }
            }
            //保存xml文件
            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();
            DOMSource domSource=new DOMSource(docs);

            OutputFormat format = new OutputFormat(docs);
            format.setEncoding("UTF-8");
            format.setIndenting(true);

            //设置编码类型
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");

            StreamResult result=new StreamResult(new FileOutputStream(f));


            //把DOM树转换为xml文件
            transformer.transform(domSource, result);
//
//            Writer out = new StringWriter();
//            XMLSerializer serializer = new XMLSerializer(out, format);
//            serializer.serialize(docs);
//            String resultstr = out.toString();
//
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
//            bufferedWriter.write(resultstr);
//            bufferedWriter.flush();
//            bufferedWriter.close();
//            System.out.println(resultstr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            throw e;
        }

        return  resultExits;
    }

    private static List<String> createBody(List<List<String>> listData, int index, Document docs) {
        List<String> resultExits = new ArrayList<>();
        for (int i =1 ;i<listData.size();i++) {
            List<String>  datas =  listData.get(i);
            String name = datas.get(0);

            if (index>=datas.size()){
                continue;
            }
            String value = datas.get(index);

            if (StringUtil.isEmpty(name) || StringUtil.isEmpty(value)){
                continue;
            }

            if (isExist(docs,name,value)){
                resultExits.add(name);
                continue;
            }

            Element bodyElement = docs.createElement("string");
            bodyElement.setAttribute("name", name);
            Text m = docs.createTextNode(value);
            bodyElement.appendChild(m);
            docs.getDocumentElement().appendChild(bodyElement);
        }

        return resultExits;
    }

    private static boolean isNeedWrite(List<List<String>> listData, int index, Document docs) {
        for (int i =1 ;i<listData.size();i++) {
            List<String>  datas =  listData.get(i);
            String name = datas.get(0);

            if (index>=datas.size()){
                continue;
            }
            String value = datas.get(index);

            if (StringUtil.isEmpty(name) || StringUtil.isEmpty(value)){
                continue;
            }

            if (isExist(docs,name,value)){
                continue;
            }

            return true;
        }

        return false;
    }



    /**
     * 查找值是否存在
     * @param document
     * @param kname
     * @param kvalue
     * @return
     */
    public  static boolean isExist(Document document,String kname ,String kvalue){

        NodeList strings = document.getElementsByTagName("string");
        for (int i =0;i<strings.getLength();i++){
            Element node = (Element) strings.item(i);
///           System.out.println(node.toString());
            if (node != null && node.getFirstChild() !=null && node.getNodeName().equals("string")){
                Node name = node.getAttributes().getNamedItem("name");
                String key = name.getNodeValue();
                String value = node.getTextContent();

                if (key.equals(kname)){
                    if (value.equals(kvalue)){
                        return true;
                    }else {
                        System.out.println("Exist key === :"+kname+" value no equals");
                        System.out.println("Exist value === :"+kvalue+"");
                        return true;
                    }

                }

            }


        }
        return false;
    }


}
