/**
 * 
 */
package cn.loong38.server.thread;

import cn.loong38.server.HashUser;
import cn.loong38.server.UserSocket;
import cn.loong38.server.command.Command;
import cn.loong38.server.log.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author LiuZhiwen
 *
 */
public class UserThread extends Thread {
//    private LinkObject linkobj;
//    private Socket socket;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    Command cmd = Command.getCommand();
    UserSocket userSocket;

    public UserThread(String name) {
	super("UUID:" + name);
//	this.socket = HashUser.getHashuser().get(name).socket;
//	this.linkobj = HashUser.getHashuser().get(name).linkobj;
	userSocket = HashUser.getHashuser().get(name);
	init();
    }

    private void init() {
	try {
	    dis = new DataInputStream(userSocket.getInputStream());
	    dos = new DataOutputStream(userSocket.getOutputStream());
	    dos.writeUTF("return link");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void run() {
	super.run();

	String line = "";

	if (dis != null && dos != null)
	    boot: while (true) {
		try {
		    Thread.sleep(200);
		    line = "";
		    line = dis.readUTF();
		} catch (IOException e) {
		    line = "";
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		if (!line.equals(""))
		    execCmd(line);

		if (!isLinkNow())
		    break boot;
	    }
    }

    private void execCmd(String line) {
	String[] msg = new String[] { "", "" };
	String[] str = line.split(" ", 2);
	int i = 0;
	for (String s : str) {
	    msg[i] = s;
	    i++;
	}
//	msg[Command.CMD] = str[Command.CMD];
//	msg[Command.PARAMETER] = str[Command.PARAMETER];
	cmd.invoke(msg[Command.CMD], msg[Command.PARAMETER], userSocket);
	// cmd获取ID，id获取类，类获取对象，对象执行方法
    }

    private boolean isLinkNow() {
	try {
	    userSocket.sendUrgentData(0xFF);
	} catch (IOException e1) {
	    HashUser hashUser = HashUser.getHashuser();
	    hashUser.remove(userSocket.getLinkobj().getUUID());
//	    hashUser.remove

	    Log.INFO(userSocket.getLinkobj().getUUID() + "断开连接。");
	    return false;
	}
	return true;
    }

}
