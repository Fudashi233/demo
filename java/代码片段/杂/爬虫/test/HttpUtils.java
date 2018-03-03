import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/** 
     * Http工具类 
     *  
     * @author Zhu 
     *  
     */  
    public class HttpUtils {  
      
        private static CloseableHttpClient httpClient = HttpClients.createDefault();  
        private static HttpClientContext context = new HttpClientContext();  
      
        private HttpUtils() {  
      
        }  
      
        public static String sendGet(String url) {  
            CloseableHttpResponse response = null;  
            String content = null;  
            try {  
                HttpGet get = new HttpGet(url);  
                response = httpClient.execute(get, context);  
                HttpEntity entity = response.getEntity();  
                content = EntityUtils.toString(entity);  
                EntityUtils.consume(entity);  
                return content;  
            } catch (Exception e) {  
                e.printStackTrace();  
                if (response != null) {  
                    try {  
                        response.close();  
                    } catch (IOException e1) {  
                        e1.printStackTrace();  
                    }  
                }  
            }  
            return content;  
        }  
      
        public static String sendPost(String url, List<NameValuePair> nvps) {  
            CloseableHttpResponse response = null;  
            String content = null;  
            try {  
                //　HttpClient中的post请求包装类  
                HttpPost post = new HttpPost(url);  
                // nvps是包装请求参数的list  
                if (nvps != null) {  
                    post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));  
                }  
                // 执行请求用execute方法，content用来帮我们附带上额外信息  
                response = httpClient.execute(post, context);  
                // 得到相应实体、包括响应头以及相应内容  
                HttpEntity entity = response.getEntity();  
                // 得到response的内容  
                content = EntityUtils.toString(entity);  
                //　关闭输入流  
                EntityUtils.consume(entity);  
                return content;  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                if (response != null) {  
                    try {  
                        response.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
            return content;  
        }  
        
        public static boolean login() throws IOException {  
            // 查看CSDN登陆页面源码发现登陆时需要post5个参数  
            // name、password，另外三个在页面的隐藏域中，a good start  
            // 这样登陆不行，因为第一次需要访问需要拿到上下文context  
            // Document doc = Jsoup.connect(LOGIN_URL).get();  
            String html = HttpUtils.sendGet("https://passport.csdn.net/account/login");  
            Document doc = Jsoup.parse(html);  
            Element form = doc.select(".user-pass").get(0);  
            String lt = form.select("input[name=lt]").get(0).val();  
            String execution = form.select("input[name=execution]").get(0).val();  
            String _eventId = form.select("input[name=_eventId]").get(0).val();  
        	
            boolean result = false;  
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
            nvps.add(new BasicNameValuePair("username", "710106701@qq.com"));  
            nvps.add(new BasicNameValuePair("password", "APTX4869."));  
            nvps.add(new BasicNameValuePair("lt", lt));  
            nvps.add(new BasicNameValuePair("execution", execution));  
            nvps.add(new BasicNameValuePair("_eventId", _eventId));  
            String ret = HttpUtils.sendPost("https://passport.csdn.net/account/login", nvps);  
            if (ret.indexOf("redirect_back") > -1) {  
                System.out.println("登陆成功。。。。。");  
                result = true;  
            } else if (ret.indexOf("登录太频繁") > -1) {  
            	System.out.println("登录太频繁，请稍后再试。。。。。");  
            } else {  
            	System.out.println("登陆失败。。。。。");  
            }  
            return result;  
        }  
        public static void main(String[] args) throws IOException {
        	
        	System.out.println(login());
        }
    }  