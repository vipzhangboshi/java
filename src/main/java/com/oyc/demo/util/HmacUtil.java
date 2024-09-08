package com.oyc.demo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by yueguangli on 18/6/12.
 */
public class HmacUtil {

  /**
   * 获取定长的字符串
   *
   * @param str 原始字符串
   * @param len 固定长度
   * @param c   不够填充的字符
   * @return 固定长度的字符串
   */
  public static String getFixedLenString (String str, int len, char c) {
    if (str == null || str.length() == 0) {
      return str;
    }
    if (str.length() == len) {
      return str;
    }
    if (str.length() > len) {
      return str.substring(0, len);
    }
    StringBuilder sb = new StringBuilder(str);
    while (sb.length() < len) {
      sb.append(c);
    }
    return sb.toString();
  }


  private static String twoStringXor (String str1, String str2) {
    byte[] b1 = str1.getBytes();
    byte[] b2 = str2.getBytes();
    byte[] longbytes;
    byte[] shortbytes;
    if (b1.length >= b2.length) {
      longbytes = b1;
      shortbytes = b2;
    } else {
      longbytes = b2;
      shortbytes = b1;
    }
    byte[] xorstr = new byte[longbytes.length];
    int i = 0;
    for (; i < shortbytes.length; i++) {
      xorstr[i] = (byte) (shortbytes[i] ^ longbytes[i]);
    }
    for (; i < longbytes.length; i++) {
      xorstr[i] = longbytes[i];
    }
    return new String(xorstr);
  }


  /**
   * 加密源数据
   *
   * @se 这是针对多条字符串（即数组）进行加密的方法。它会把数组元素拼成新字符串，然后再加密
   * @se 本文暂未用到该方法
   * @para aValue 加密的原文，即源数据
   * @para aKey   密钥
   */
  public static String getHmac (String[] args, String key) {
    if (args == null || args.length == 0) {
      return (null);
    }
    StringBuffer str = new StringBuffer();
    for (String arg : args) {
      str.append(arg);
    }
    return (hmacSign(str.toString(), key));
  }

  /**
   * 加密源数据
   *
   * @param aValue 加密的原文，即源数据
   * @param aKey   密钥
   * @s 这是针对一条字符串进行加密的方法
   */
  public static String hmacSign (String aValue, String aKey) {
    byte[] k_ipad = new byte[64];
    byte[] k_opad = new byte[64];
    byte[] keyb;
    byte[] value;
    keyb = aKey.getBytes(StandardCharsets.UTF_8);
    value = aValue.getBytes(StandardCharsets.UTF_8);

    Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
    Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
    for (int i = 0; i < keyb.length; i++) {
      k_ipad[i] = (byte) (keyb[i] ^ 0x36);
      k_opad[i] = (byte) (keyb[i] ^ 0x5c);
    }

    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
    md.update(k_ipad);
    md.update(value);
    byte[] dg = md.digest();
    md.reset();
    md.update(k_opad);
    md.update(dg, 0, 16);
    dg = md.digest();
    return toHex(dg);
  }

  public static String toHex (byte[] input) {
    if (input == null) {
      return null;
    }
    StringBuffer output = new StringBuffer(input.length * 2);
    for (byte b : input) {
      int current = b & 0xff;
      if (current < 16) {
        output.append("0");
      }
      output.append(Integer.toString(current, 16));
    }
    return output.toString().toUpperCase();
  }
}
