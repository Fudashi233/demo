package login;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;  
  
/** 
 * @author Jason.F 
 * Date 2016年5月20日 
 * **/  
  
public class IteyeLoginX {  
    private String loginURL ="http://www.iteye.com/login";  
      
    private void login(){    
        //第一次请求  
        Connection conFirst=Jsoup.connect(loginURL);  
        //配置模拟浏览器  
        conFirst.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");  
        Response rs=null;  
        try {  
            rs = conFirst.execute();  
        } catch (IOException e2) {  
                // TODO Auto-generated catch block  
                e2.printStackTrace();  
            }//获取响应  
        Document d1=Jsoup.parse(rs.body());//转换为Dom树  
        List<Element> et= d1.select("#login_form");//获取form表单，可以通过查看页面源码代码得知  
        //获取，cooking和表单属性，下面map存放post时的数据   
        Map<String, String> datas=new HashMap<String,String>();  
        for(Element e:et.get(0).getAllElements()){  
            System.out.println(e.text());  
            if(e.attr("name").equals("name"))  
                 e.attr("value", "fjssharpsword");//设置用户名  
            if(e.attr("name").equals("password"))  
                 e.attr("value","xyz"); //设置用户密码  
            if(e.attr("name").length()>0)//排除空值表单属性  
                datas.put(e.attr("name"), e.attr("value"));    
        }    
        /** 
         * * 第二次请求，post表单数据，以及cookie信息 
         **/  
        Connection conSecond=Jsoup.connect(loginURL);  
        conSecond.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");  
        //设置cookie和post上面的map数据  
        Response rsLogin=null;  
        try {  
            rsLogin = conSecond.ignoreContentType(true).method(Method.POST).data(datas).cookies(rs.cookies()).execute();  
        } catch (IOException e1) {  
                // TODO Auto-generated catch block  
                e1.printStackTrace();  
            }  
        //输出提交后html，看地址列表  
        System.out.println(rsLogin.body());  
          
        //登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可  
        Map<String, String> map=rsLogin.cookies();  
        for(String s:map.keySet())  
            System.out.println(s+"      "+map.get(s));  
    }  
      
    public static void main(String[] args) {  
        IteyeLoginX lession = new IteyeLoginX();  
        lession.login();  
    }  
}  