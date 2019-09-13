package myTomcatV2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jiang.YX
 * @version 1.0
 * @date 2019/9/13 16:40
 */
public interface Servlet {
    public void init();
    public void service(InputStream in, OutputStream out) throws IOException;
    public void destroy();
}
