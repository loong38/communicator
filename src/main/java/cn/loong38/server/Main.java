/**
 *
 */
package cn.loong38.server;

import cn.loong38.server.thread.AcceptThread;
import cn.loong38.server.thread.Client;

import java.io.IOException;

/**
 * @author LiuZhiwen
 *
 */
public class Main {

    /**
     *
     */
    public Main() {
        // TODO 自动生成的构造函数存根
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println(args.length);
        if (args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                switch (args[i]) {
                    case "-help":
                        help();
                        break;
                    case "-GUI":

                        break;

//	    case "":
//		break;
                    default:
                        break;
                }
            }
        } else {
            ServerSocket ss = new ServerSocket(3000);
            new AcceptThread(ss).start();
        }


        new Client();
    }

    private static void help() {
        System.out.println("-GUI	显示GUI控制台。");
    }

}
