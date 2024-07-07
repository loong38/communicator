/**
 *
 */
package cn.loong38.server.command;

import cn.loong38.server.UserSocket;
import cn.loong38.server.annotation.CmdClass;
import cn.loong38.server.annotation.CmdMethod;
import cn.loong38.server.log.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 命令类，次类中提供command文件夹中的命令及默认的命令。
 *
 * @author LiuZhiwen
 */
public class Command {
    public static final int CMD = 0;
    public static final int PARAMETER = 1;
    private final static Command command = new Command();
    private final HashMap<String, Object> idToObj = new HashMap<>();
    private final HashMap<String, Method> idToBootCmd = new HashMap<>();
    private final HashMap<String, Method> cmdToMethod = new HashMap<>();
    private final HashMap<String, String> cmdToId = new HashMap<>();
    private final HashMap<String, CmdMethod> cmdAnnObj = new HashMap<>();

    private Command() {
        init();
    }

    /**
     * 获取本类的对象。
     *
     * @return command
     */
    public static Command getCommand() {
        return command;
    }

    /**
     * 初始化。
     */
    private void init() {

        Log.INFO("加载默认命令。");
        try {
            addCommand(Class.forName("cn.loong38.server.command.LinkCommand"));
            addCommand(Class.forName("cn.loong38.server.command.DefaultCommand"));
        } catch (ClassNotFoundException e) {
            Log.ERROR("默认命令类读取失败。");
            e.printStackTrace();
        }

        Log.VERBOSE("枚举命令jar包目录。");

        String path = "./commands";
        File files = new File(path);
        for (File file : files.listFiles()) {
            addCommandJar(file);
        }
    }

    /**
     * @param file
     */
    private void addCommandJar(File file) {
        Log.INFO("加载：", file.getName());
        if (!Command01Tool.isjar(file)) {
            Log.WARN(file.getName(), "jar包类型错误。");
            return;
        }
        readZIP(file);

    }

    /**
     * 读取ZIP文件。
     *
     * @param file
     */
    private void readZIP(File file) {
        if (!Command01Tool.isZip(file)) {
            Log.WARN(file.getName(), "不为ZIP文件。");
            return;
        }

        ZipFile zip = null;
        try {
            zip = new ZipFile(file);
        } catch (ZipException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String fileName = "";
        Enumeration<? extends ZipEntry> enu = zip.entries();// 枚举ZIP中的目录与文件
        while (enu.hasMoreElements()) {

            ZipEntry zipElement = enu.nextElement();// 获取到ZIP中的目录与文件
            fileName = zipElement.getName();

            readCmdClass(fileName);

//	    if (Command01Tool.isclass(fileName))
//		loadCmdClass(fileName);
        }

        try {
            zip.close();
        } catch (IOException e) {
            Log.ERROR(zip.getName(), "zip文件关闭失败");
        }
    }

    /**
     * 读取被CmdClass注释的类
     *
     * @param fileName
     */
    private void readCmdClass(String fileName) {

        if (fileName.lastIndexOf(".class") == fileName.length() - 6) {
            fileName = fileName.substring(0, fileName.length() - 6);
        }
        fileName = fileName.replace("/", ".");

        try {
            Class<?> cls = Class.forName(fileName);

//	    Log.INFO(fileName, "命令读取。");
            addCommand(cls);
        } catch (ClassNotFoundException e) {
            Log.VERBOSE(fileName, "不为class文件");
        }
    }

    /**
     * 添加命令。
     *
     * @param cls
     */
    private void addCommand(Class<?> cls) {
        if (!Command01Tool.isCmdClass(cls)) return;
        Log.INFO("加载命令类：", cls.getName());

        String clsID = cls.getAnnotation(CmdClass.class).id();

        addObj(clsID, cls);
        addBootCmd(clsID, cls);
        addCmd(clsID, cls);

    }

    private void addCmd(String clsID, Class<?> cls) {
        for (Method method : cls.getMethods()) {
            if (method.isAnnotationPresent(CmdMethod.class)) {// 判断一个方法是否被CmdMethod注释
                CmdMethod annotation = method.getAnnotation(CmdMethod.class);

                try {
                    Method met = cls.getMethod(annotation.cmd(), String.class, UserSocket.class);
                    cmdToMethod.put(annotation.cmd(), met);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

                cmdToId.put(annotation.cmd(), clsID);
                cmdAnnObj.put(annotation.cmd(), annotation);

                Log.INFO("添加命令:", annotation.cmd());
            }
        }
    }

    /**
     * 添加CmdInterface
     *
     * @param clsID
     * @param cls
     */
    private void addBootCmd(String clsID, Class<?> cls) {
        try {
            idToBootCmd.put(clsID, cls.getMethod("bootCommand", String.class, UserSocket.class));
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 添加对象
     *
     * @param cls
     */
    private void addObj(String clsID, Class<?> cls) {

        try {
            Object obj = cls.getConstructor().newInstance();
            idToObj.put(clsID, obj);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }

    /**
     * 获取命令所对应的CommandID
     *
     * @param cmd
     * @return
     */
    public String getCommandID(String cmd) {
        return cmdToId.get(cmd);
    }

    /**
     * 获取指定方法的注解
     *
     * @param cmd
     * @return
     */
    public CmdMethod getCmdAnnotation(String cmd) {
        return cmdAnnObj.get(cmd);
    }

    /**
     * 执行指定的命令。
     *
     * @param cmd
     * @param parameter
     * @param userSocket
     */
    public void invoke(String cmd, String parameter, UserSocket userSocket) {
        try {
            cmdToMethod.get(cmd).invoke(idToObj.get(cmdToId.get(cmd)), parameter, userSocket);
        } catch (IllegalAccessException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NullPointerException e) {
            try {
                new DataOutputStream(userSocket.getOutputStream()).writeUTF("return 未知命令：" + cmd);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Log.ERROR(cmd, "未知命令");
        }
    }

    /**
     * 执行接口提供的方法。
     *
     * @param msg
     * @param userSocket
     */
    public void invokeBootCmd(String id, String msg, UserSocket userSocket) {
//	try {
//	    Object obj = cls.getConstructor().newInstance();
//	    idToObj.put(clsID, obj);
//	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//		| NoSuchMethodException | SecurityException e) {
//	    // TODO 自动生成的 catch 块
//	    e.printStackTrace();
//	}
//	String id = cmdToId.get(msg.split(" ", 2)[CMD]);
        try {
            idToBootCmd.get(id).invoke(idToObj.get(id), msg, userSocket);
        } catch (NullPointerException e) {
            Log.ERROR("未找到此ID", id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
