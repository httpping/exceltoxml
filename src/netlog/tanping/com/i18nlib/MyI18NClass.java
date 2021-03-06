package netlog.tanping.com.i18nlib;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.tp.xml.InsertXML;
import netlog.tanping.com.i18nlib.util.ExcelReader;
import netlog.tanping.com.i18nlib.util.FileParse;
import netlog.tanping.com.i18nlib.util.StringUtil;
import netlog.tanping.com.i18nlib.util.XmlParseUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * 国际化使用
 */
public class MyI18NClass {

    /**
     * 资源目录 例如 ： C:\work\demo\AndroidDemos\I18NParse\app\src\main\res
     */
    public static String rootRes ;//="C:\\work\\zf\\Zaful\\app\\src\\main\\res";


    /**
     * xml 目录 ： 默认
     */
    public static String rootXLSX;


    public static void main(String[] args) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        System.out.println("start ...");
        String filepath = System.getProperty("user.dir") ;

        if (rootRes == null){
            rootRes  = filepath + "\\app\\src\\main\\res";
        }
        if(rootXLSX ==null){
            rootXLSX = filepath +"\\I18NLib\\xml-data.xlsx";
        }

        parse("v4.4.0");
        System.out.println("ending ... ");

        System.out.println(filepath);


    }

    public static  void parse(String tag) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        //解析 xlsx
        ExcelReader eh = new ExcelReader(rootXLSX,"Sheet1");
        eh.getSheetData();

        //获取目录
        HashMap<String, String> map = FileParse.parse(rootRes);

        List<String> strings =  eh.listData.get(0);

        for (int i = 0 ;i<strings.size();i++){
            String type = strings.get(i);
            if (StringUtil.isEmpty(type)){
                continue;
            }
            String path = map.get(type);
            if (StringUtil.isEmpty(path)){
                continue;
            }

//            InsertXML.exitsContent.

            //添加
            List<String> resultExits = XmlParseUtil.parse(path,eh.listData,i,tag);
            if (InsertXML.exitsContent !=null){
                InsertXML.exitsContent.put(type,resultExits);
            }

        }
    }
}
