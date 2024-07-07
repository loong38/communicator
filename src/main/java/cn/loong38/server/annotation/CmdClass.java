/**
 * 
 */
package cn.loong38.server.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author LiuZhiwen <br>
 *         声明一个类是否是命令类。
 */
public @interface CmdClass {
    String id();

    String version() default "0.0.0.0";

}
