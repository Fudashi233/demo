package annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 验证集合的合法性（数组、Collection、Map）
 * @author 付大石
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CollectionCheck {
    
    boolean notNull() default false;
    /**
     * 验证集合的大小是否在minSize到maxSize之间,默认大小是[0,Integer.MAX_VALUE]
     */
    int minSize() default 0;
    int maxSize() default Integer.MAX_VALUE;
}
