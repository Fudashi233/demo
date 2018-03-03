package annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数值校验
 * @author 付大石
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NumberCheck {
    
    /**
     * 这个属性仅对包装类有效，表示包装类是否为空
     */
    boolean notNull() default false;
    
    /**
     * 最小值，默认值为Long.MIN_VALUE
     */
    long min() default Long.MIN_VALUE;
    
    /**
     * 最小值，默认职位Long.MAX_VALUE
     */
    long max() default Long.MAX_VALUE;
    
    /**
     * 对数据域数值的限制，当not为true（默认值），in不为空，表示数值不能是in中的任何一个元素。
     * 当not为true，in为空，则没有限制
     * 当not为false，in不为空，表示数值必须是in中的某个元素
     * 当not为false，in为空，会产生语义上的错误，不允许使用
     * 异常
     */
    long[] in() default {};
    
    boolean not() default false;
}
