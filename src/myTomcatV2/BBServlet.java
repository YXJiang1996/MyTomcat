package myTomcatV2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jiang.YX
 * @version 1.0
 * @date 2019/9/13 16:43
 */
public class BBServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("bb...init.....");
    }

    @Override
    public void service(InputStream in, OutputStream out) throws IOException {
        System.out.println("BB service...");
        out.write("I am from Server...BB".getBytes());
        out.flush();
    }

    @Override
    public void destroy() {
        System.out.println("bb...destroy.....");
    }
}
