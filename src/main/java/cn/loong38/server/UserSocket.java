/**
 * 
 */
package cn.loong38.server;

import cn.loong38.server.link.LinkObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author LiuZhiwen
 *
 */
public class UserSocket extends Socket {
    private volatile LinkObject linkobj;

    /**
     * @return linkobj
     */
    public LinkObject getLinkobj() {
	return linkobj;
    }

    /**
     * @param linkobj 要设置的 linkobj
     */
    public void setLinkobj(LinkObject linkobj) {
	this.linkobj = linkobj;
    }

    public UserSocket() {
	super();
    }

    public UserSocket(Proxy proxy) {
	super(proxy);
    }

    public UserSocket(String host, int port) throws UnknownHostException, IOException {
	super(host, port);
    }

    public UserSocket(InetAddress address, int port) throws IOException {
	super(address, port);
    }

    public UserSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
	super(host, port, localAddr, localPort);
    }

    public UserSocket(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
	super(address, port, localAddr, localPort);
    }
}
