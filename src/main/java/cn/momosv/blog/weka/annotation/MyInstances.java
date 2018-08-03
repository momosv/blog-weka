package cn.momosv.blog.weka.annotation;

import java.lang.annotation.*;


@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MyInstances {

    int CLASS_INDEX() default -1;
    int INDEX();
    String RELATION() default "";
    String ATTRIBUTE()default "";
    String[] value() default {};
    String dateFormat() default "";
    String type() default "numeric";

}
