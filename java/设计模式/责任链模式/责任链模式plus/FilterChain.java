package cn.edu.jxau.chainResp;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    
    public static void main(String[] args) {
        
        FilterChain filterChain = new FilterChain();
        filterChain.addFilter(new HTMLFilter()).addFilter(new SensitiveFilter());
        String info = filterChain.doFilter("<h1>holy shit!</h1>");
        System.out.println(info);
    }
    
    public List<Filter> filterList; //过滤器链
    private int cursor = 0;
    public FilterChain() {
        filterList = new ArrayList<>();
    }
    
    public FilterChain addFilter(Filter filter) {
        filterList.add(filter);
        return this;
    }
    
    public String doFilter(String info) {
        
        if(cursor==filterList.size()) {
            cursor = 0;
            return info;
        }
        Filter filter = filterList.get(cursor);
        cursor++;
        return filter.doFilter(info, this);
    }
}
