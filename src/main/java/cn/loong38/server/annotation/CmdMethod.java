/**
 * 
 */
package cn.loong38.server.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
 * @author LiuZhiwen<br>
 *         标记方法，value为命令名
 */
//@Excute
public @interface CmdMethod {
    String cmd();

    boolean autoExec() default false;

//    /**
//     * EXCUTE为是否在检测到指令执行还是返回给用户自行处理。
//     * 
//     * @author LiuZhiwen
//     */
//    public @interface Excute {
//	boolean value() default false;
//    }
}
