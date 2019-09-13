package myTomcatV2;

import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author Jiang.YX
 * @version 1.0
 * @date 2019/9/13 15:13
 */
public class TestServer {
    //定义一个变量，存放服务端WebContent目录的绝对路径
    public static String WEB_ROOT = System.getProperty("user.dir") + "\\" + "WebContent";
    //定义静态变量，用于存放本次请求的静态页面名称
    private static String url = "";

    //定义一个静态类型Map,用于存储服务端conf.properties中的配置信息
    private static Map<String, String> map = new HashMap<String, String>();

    static {
        //服务器启动之前降配置参数中的信息加载到map中
        //创建一个Properties对象
        Properties prop = new Properties();
        try {
            //加载WebContent目录下的conf.properties文件
            prop.load(new FileInputStream(WEB_ROOT + "\\conf.properties"));
            //将配置文件中的数据读取到map中
            Set set = prop.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = prop.getProperty(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            //创建ServerSocket,监听本机器8080端口，等待来自客户端的请求
            serverSocket = new ServerSocket(8080);
            while (true) {
                //获取到客户端对应的socket
                socket = serverSocket.accept();
                //获取输入流对象
                in = socket.getInputStream();
                //获取输出流对象
                out = socket.getOutputStream();
                //获取HTTP协议的请求部分，截取客户端要访问的资源名称，将这个资源名赋值给url
                parse(in);
                //判断本次请求的是一个静态demo.html界面，还是运行在服务端一段JAVA小程序
                if (null != url) {
                    if (url.indexOf(".") != -1) {
                        //发送静态资源文件
                        sendStaticResource(out);
                    } else {
                        //发送动态资源
                        sendDynamicResource(in, out);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            if (null != in) {
                in.close();
                in = null;
            }
            if (null != out) {
                out.close();
                out = null;
            }
            if (null != socket) {
                socket.close();
                socket = null;
            }
        }
    }

    /**
     * 获取HTTP协议的请求部分，截取客户端要访问的资源名称，将这个资源名赋值给url
     *
     * @param in
     */
    private static void parse(InputStream in) throws IOException {
        //定义一个变量，存放HTTP协议请求部分数据
        StringBuffer content = new StringBuffer(2048);
        //.定义一个数组，存放HTTP协议请求部分数据
        byte[] buffer = new byte[2048];
        //定义一个变量i,代表读取数据到数组之后，数据量的大小
        int i = -1;
        //读取客户端发送过来的数据，将数据读取到字节数组buffer中，i代表读取数据量的大小
        i = in.read(buffer);
        //遍历字节数组，将数组中的数据追加到content变量中
        for (int j = 0; j < i; j++) {
            content.append((char) buffer[j]);
        }
        //打印HTTP协议请求部分数据
        System.out.println(content);
        //截取客户端要请求的资源路径demo.html,赋值给url
        parseUrl(content.toString());

    }

    //截取客户端要请求的资源路径demo.html,赋值给url
    public static void parseUrl(String content) {
        int index1, index2;
        index1 = content.indexOf(" ");
        if (index1 != -1) {
            index2 = content.indexOf(" ", index1 + 1);
            if (index2 > index1) {
                url = content.substring(index1 + 2, index2);
            }
        }
        System.out.println(url);

    }

    /**
     * 发送静态资源
     *
     * @param out
     */
    private static void sendStaticResource(OutputStream out) throws IOException {
        //定义一个字节数组，用于存放本次请求的静态资源demo01.html的内容
        byte[] bytes = new byte[2048];
        //定义一个文件输入流，用户获取静态资源demo01.html中的内容
        FileInputStream fileIn = null;
        try {
            //创建文件对象File,代表本次要请求的资源demo01.html
            File file = new File(WEB_ROOT, url);
            //如果文件存在
            if (file.exists()) {
                //向客户端输出HTTP协议的响应行/响应头
                out.write("HTTP/1.1 200 OK\n".getBytes());
                out.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                out.write("Server:Apache-Coyote/1.1\n".getBytes());
                out.write("\n".getBytes());
                //获取到文件输入流对象
                fileIn = new FileInputStream(file);
                //读取静态资源demo01.html中的内容到数组中
                int ch = fileIn.read(bytes);
                while (ch != -1) {
                    //将读取到数组中的内容通过输出流发送到客户端
                    out.write(bytes, 0, ch);
                    ch = fileIn.read(bytes);
                }
            } else {
                //如果文件不存在，向客户端响应文件不存在的消息
                out.write("HTTP/1.1 200 OK\n".getBytes());
                out.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                out.write("Server:Apache-Coyote/1.1\n".getBytes());
                out.write("\n\n".getBytes());
                String errorMsg = "File not found";
                out.write(errorMsg.getBytes());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fileIn) {
                fileIn.close();
                fileIn = null;
            }
        }
    }

    public static void sendDynamicResource(InputStream in, OutputStream out) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //将HTTP协议的响应行和响应头发送到客户端
        out.write("HTTP/1.1 200 OK\n".getBytes());
        out.write("Content-Type:text/html;charset=utf-8\n".getBytes());
        out.write("Server:Apache-Coyote/1.1\n".getBytes());
        out.write("\n".getBytes());
        //判断map中是否存在一个key,这个keys是否和背刺待请求的资源路径一致
        if (map.containsKey(url)) {
            //如果包含指定的key,获取到Map中key对应的value部分
            String value = map.get(url);
            //通过反射将对应的JAVA程序加载到内存
            Class clazz = Class.forName(value);
            Servlet servlet = (Servlet) clazz.newInstance();
            //执行init方法
            servlet.init();
            //执行service方法
            servlet.service(in, out);
        }

    }
}
