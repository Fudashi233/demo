package cn.edu.jxau.web.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 解决访问中文资源时的404NotFound问题，在web.xml的配置中，
 * 该filter必须是最后一个过滤器
 * @author 付大石
 */
public class URLFilter implements Filter {

    public final static String DEFAULT_URI_ENCODE = "UTF-8";
    private String encode = null;
    public FilterConfig config;

    public void init(FilterConfig config) throws ServletException {
        this.encode = config.getInitParameter("DEFAULT_URI_ENCODE");
        this.config = config;
        if (this.encode == null) {
            this.encode = DEFAULT_URI_ENCODE;
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("---URL Filter---");
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        String decodedUri = URLDecoder.decode(uri, encode);
        System.out.println(uri + "    " + decodedUri);
        if (uri.equals(decodedUri)) {
            chain.doFilter(req, res);
        } else {
            decodedUri = decodedUri.substring(request.getContextPath().length());
            config.getServletContext().getRequestDispatcher(decodedUri).forward(req, res);// 这样写会跳过之后的过滤器直接访问资源
        }
    }
    
    
    public void destroy() {
    }
}
