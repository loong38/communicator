/**
 * 
 */
package cn.loong38.server.command;

import cn.loong38.server.HashUser;
import cn.loong38.server.UserSocket;
import cn.loong38.server.annotation.CmdClass;
import cn.loong38.server.annotation.CmdMethod;
import cn.loong38.server.link.Link;
import cn.loong38.server.link.LinkObject;
import cn.loong38.server.log.Log;
import cn.loong38.server.thread.UserThread;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author LiuZhiwen
 *
 */
@CmdClass(id = "LinkCommand")
public class LinkCommand implements CmdInterface {

    private HashUser hashuser = HashUser.getHashuser();

    @Override
    public void bootCommand(String msg, UserSocket userSocket) {
	Runnable run = () -> {
	    String cmd[] = msg.split(" ", 2);
	    switch (cmd[Command.CMD]) {
	    case "link":
		link(cmd[Command.PARAMETER], userSocket);
		break;
	    default:
		try {
		    new DataOutputStream(userSocket.getOutputStream()).writeUTF("未知命令");
		    Log.WARN(userSocket.getInetAddress(), "未知命令");
		} catch (IOException e) {
		    e.printStackTrace();
		}
		break;
	    }
	};
	new Thread(run, "commandThread").start();

    }

    @CmdMethod(cmd = "link")
    public void link(String parameter, UserSocket userSocket) {
	LinkObject obj = Link.readLinkObj(parameter);

	try {
	    DataOutputStream dos = new DataOutputStream(userSocket.getOutputStream());
	    if (!obj.isNull()) {
		if (!hashuser.isUUID(obj.getUUID())) {

		    Log.INFO("允许连接", obj.getUUID());
		    
		    userSocket.setLinkobj(obj);
		    hashuser.put(obj.getUUID(), userSocket);
		    new UserThread(obj.getUUID()).start();
		} else {
		    Log.ERROR(obj.getUUID(), "已存在");
		    dos.writeUTF("return 请勿重复连接。");
		}

	    } else {
		dos.writeUTF("return linkParameterError");
		Log.INFO(userSocket.getInetAddress(), "连接参数错误.");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
