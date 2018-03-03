package cn.edu.jxau.chainResp;

public class SensitiveFilter implements Filter {

    @Override
    public String doFilter(String str) {
        return str.replaceAll("shit","***");
    }
}
