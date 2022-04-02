package com.fentiaozi.netty.socket.util;

import java.math.BigInteger;

public class ByteIntUtil {
    /**
     * byte转换为int
     * @param b byte数组
     * @return
     */
    public static int byte2Int(byte[] b) {
        int mask=0xff;
        int temp=0;
        int n=0;
        for(int i=0;i<b.length;i++){
            n<<=8;
            temp=b[i]&mask;
            n|=temp;
        }
        return n;
    }
    /**
     * int转byte数组
     * @param n
     * @return
     */
    public static byte[] int2Bytes(int n){
        byte[] b = new byte[4];
        for(int i = 0;i < 4;i++){
            b[i]=(byte)(n>>(24-i*8));
        }
        return b;
    }

    /**
     * 把16进制字符串转换成字节数组
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 数组转换成十六进制字符串
     * @author lsl
     * @param bArray
     * @return java.lang.String
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2){
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 16进制求和
     * @param hexdata
     * @return
     */
  /*  public static String makeChecksum1(String hexdata) {
        if (hexdata == null || hexdata.equals("")) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        return Integer.toHexString(total);
    }*/


    /**
     * 从帧头开始按字节求和得出的结果对256求余
     * @param data 16进制数据求和
     * @return hex
     */
    public static String makeCheckSum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        data = data.replaceAll(" ", "");
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /*
          用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 16进制转10进制
     * @param hex
     * @return
     */
    public static BigInteger hex2Int(String hex){
        BigInteger h = new BigInteger(hex, 16);// 16进制转10进制
        return h;
    }

    /**
     * 16进制异或运算
     * @param content
     * @return
     */
    public static String yihuo(String content) {
        content = change(content);
        String[] b = content.split(" ");
        int a = 0;
        for (int i = 0; i < b.length; i++) {
            a = a ^ Integer.parseInt(b[i], 16);
        }
        if(a<10){
            StringBuffer sb = new StringBuffer();
            sb.append("0");
            sb.append(a);
            return sb.toString();
        }
        return Integer.toHexString(a);
    }
    public static String change(String content) {
        String str = "";
        for (int i = 0; i < content.length(); i++) {
            if (i % 2 == 0) {
                str += " " + content.substring(i, i + 1);
            } else {
                str += content.substring(i, i + 1);
            }
        }
        return str.trim();
    }
    public static void main(String[] args) {
        System.out.println(yihuo("8351DBE1"));
    }
}
