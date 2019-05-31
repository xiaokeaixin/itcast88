package com.itheima.test.md5;

import org.apache.shiro.crypto.hash.Md5Hash;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Test {

    /**
     * MD5哈希
     * 他是在MD5的基础上，在进行散列的算法 他是apache shiro提供的
     * 使用一个秘药对密码进行加密，且打散
     * md5不仅能加盐，还能撒盐
     * cgx@export.com
     * d244cf08243d25b2855665d8f833916f
     * @param args
     */
    public static void main(String[] args) {
        //1.创建对象并且加密
        Md5Hash md5Hash = new Md5Hash("123456", "cgx@export.com", 2);
        //2.输出加密结果
        System.out.println(md5Hash);
    }



    /**
     * 把乱码转换成可见字符
     * 用Base64编码，可以编码可以解码
     * 运行结果每次都是一样的
     * @param args
     * @throws NoSuchAlgorithmException
     */
    /*public static void main(String[] args) throws NoSuchAlgorithmException {
        //1.创建对象
        MessageDigest md5 = MessageDigest.getInstance("md5");
        //2.创建一个密码对象
        String password = "1234";
        //3.对密码进行加密
        byte[] bytes = md5.digest(password.getBytes());
        //4.对加密的字节数组进行base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(bytes);
        //5.输出加密后且编码的内容
        System.out.println(str);
    }*/
}
