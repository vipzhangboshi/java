package com.oyc.demo.util;

import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

///**
// * Created by yueguangli on 18/6/12.
// */
//@Slf4j
public class AesUtil {

  //编码方式
  public static final String bm = "UTF-8";

  /**
   * 加密
   *
   * @param data         被加密数据
   * @param dataSecretIV 初始向量 AES 为16bytes. DES 为8bytes
   * @param dataSecret   私钥  AES固定格式为128/192/256 bits.即：16/24/32bytes。DES固定格式为128bits，即8bytes。
   * @return
   */
  public static String encrypt (String data, String dataSecretIV, String dataSecret) {
    //加密方式： AES128(CBC/PKCS5Padding) + Base64, 私钥：aabbccddeeffgghh
    try {
      if (data == null || data.isEmpty()) {
        return "";
      }
      IvParameterSpec zeroIv = new IvParameterSpec(dataSecretIV.getBytes());
      //两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
      SecretKeySpec key = new SecretKeySpec(dataSecret.getBytes(), "AES");
      //实例化加密类，参数为加密方式，要写全
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
      //初始化，此方法可以采用三种方式，按加密算法要求来添加。
      //（1）无第三个参数（
      // 2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)（
      // 3）采用此代码中的IVParameterSpec
      cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
      //加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
      byte[] encryptedData = cipher.doFinal(data.getBytes(bm));

      return Base64.encode(encryptedData).replace("\n", "").replace("\r", "");
    } catch (Exception e) {
//      log.error("AesUtil-encrypt-error: param: encrypted:{},dataSecretIV:{},dataSecret:{}",data,dataSecretIV,dataSecret,e);
      return "";
    }
  }

  /**
   * 解密
   *
   * @param encrypted
   * @return
   */
  public static String decrypt (String encrypted, String dataSecretIV, String dataSecret) {
    try {
      if (encrypted == null || encrypted.isEmpty()) {
        return "";
      }
      byte[] byteMi =Base64.decode(encrypted);
      IvParameterSpec zeroIv = new IvParameterSpec(dataSecretIV.getBytes());
      SecretKeySpec key = new SecretKeySpec(dataSecret.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      //与加密时不同MODE:Cipher.DECRYPT_MODE
      cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
      byte[] decryptedData = cipher.doFinal(byteMi);
      return new String(decryptedData, bm);
    } catch (Exception e) {
//      log.error("AesUtil-error: param: encrypted:{},dataSecretIV:{},dataSecret:{}",encrypted,dataSecretIV,dataSecret,e);
      return "";
    }
  }

  public static void main(String[] args) {
    String s="ld1SSEB6nXDCMPKffLwrem8F17dQH5hAyI+DEDJaxv0rgTSKl6Dk17031pBtmh1b4JJwI+z++BJQh7RCe6+ydg==";
    String decrypt = decrypt(s, "sSsZYLus0nrIyQkr", "zIicJFCLyYcjpJBd");
    System.out.println(decrypt);




  }
}
