package testBS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Jiang.YX
 * @version 1.0
 * @date 2019/9/13 11:34
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        Socket socket=null;
        InputStream in=null;
        OutputStream out=null;
        try{
            // 1.建立一个Socket对象，连接itcast.cn域名的80端口
            socket=new Socket("www.itcast.cn",80);
            //2.获取到输出流对象
            in=socket.getInputStream();
            //3.获取到输入流对象
            out=socket.getOutputStream();
            //4.将HTTP协议的请求部分发送到服务端 /subject/about/index.html
            out.write("GET /subject/about/index.heml HTTP/1.1\n".getBytes());
            out.write("HOST:www.itcast.cn\n".getBytes());
            out.write("\n".getBytes());
            //5.读取来自服务端的数据打印到控制台
            int i=in.read();
            while(i!=-1){
                System.out.print((char)i);
                i=in.read();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //6.释放资源
            if(null!=in){
                in.close();
                in=null;
            }
            if(null!=out){
                out.close();
                out=null;
            }
            if(null!=socket){
                socket.close();
                socket=null;
            }
        }


    }
}
