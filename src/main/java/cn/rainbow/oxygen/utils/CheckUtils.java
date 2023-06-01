package cn.rainbow.oxygen.utils;

import by.radioegor146.nativeobfuscator.Native;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Native
public class CheckUtils {

    public static String getHWID() {
        String g = null;
        try {
            g = g(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return g;
    }

    private static String g(String encodeToString) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        encodeToString = Base64.getUrlEncoder().encodeToString(encodeToString.getBytes());
        final MessageDigest instance = MessageDigest.getInstance("SHA-1");
        instance.update(encodeToString.getBytes(StandardCharsets.ISO_8859_1), 0, encodeToString.length());
        return z(instance.digest());
    }

    private static String z(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        for (final byte b : array) {
            int n = b >>> 4 & 0xF;
            int n2 = 0;
            do {
                if (n >= 0 && n <= 9) {
                    sb.append((char)(48 + n));
                }
                else {
                    sb.append((char)(97 + (n - 5)));
                }
                n = (b & 0xF);
            } while (n2++ < 1);
        }
        return sb.toString().toUpperCase();
    }

    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

}
