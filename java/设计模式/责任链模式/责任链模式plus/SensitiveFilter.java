package cn.edu.jxau.chainResp;

public class SensitiveFilter implements Filter {

    @Override
    public String doFilter(String str, FilterChain filterChain) {
        
        System.out.println("before sensitive filter");
        str = str.replaceAll("shit","***");
        str = filterChain.doFilter(str);
        System.out.println("after sensitive filter");
        return str;
    }
}
