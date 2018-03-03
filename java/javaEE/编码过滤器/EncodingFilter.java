package cn.edu.jxau.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 编码过滤器，对请求参数进行编码
 * 对相应参数进行编码
 * @author 付大石
 */
public class EncodingFilter implements Filter {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        chain.doFilter(new EncodingRequestWrapper((HttpServletRequest) request),
                new EncodingResponseWrapper((HttpServletResponse) response));
    }

    @Override
    public void destroy() {

    }

    private class EncodingRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String[]> paramMap;

        public EncodingRequestWrapper(HttpServletRequest request) throws UnsupportedEncodingException {

            super(request);
            paramMap = encodingParam(request);
        }

        private Map<String, String[]> encodingParam(HttpServletRequest request) throws UnsupportedEncodingException {

            Objects.requireNonNull(request, "参数request不能为null");
            Map<String, String[]> paramMap = request.getParameterMap();

            Iterator<Entry<String, String[]>> iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String[]> entry = iterator.next();
                String key = entry.getKey();
                String[] values = entry.getValue();
                for (int i = 0; i < values.length; i++) {
                    values[i] = new String(values[i].getBytes("iso-8859-1"), encoding);
                }
                System.out.println(Arrays.toString(values));
            }
            return paramMap;
        }

        @Override
        public String getParameter(String name) {
            return paramMap.get(name)[0];
        }

        @Override
        public String[] getParameterValues(String name) {
            return paramMap.get(name);
        }
    }

    private class EncodingResponseWrapper extends HttpServletResponseWrapper {

        public EncodingResponseWrapper(HttpServletResponse response) {
            super(response);
            response.setCharacterEncoding(encoding);
            response.setContentType("text/html;charset=UTF-8");
        }
    }
}
