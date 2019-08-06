package annotationutils;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MyService {
    /**
     * 服务接口类
     */
    String value();

    /**
     * 服务版本号
     */
    String version() default "";
}