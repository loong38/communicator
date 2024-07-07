/**
 *
 */
package cn.loong38.server;

import cn.loong38.server.command.Command;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author LiuZhiwen
 *
 */
public class ServerSocket extends java.net.ServerSocket {

    /**
     *
     */
    public ServerSocket() throws IOException {
    }

    public ServerSocket(int port) throws IOException {
        super(port);
        Command.getCommand();
    }

    @Override
    public UserSocket accept() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isBound())
            throw new SocketException("Socket is not bound yet");
        UserSocket s = new UserSocket();
        implAccept(s);
        return s;
    }
}
