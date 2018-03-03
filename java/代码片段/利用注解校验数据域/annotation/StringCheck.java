package annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 字符串校验
 * @author 付大石
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringCheck {
    
    /**
     * 字符串是否可为空，默认可为空
     */
    boolean notNull() default false;
    
    /**
     * 字符串最小长度，默认为0
     */
    int minLen() default 0;
    
    /**
     * 字符串最大长度，默认为Integer.MAX
     */
    int maxLen() default Integer.MAX_VALUE;
    
    /**
     * 字符串需匹配的正则表达式，默认为空串
     */
    String regExp() default "";
    
    /**
     * 正则表达式的匹配表示，默认为0，使用时，为确保可读性请使用
     * {@Code Pattern}的相应的静态数据域作为值
     */
    int flags() default 0;
}
