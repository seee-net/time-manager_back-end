package com.util;

import java.io.BufferedReader;
import java.io.IOException;

public class InputUtil {
    public static String getInput(BufferedReader in) throws IOException {
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}
