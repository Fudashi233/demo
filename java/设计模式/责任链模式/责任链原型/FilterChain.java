package cn.edu.jxau.chainResp;

import java.util.ArrayList;
import java.util.List;

public class FilterChain implements Filter {
    
    public List<Filter> filterList; //过滤器链
    
    public FilterChain() {
        filterList = new ArrayList<>();
    }
    
    public FilterChain addFilter(Filter filter) {
        filterList.add(filter);
        return this;
    }
    
    public String doFilter(String info) {
        
        for(Filter filter : filterList) {
            info = filter.doFilter(info);
        }
        return info;
    }
}
