package cn.rainbow.oxygen.utils.other;

import by.radioegor146.nativeobfuscator.Native;

@SuppressWarnings("all")
@Native
public class RC4Utils {

    public static String decrypt(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(encrypt(data, key));
    }

    public static String decrypt(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(encrypt(HexString2Bytes(data), key));
    }

    public static byte[] encrypt_byte(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte[] b_data = data.getBytes();
        return encrypt(b_data, key);
    }

    public static String encrypt(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return toHexString(asString(encrypt_byte(data, key)));
    }

    private static String asString(byte[] buf) {
        StringBuilder stringBuilder = new StringBuilder(buf.length);
        for (byte b : buf) {
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString();
    }

    public static byte[] encrypt(byte[] input, String password) {
        int x = 0;
        int y = 0;
        byte[] key = initKey(password);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte[] state = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

    private static String toHexString(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str.append(s4);
        }
        return str.toString();// 0x表示十六进制
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        return (byte) (_b0 ^ _b1);
    }
}
