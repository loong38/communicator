/**
 * 
 */
package cn.loong38.server.command;

import cn.loong38.server.UserSocket;

/**
 * @author LiuZhiwen
 *
 */
public interface CmdInterface {
    /**
     * CMD的对象是批量传输，在没有本命令的情况下可能会有其他的命令，<br>
     * switch中的default或者if的else中不要加多余的语句。
     * 
     * @param msg
     */
    public void bootCommand(String msg, UserSocket userSocket);

}
