package com.yxh.cgc.utils;

import java.nio.ByteOrder;

/**
 * @Author HaoXin
 * @Date 2021/5/19
 */
public class ByteConvertTool {
    /**
     * 无符号 byte类型转 int
     */
    public static Integer byteToUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }

    /**
     * 无符号 short类型转 int
     */
    public static int shortToUnsignedInt(short x) {
        return ((int) x) & 0xffff;
    }

    /**
     * 无符号 int类型转 long
     */
    public static long intToUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
    }


    /**
     * 字节数组转 short，小端
     */
    public static short byteArray2Short_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 2) {
            return 0;
        }

        short value = 0;
        for (int i = 0; i < array.length; i++) {
            // & 0xff，除去符号位干扰
            value |= ((array[i] & 0xff) << (i * 8));
        }
        return value;
    }

    /**
     * 字节数组转 short，大端
     */
    public static short byteArray2Short_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 2) {
            return 0;
        }

        short value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((array[i] & 0xff) << ((array.length - i - 1) * 8));
        }
        return value;
    }

    /**
     * 字节数组转 int，小端
     */
    public static int byteArray2Int_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 4) {
            return 0;
        }

        int value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((array[i] & 0xff) << (i * 8));
        }
        return value;
    }

    /**
     * 字节数组转 int，大端
     */
    public static int byteArray2Int_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 4) {
            return 0;
        }

        int value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((array[i] & 0xff) << ((array.length - i - 1) * 8));
        }
        return value;
    }

    /**
     * 字节数组转 float，小端
     */
    public static float byteArray2Float_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 4) {
            return 0;
        }

        return Float.intBitsToFloat(byteArray2Int_Little_Endian(array));
    }

    /**
     * 字节数组转 float，大端
     */
    public static float byteArray2Float_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 4) {
            return 0;
        }

        return Float.intBitsToFloat(byteArray2Int_Big_Endian(array));
    }

    /**
     * 字节数组转 long，小端
     */
    public static long byteArray2Long_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        long value = 0;
        for (int i = 0; i < array.length; i++) {
            // 需要转long再位移，否则int丢失精度
            value |= ((long) (array[i] & 0xff) << (i * 8));
        }
        return value;
    }

    /**
     * 字节数组转 long，大端
     */
    public static long byteArray2Long_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        long value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((long) (array[i] & 0xff) << ((array.length - i - 1) * 8));
        }
        return value;
    }

    /**
     * 字节数组转 double，小端
     */
    public static double byteArray2Double_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        return Double.longBitsToDouble(byteArray2Long_Little_Endian(array));
    }

    /**
     * 字节数组转 double，大端
     */
    public static double byteArray2Double_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        return Double.longBitsToDouble(byteArray2Long_Big_Endian(array));
    }

    /**
     * 字节数组转 HexString
     */
    public static String byteArray2HexString(byte[] array) {

        StringBuilder builder = new StringBuilder();
        for (byte b : array) {

            String s = Integer.toHexString(b & 0xff);
            if (s.length() < 2) {
                builder.append("0");
            }
            builder.append(s);
        }

        return builder.toString().toUpperCase();
    }

    //---------------------------------华丽的分割线-------------------------------------

    /**
     * short 转字节数组，小端
     */
    public static byte[] short2ByteArray_Little_Endian(short s) {

        byte[] array = new byte[2];

        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * short 转字节数组，大端
     */
    public static byte[] short2ByteArray_Big_Endian(short s) {

        byte[] array = new byte[2];

        for (int i = 0; i < array.length; i++) {
            array[array.length - 1 - i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * int 转字节数组，小端
     */
    public static byte[] int2ByteArray_Little_Endian(int s) {

        byte[] array = new byte[4];

        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * int 转字节数组，大端
     */
    public static byte[] int2ByteArray_Big_Endian(int s) {

        byte[] array = new byte[4];

        for (int i = 0; i < array.length; i++) {
            array[array.length - 1 - i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * float 转字节数组，小端
     */
    public static byte[] float2ByteArray_Little_Endian(float f) {

        return int2ByteArray_Little_Endian(Float.floatToIntBits(f));
    }

    /**
     * float 转字节数组，大端
     */
    public static byte[] float2ByteArray_Big_Endian(float f) {

        return int2ByteArray_Big_Endian(Float.floatToIntBits(f));
    }

    /**
     * long 转字节数组，小端
     */
    public static byte[] long2ByteArray_Little_Endian(long l) {

        byte[] array = new byte[8];

        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (l >> (i * 8));
        }
        return array;
    }

    /**
     * long 转字节数组，大端
     */
    public static byte[] long2ByteArray_Big_Endian(long l) {

        byte[] array = new byte[8];

        for (int i = 0; i < array.length; i++) {
            array[array.length - 1 - i] = (byte) (l >> (i * 8));
        }
        return array;
    }

    /**
     * double 转字节数组，小端
     */
    public static byte[] double2ByteArray_Little_Endian(double d) {

        return long2ByteArray_Little_Endian(Double.doubleToLongBits(d));
    }

    /**
     * double 转字节数组，大端
     */
    public static byte[] double2ByteArray_Big_Endian(double d) {

        return long2ByteArray_Big_Endian(Double.doubleToLongBits(d));
    }

    /**
     * HexString 转字节数组
     */
    public static byte[] hexString2ByteArray(String hexString) {

        // 两个十六进制字符一个 byte，单数则有误
        if (hexString.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] array = new byte[hexString.length() / 2];

        int value = 0;
        for (int i = 0; i < hexString.length(); i++) {

            char s = hexString.charAt(i);

            // 前半个字节
            if (i % 2 == 0) {
                value = Integer.parseInt(String.valueOf(s), 16) * 16;
            } else {
                // 后半个字节
                value += Integer.parseInt(String.valueOf(s), 16);
                array[i / 2] = (byte) value;
                value = 0;
            }
        }

        return array;
    }

    public static String array2String(byte[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(array.length).append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i != array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // 默认大端

    public static short array2Short(byte[] array) {
        return array2Short(array, ByteOrder.BIG_ENDIAN);
    }

    public static short array2Short(byte[] array, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return byteArray2Short_Little_Endian(array);
        }
        return byteArray2Short_Big_Endian(array);
    }


    public static int array2Int(byte[] array) {
        return array2Int(array, ByteOrder.BIG_ENDIAN);
    }

    public static int array2Int(byte[] array, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return byteArray2Int_Little_Endian(array);
        }
        return byteArray2Int_Big_Endian(array);
    }


    public static long array2Long(byte[] array) {
        return array2Long(array, ByteOrder.BIG_ENDIAN);
    }

    public static long array2Long(byte[] array, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return byteArray2Long_Little_Endian(array);
        }
        return byteArray2Long_Big_Endian(array);
    }

    public static float array2Float(byte[] array) {
        return array2Float(array, ByteOrder.BIG_ENDIAN);
    }

    public static float array2Float(byte[] array, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return byteArray2Float_Little_Endian(array);
        }
        return byteArray2Float_Big_Endian(array);
    }

    public static Double array2Double(byte[] array) {
        return array2Double(array);
    }

    public static Double array2Double(byte[] array, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return byteArray2Double_Little_Endian(array);
        }
        return byteArray2Double_Big_Endian(array);
    }

    // 默认大端
    public static byte[] short2ByteArray(short value) {
        return short2ByteArray(value, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] short2ByteArray(short value, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return short2ByteArray_Little_Endian(value);
        }
        return short2ByteArray_Big_Endian(value);
    }

    public static byte[] int2ByteArray(int value) {
        return int2ByteArray(value, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] int2ByteArray(int value, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return int2ByteArray_Little_Endian(value);
        }
        return int2ByteArray_Big_Endian(value);
    }

    public static byte[] long2ByteArray(long value) {
        return long2ByteArray(value, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] long2ByteArray(long value, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return long2ByteArray_Little_Endian(value);
        }
        return long2ByteArray_Big_Endian(value);
    }

    public static byte[] float2ByteArray(float value) {
        return float2ByteArray(value, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] float2ByteArray(float value, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return float2ByteArray_Little_Endian(value);
        }
        return float2ByteArray_Big_Endian(value);
    }

    public static byte[] double2ByteArray(double value) {
        return double2ByteArray(value, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] double2ByteArray(double value, ByteOrder endian) {
        if (endian == ByteOrder.LITTLE_ENDIAN) {
            return double2ByteArray_Little_Endian(value);
        }
        return double2ByteArray_Big_Endian(value);
    }

    public static String byte2Hex(byte num) {
        return byteArray2HexString(new byte[]{num});
    }

    public static short getShort(byte[] arr, int index) {
        return getShort(arr, index, ByteOrder.BIG_ENDIAN);
    }

    // 截取一段为short
    public static short getShort(byte[] arr, int index, ByteOrder endian) {
        byte[] numArr = new byte[Short.BYTES];
        copyArr(arr, index, numArr);
        return array2Short(numArr, endian);
    }

    public static int getInt(byte[] src, int index) {
        return getInt(src, index, ByteOrder.BIG_ENDIAN);
    }

    // 截取一段为int
    public static int getInt(byte[] arr, int index, ByteOrder endian) {
        byte[] numArr = new byte[Integer.BYTES];
        copyArr(arr, index, numArr);
        return array2Int(numArr, endian);
    }

    public static long getLong(byte[] arr, int index) {
        return getLong(arr, index, ByteOrder.BIG_ENDIAN);
    }

    // 截取一段为long
    public static long getLong(byte[] arr, int index, ByteOrder endian) {
        byte[] numArr = new byte[Long.BYTES];
        copyArr(arr, index, numArr);
        return array2Long(numArr, endian);
    }

    public static float getFloat(byte[] src, int index) {
        return getFloat(src, index, ByteOrder.BIG_ENDIAN);
    }

    // 截取一段为float
    public static float getFloat(byte[] arr, int index, ByteOrder endian) {
        byte[] numArr = new byte[Float.BYTES];
        copyArr(arr, index, numArr);
        return array2Float(numArr, endian);
    }

    public static Double getDouble(byte[] arr, int index) {
        return getDouble(arr, index, ByteOrder.BIG_ENDIAN);
    }

    // 截取一段为double
    public static Double getDouble(byte[] arr, int index, ByteOrder endian) {
        byte[] numArr = new byte[Double.BYTES];
        copyArr(arr, index, numArr);
        return array2Double(numArr, endian);
    }

    private static void copyArr(byte[] src, int index, byte[] dest) {
        System.arraycopy(src, index, dest, 0, dest.length);
    }
}
