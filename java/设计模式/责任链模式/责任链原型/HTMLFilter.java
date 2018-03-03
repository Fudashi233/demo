package cn.edu.jxau.chainResp;

public class HTMLFilter implements Filter {

    @Override
    public String doFilter(String str) {
        return str.replace("<","&lt;").replace(">","&gt");
    }
}
