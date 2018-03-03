package cn.edu.jxau.java.util;

import java.util.Objects;

public final class FStringJoiner {

    private final String prefix; // 前缀
    private final String delimiter; // 分隔符
    private final String suffix; // 后缀
    private StringBuilder value; // 值
    private String emptyValue;

    public FStringJoiner(CharSequence delimiter) {
        this(delimiter, "", "");
    }

    public FStringJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {

        // 参数校验 //
        Objects.requireNonNull(delimiter, "参数delimiter不能为null");
        Objects.requireNonNull(prefix, "参数prefix不能为null");
        Objects.requireNonNull(suffix, "参数suffix不能为null");
        
        // 数据域初始化 //
        this.delimiter = delimiter.toString();
        this.prefix = prefix.toString();
        this.suffix = suffix.toString();
        this.emptyValue = this.prefix + this.suffix;
    }
    
    public FStringJoiner setEmptyValue(CharSequence emptyValue) {
        
        Objects.requireNonNull(emptyValue, "参数empty不能为null");
        this.emptyValue = emptyValue.toString();
        return this;
    }
    
    @Override
    public String toString() {
        
        if(value == null) {
            return emptyValue;
        } else {
            return value.toString()+suffix;
        }
    }
    
    public FStringJoiner add(CharSequence newElement) {
        preBuild().append(newElement);
        return this;
    }
    
    public FStringJoiner merge(FStringJoiner other) {
        
        if(other.value != null) {
            preBuild().append(other.value, other.prefix.length(),
                    other.value.length());
        }
        return this;
    }
    
    private StringBuilder preBuild() {
        
        if(value==null) {
            value = new StringBuilder(prefix);
        } else {
            value.append(delimiter);
        }
        return value;
    }
    
    public int length() {
        return value == null ? (emptyValue.length()) 
                : (value.length() + suffix.length());
    }
}
