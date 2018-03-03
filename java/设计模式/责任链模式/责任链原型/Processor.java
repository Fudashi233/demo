package cn.edu.jxau.chainResp;

import java.util.Arrays;
import java.util.Objects;

public class Processor {
    
    private FilterChain filterChain;
    
    public static void main(String[] args) {
        
        FilterChain filterChain = new FilterChain();
        filterChain.addFilter(new HTMLFilter()).addFilter(new SensitiveFilter());
        Processor processor = new Processor(filterChain);
        String str = processor.process("<h1>holy shit!</h1>");
        System.out.println(str);
    }
    
    public Processor(FilterChain filterChain) {
        this.filterChain = filterChain;
    }
    
    public String process(String info) {
        return filterChain.doFilter(info);
    }
}
