package cn.rainbow.oxygen.utils.other;

import by.radioegor146.nativeobfuscator.Native;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Native
public class MD5Utils {

    public static String getMD5(Class clazz) {
        String path;
        try {
            path = URLDecoder.decode(clazz.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            path = path.replace("file:\\","").replace("file:","");
            if (path.contains("!")) {
                path = path.substring(0, path.lastIndexOf("!"));
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return MD5Utils.getMD5(new File(path));
    }

    public static String getMD5(File path) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len;
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(path);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
            return bi.toString(16);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
