/**
 * 
 */
package cn.loong38.server.command;

import cn.loong38.server.HashUser;
import cn.loong38.server.UserSocket;
import cn.loong38.server.annotation.CmdClass;
import cn.loong38.server.annotation.CmdMethod;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author LiuZhiwen
 *
 */
@CmdClass(id = "defaultCommand")
public class DefaultCommand implements CmdInterface {

    @Override
    public void bootCommand(String msg, UserSocket userSocket) {

	DataOutputStream dos = new DataOutputStream(null);
	try {
	    dos = new DataOutputStream(userSocket.getOutputStream());
	} catch (IOException e1) {
	    e1.printStackTrace();
	}

	String[] cmd = msg.split(" ", 2);
	switch (cmd[Command.CMD]) {
	case "ls":
	    if (dos != null)
		ls(cmd[Command.PARAMETER], userSocket);
//		    dos.writeUTF(ls(cmd[Command.PARAMETER], userSocket));
	    break;
	case "say":
	    say(cmd[Command.PARAMETER], userSocket);
	    break;
	default:
	    break;
	}
    }

    @CmdMethod(cmd = "say")
    public void say(String msg, UserSocket userSocket) {
	HashUser hashuser = HashUser.getHashuser();

	String[] value = msg.split(" ", 2);
	UserSocket us = hashuser.get(value[0]);
	if (us == null) {
	    try {
		new DataOutputStream(userSocket.getOutputStream()).writeUTF("未找到此用户");
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    return;
	}

	try {
	    DataOutputStream dos = new DataOutputStream(us.getOutputStream());
	    dos.writeUTF("return " + userSocket.getLinkobj().getUUID() + "对你说" + value[1]);
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    /**
     * 查看
     * 
     * @param parameter
     * @return
     */
    @CmdMethod(cmd = "ls")
    public void ls(String parameter, UserSocket userSocket) {
	HashUser hashuser = HashUser.getHashuser();
	StringBuffer sb = new StringBuffer();

	System.out.println(parameter);
	switch (parameter) {
	case "?":
	    sb.append("\nuser");
	    sb.append("\nsocket");
	    break;
	case "user":
	    for (String userUUID : hashuser.keySet()) {
		// String id = uss.getLinkobj().getUUID();
		sb.append(userUUID);
	    }
	    break;
	case "socket":
	    for (UserSocket us : hashuser.values()) {
		sb.append(us.toString());
	    }
	    break;
	default:
	    sb.append("请输入正确参数！");
	    break;
	}
//	return "return " + sb.toString();
	try {
	    new DataOutputStream(userSocket.getOutputStream()).writeUTF("return " + sb.toString());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
