/**
 * 
 */
package cn.loong38.server.thread;

import cn.loong38.server.UserSocket;
import cn.loong38.server.command.Command;
import cn.loong38.server.log.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @author LiuZhiwen
 *
 */
public class AcceptThread extends Thread {

    private ServerSocket server;

    public AcceptThread(ServerSocket serverSocket) {
	this.server = serverSocket;
	try {
	    serverSocket.setSoTimeout(0);
	} catch (SocketException e) {
	    e.printStackTrace();
	}
	init();
    }

    private void init() {
	setName("服务器接受请求线程");
    }

    @Override
    public void run() {
	super.run();
	UserSocket usersocket = null;
	boot: while (true) {

	    try {
		// 等待连接
		Log.INFO("等待连接");
		usersocket = null;
		usersocket = (UserSocket) server.accept();
		usersocket.setSoTimeout(500);
		Log.INFO(usersocket.getInetAddress(), " 请求连接。");

		linkHandle(usersocket);

	    } catch (IOException e) {
		e.printStackTrace();
		continue boot;
	    }
	}
    }

    private void linkHandle(UserSocket usersocket) {
	try {
	    DataInputStream dis = new DataInputStream(usersocket.getInputStream());
	    String msg = dis.readUTF();
		Command command = Command.getCommand();
		command.invokeBootCmd("LinkCommand", msg, usersocket);
	} catch (SocketTimeoutException e) {
	    Log.ERROR(usersocket.getInetAddress(), "连接请求数据接受超时。");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
