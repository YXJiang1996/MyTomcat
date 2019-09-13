package myTomcatV2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jiang.YX
 * @version 1.0
 * @date 2019/9/13 16:41
 */
public class AAServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("aa...init.....");
    }

    @Override
    public void service(InputStream in, OutputStream out) throws IOException {
        System.out.println("AA service...");
        out.write("I am from Server...AA".getBytes());
        out.flush();
    }

    @Override
    public void destroy() {
        System.out.println("aa...destroy.....");
    }
}
