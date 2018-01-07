package net.weibeld.qs.chatbot;

import java.io.UnsupportedEncodingException;

public class Util {

    public static byte[] string2bytes(String s) {
        byte[] bytes = null;
        try {
            bytes = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static String bytes2string(byte[] b) {
        String s = null;
        try {
            s = new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

}
