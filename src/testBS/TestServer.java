package testBS;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jiang.YX
 * @version 1.0
 * @date 2019/9/13 14:40
 */
public class TestServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=null;
        Socket socket=null;
        OutputStream out=null;
        try{
            //1_创建ServerSocker对象，监听本机的8080端口号
            serverSocket=new ServerSocket(8080);
            while(true){
            //2_等待来自客户端的请求获取和客户端对应的Socket对象
            socket=serverSocket.accept();
            //3_通过获取到的Socket对象获取到输出流对象
            out=socket.getOutputStream();
            //4_通过获取到的输出流对象将HTTP协议的相应部分发送到客户端
            out.write("HTTP/1.1 200 OK\n".getBytes());
            out.write("Content-Type:text/html;charset=utf-8\n".getBytes());
            out.write("Server:Apache-Coyote/1.1\n".getBytes());
            out.write("\n\n".getBytes());
            StringBuffer buf=new StringBuffer();
            buf.append("<html>");
            buf.append("<head><title>I am tilte</title></head>");
            buf.append("<body>");
            buf.append("<h1>I am header 1</h1>");
            buf.append("<a href='http://www.baidu.com'>百度</a>");
            buf.append("</body>");
            buf.append("</html>");
            out.write(buf.toString().getBytes());
            out.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5_释放资源
            if(null!=out){
                out.close();
                out=null;
            }
            if(null!=socket){
                socket.close();;
                socket=null;
            }
        }
    }
}
