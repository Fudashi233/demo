package cn.edu.jxau.chainResp;

public class HTMLFilter implements Filter {

    @Override
    public String doFilter(String str, FilterChain filterChain) {
        
        System.out.println("before HTML filter");
        str = str.replace("<","&lt;").replace(">","&gt");
        str = filterChain.doFilter(str);
        System.out.println("after HTML filter");
        return str;
    }
}
