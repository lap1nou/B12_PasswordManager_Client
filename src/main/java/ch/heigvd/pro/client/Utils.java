package ch.heigvd.pro.client;

import org.json.JSONArray;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import java.util.Arrays;

public class Utils {

    /**
     * This function is used to properly convert byte array to char array using Char and Byte buffer, it then wipe all
     * the data used, in order to not keep anything sensible in memory.
     * Source : https://stackoverflow.com/questions/5513144/converting-char-to-byte
     *
     * @param toConvert the char array to convert
     * @return a byte array converted from the char array
     */
    public static byte[] charToByteArray(char[] toConvert) {
        CharBuffer charBuffer = CharBuffer.wrap(toConvert);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);

        byte[] result = new byte[toConvert.length];

        for (int i = 0; i < result.length; ++i) {
            result[i] = byteBuffer.get(i);
        }

        // Wiping passwords
        Arrays.fill(byteBuffer.array(), (byte) 0);
        Arrays.fill(charBuffer.array(), (char) 0);

        return result;
    }

    /**
     * This function is used to properly convert char array to byte array, it then wipe all
     * the data used, in order to not keep anything sensible in memory.
     *
     * @param toConvert the byte array to convert
     * @return a char array converted from the byte array
     */
    public static char[] byteToCharArray(byte[] toConvert) {
        char[] result = new char[toConvert.length];

        for (int i = 0; i < toConvert.length; ++i) {
            result[i] = (char) toConvert[i];
        }

        return result;
    }

    /**
     * This function convert JSONArray to byte array
     * @param toConvert JSON array
     * @return byte array
     */
    public static byte[] JSONArrayTobyte(JSONArray toConvert){
        byte[] value = new byte[toConvert.length()];

        for(int i = 0; i < toConvert.length(); ++i){
            value[i] = ( (Integer)toConvert.get(i)).byteValue();
        }

        return value;
    }
}
