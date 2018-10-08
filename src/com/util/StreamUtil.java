package com.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletInputStream;
import java.io.IOException;

public class StreamUtil {
    public static String getInput(ServletInputStream in) throws IOException {
        StringBuilder content = new StringBuilder();
        byte[] b = new byte[1024];
        int lens = -1;
        while ((lens = in.read(b)) > 0) {
            content.append(new String(b, 0, lens));
        }

        return content.toString();
    }

    public static void setOutput(ServletOutputStream out, String data) throws IOException {
        out.write(data.getBytes());
        out.flush();
    }
}
