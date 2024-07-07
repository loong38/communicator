/**
 * 
 */
package cn.loong38.server.log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LiuZhiwen
 *
 */
public class Log {

    /** 不输出 */
    public static final int NO = 0;
    /** 输出所有 */
    public static final int ALL = 1;
    /** 详细模式 */
    public static final int VERBOSE = 2;
    /** 日志 */
    public static final int INFO = 3;
    /** debug */
    public static final int DEBUG = 4;
    /** 警告 */
    public static final int WARN = 5;
    /** 错误 */
    public static final int ERROR = 6;
    /***/
    public static final int ASSERT = 7;

    public static final String[] modestr = new String[] { "NO", "ALL", "VERBOSE", "INFO", "DEBUG", "WARN", "ERROR",
	    "ASSERT" };

//    enum NO;
    public static int out = Log.NO;

    public static void WARN(Object... msg) {
	print(WARN, msg);
    }

    public static void INFO(Object... msg) {
	print(INFO, msg);
    }

    public static void ERROR(Object... msg) {
	print(ERROR, msg);
    }

    public static void VERBOSE(Object... msg) {
	print(VERBOSE, msg);
    }

    private static boolean isPrint(int mode) {

	switch (mode) {
	case INFO:
	    return (mode == INFO) ? true : false;
	case ALL:
	    return true;
	case ASSERT:
	    return true;
	case DEBUG:
	    return isDebug(mode);
	case ERROR:
	    return (mode == ERROR) ? true : false;
	case NO:
	    return false;
	case VERBOSE:
	    return true;
	case WARN:
	    return (mode == WARN) ? true : false;
	default:
	    break;

	}
	return false;
    }

    private static boolean isDebug(int mode) {
	switch (mode) {
	case VERBOSE:
	case NO:
	case ALL:
	case ASSERT:
	    return false;
	case DEBUG:
	case ERROR:
	case INFO:
	case WARN:
	    return true;
	default:
	    break;
	}
	return false;
    }

    public static void print(int mode, Object... msg) {

	StringBuffer str = new StringBuffer();

	str.append("[");
	str.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
	str.append("]< " + modestr[mode] + " >:");
	if (isPrint(mode)) {
	    for (Object text : msg) {
		str.append(text.toString());
	    }
	    str.append("\n");
	    if (mode == ERROR)
		System.err.print(str.toString());
	    else
		System.out.print(str.toString());
	}
    }

}
