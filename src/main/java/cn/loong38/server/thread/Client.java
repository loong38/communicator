/**
 * 
 */
package cn.loong38.server.thread;

import cn.loong38.server.HashUser;
import cn.loong38.server.UserSocket;
import cn.loong38.server.link.LinkObject;
import cn.loong38.server.log.Log;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author LiuZhiwen
 *
 */
public class Client extends Thread {

    private UserSocket userSocket = new UserSocket();

    public Client() {
	start();
    }

    @Override
    public void run() {
	super.run();
	setName("localUser:Root");

//	Command cmd = Command.getCommand();
//
//	LinkObject obj = new LinkObject();
//	obj.setUUID("root");
//	obj.setUsername("root");
//	userSocket.setLinkobj(obj);
	try (Scanner sc = new Scanner(System.in)) {
	    UserSocket userSocket = new UserSocket("127.0.0.1", 3000);
//	    userSocket.socket = socket;
	    LinkObject obj = new LinkObject();
	    obj.setUUID("root");
	    obj.setUsername("root");
	    userSocket.setLinkobj(obj);

	    DataInputStream dis = new DataInputStream(userSocket.getInputStream());
	    DataOutputStream dos = new DataOutputStream(userSocket.getOutputStream());

	    dos.writeUTF("link " + new Gson().toJson(userSocket.getLinkobj()));

	    new Thread(() -> {
		try {
		    String msg = "";
		    boot: while (!userSocket.isClosed()) {
			msg = "";
			msg = dis.readUTF();
			if (!msg.equals(""))
			    Log.INFO("root:", msg);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }).start();
	    boot: while (true) {

//		    cmd.invokeBootCmd(msg, userSocket);
		if (sc.hasNextLine())
		    dos.writeUTF(sc.nextLine());

		if (!isLinkNow())
		    break boot;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    private boolean isLinkNow() {
	try {
	    userSocket.sendUrgentData(0xFF);
	} catch (IOException e1) {
	    HashUser hashUser = HashUser.getHashuser();
	    System.out.println(userSocket.getLinkobj() == null);
	    hashUser.remove(userSocket.getLinkobj().getUUID());

	    Log.INFO(userSocket.getLinkobj().getUUID() + "断开连接。");
	    return false;
	}
	return true;
    }
}
