/**
 * 
 */
package cn.loong38.server.command;

import cn.loong38.server.annotation.CmdClass;
import cn.loong38.server.log.Log;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * @author LiuZhiwen
 *
 */
public class Command01Tool {
    protected static boolean isZip(File file) {
	try {
	    new ZipFile(file).close();// ZIP读取错误停止该方法
	} catch (ZipException e) {
	    Log.ERROR("未知jar类型!");
	    return false;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return true;
    }

    /**
     * 判断是否是一个被注释的类
     * 
     * @param cls
     * @return
     */
    protected static boolean isCmdClass(Class<?> cls) {
	if (cls.isAnnotationPresent(CmdClass.class)) {
	    for (Class<?> inter : cls.getInterfaces()) {// 然后循环检查是否引入了CmdInterface接口。
		if (inter.equals(CmdInterface.class)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * 判断是否是jar包
     * 
     * @param file
     * @return
     */
    protected static boolean isjar(File file) {
	int index = file.getName().lastIndexOf(".jar");

	// 最后一次出现.jar要大于.jar的字符数量
	// 并且最后一次出现的索引要为文件名最后倒数第4个字符的位置
	if (index > 4 ? index == (file.getName().length() - 4) : false)
	    return true;

	return false;
    }

    /**
     * 判断是否是class文件
     * 
     * @param file
     * @return
     */
    protected static boolean isclass(String file) {
	int index = file.lastIndexOf(".class");

	// 最后一次出现.class要大于.class的字符数量
	// 并且最后一次出现的索引要为文件名最后倒数第6个字符的位置
	if (index > 6 ? index == (file.length() - 6) : false)
	    return true;

	return false;
    }

}
