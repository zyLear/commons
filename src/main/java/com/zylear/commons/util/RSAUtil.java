package com.zylear.commons.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RSAUtil {


    public static final String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCl5CFmG/39+3NQwsg9sPq7Ctii\n" +
            "nTzTOhr3YLrHfLMTf6/EnQajgjWJ2vBMCOXjrczvu5O6r///SrmItzo/W/FMRQMeIxmD+Kzb45va\n" +
            "3KzkvJuMUaHtbh8JqlTfObKTotCqHgIMBLYLG1vpOPAsb4zKxi9xjz2H37Ms3UkwzwtkpF+y9Xu1\n" +
            "cts3oXDTZAT9oSv9QXzU3mwgAGdkkSOyx/+cuBn+ad8Yzm4+pG6uHZko4aLmnEm600jfQldAJijy\n" +
            "C/nQoZsz2Y8DY21OBcAFlg5Hw5iCnmta+mjVT4xBIEi7wj0hWQFkkHFsAEUn/TohoeJxkRKF/A6g\n" +
            "5rjl8fLekX7DAgMBAAECggEABvqK4SXKUJX0avBWximc0qOJK4Yb2fL20XNTSDMZwXc8qLPGZLiE\n" +
            "khEQzNIPAb39AhTNCSyAQ3gcSAEVRTcT9pZspCbr2t5qPzHbC6D+8PxyOrvwaPGaAqkUuvNNbOKL\n" +
            "jqyGf4TvN5ZmDnCjwerLbHhb5JYXC5FAk2GXw6LiItCprFq56E2Kpx/F2PGnyecwBgnNetDDfajz\n" +
            "1suzC2ShueU3c+WgkaCaiR/pohbgEMtQj8rvibODuSiV7GFlkCKrw1+J2BXGYb7ogncFleNxAT2r\n" +
            "ns8AZZiqzdcWXUel20jT1i1Etu+9Md9VkpfHARB90ofGAXZkcjDQnY3ImrEWMQKBgQD/e55cA2CD\n" +
            "Svg18YElbtY2Azu12NA99ZRYCeVtrTEtwKvEkD7x7s5e/OUb/ma0sICz401IAcfbOWzKk+78Gii7\n" +
            "uNODLl+G5f0yckZEfqjidK0hSX3sEiS8c/i3yZEd0mmGh6myKIG0/VXacv6FRWc/xqqLCuJZNWM3\n" +
            "vh9V+URQuwKBgQCmOhbAVM3SJu8m8edDI4xKdAyxelCiwG8T7Wx97dkzH0e6cg9l8bbgBOLQ4CMl\n" +
            "f8WOEBOMkvgrAjIeLc+OAzqgyxRFjhniRlI+EtqvKZrpdoq7hQYzRiLMXjE0cc0WxLLFXSJGBeGY\n" +
            "uChJSUBMmWusUopeI4BqD5A+W+CGwI9NmQKBgDFJRNSVZjIN7XJio7p39mU3HHbJW+sIDFUrm5Ib\n" +
            "/kfISprCBRKhMjjK6twKqP+kqT+K0ybYCRk2q9Es1sbVuRP15QACmzUn+z+XIg0mrif5wEiSp/M5\n" +
            "0NJ2QP4bqZ4qMGdelpMjE1q6IKzi0E0fhXqdNdta/y2cigZ/7AWm79HvAoGAGEiIJlS9NKQnb8ML\n" +
            "C83XotDNQegie4QtNfYLUWI7xrIdImmjQjtLgfJ0yBrWR5Yg4B4NHQ4jXvUVKT98AL0MBY1lSrv1\n" +
            "oA6x2QucEta7ILwGjFpki30FfGGOQqEVWuwlSZvPc/dL+6l7iSlAUXBgQG5WdGYvTk8zvUhixoy+\n" +
            "4pECgYAEymmMpAWRue7vq4uA9aAao1yswvYtVpBXTxx/nFStQsSHuPkyJZ7DADED1PFW8PwWzJxw\n" +
            "pe9Ow1sajxwVyDilUP65lj6rwqAUWiBcy0c14iLIzuZLFPu+eXOz5ZqTtrKTwILl4v6uJLmrgrli\n" +
            "3hUkiwR4aNy50yYXt6AKUr1+OQ==";

    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApeQhZhv9/ftzUMLIPbD6uwrYop080zoa\n" +
            "92C6x3yzE3+vxJ0Go4I1idrwTAjl463M77uTuq///0q5iLc6P1vxTEUDHiMZg/is2+Ob2tys5Lyb\n" +
            "jFGh7W4fCapU3zmyk6LQqh4CDAS2Cxtb6TjwLG+MysYvcY89h9+zLN1JMM8LZKRfsvV7tXLbN6Fw\n" +
            "02QE/aEr/UF81N5sIABnZJEjssf/nLgZ/mnfGM5uPqRurh2ZKOGi5pxJutNI30JXQCYo8gv50KGb\n" +
            "M9mPA2NtTgXABZYOR8OYgp5rWvpo1U+MQSBIu8I9IVkBZJBxbABFJ/06IaHicZEShfwOoOa45fHy\n" +
            "3pF+wwIDAQAB";


    public static final String JWT_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCCND7JBL13cSbqniWnBsjqEV5y\n" +
            "0I/c+4iCCsozrq3rdgW5TFRDhJpGztvw/wDglnPY9KWdZue/j/qg//PyL5M2S8CqaPyjWmBEIl61\n" +
            "BICYLVm1klGctemO5PfP/GLFZfqFuqs5J6nO1Z8Tz/qAEJ37sdX+Eszo5LV9PhhcqCpKHVhSrmzA\n" +
            "1g4v2E9uiubl05yVa9kQPhJea8XeLY31qM7wbpTUYbJ1acMWU+JKdoP8UtSLDX/+UJGy3/KdIUyM\n" +
            "YamHLrk2Nz9f7xjq47H04DGgGcDSunCm5kn1zhRoz0Q6Q5SCzaC8VX7/Xu425OVLCZSU+90pg9fp\n" +
            "Gk/cP7j/v9P1AgMBAAECggEALQ4TI9BGF8zJ4Kn08YVBhO2ZtzVAcwF/TTCao6OwfygJ9wLYrbx2\n" +
            "/kWaihxTm2O0lsChhoQTcZBK8+dd7NJVqJYyB0XpFkWxxiH2j8ENFpuqzU/iec3hEhddQVVegFw3\n" +
            "gosDsPVupyn4sAxp7GLTqELzf8XVrklmG/6ew4FVkOty5lzG7OZt+mW00vYwb3VeJbEy4L0EjLqA\n" +
            "/+XbDDfOFag/KgFYOrS9WamPn/d8TgGrJNL+ihZHlOqEZqGB08tmbPyNTtcaWmvmlLcHmD/ul62h\n" +
            "Azt09l3UgPyp7jGJhUrQSLWTsc6U1sdWwY1lz+s/9CUkWSbJChdEKpkJajlOCQKBgQDTpf6E9Nba\n" +
            "j/lYBrgNwHeGIuMiBWfFW1xwHerfc8rALmzKEEN6spb3l6LttRSuvbLFNKunKmtFRAbKg9B80fxB\n" +
            "gh4hMFy7eRI1jqzMOehxQMp4Rfqr+PDdZYbPHVInq/1MOX4uNFszrNeNnPLEiHHMS3KVO4GY2scS\n" +
            "/AMFKANfswKBgQCdfR8DwDJIi0MDf8+9SjrnbiOQWJ2pzlp5X3ako+0ogGrPjK+Wj6FrIcbc82cR\n" +
            "WaZvqu5rEagvvy0JpvbnpR1iJTnWrkXgsswrfJJUnSYjN/ShEHUr/l5rbO6rG3Nzq7vSnuavkEhs\n" +
            "/eiNxIv5dhzf+jBBJ0BJHYnfAKTfVZJptwKBgA5LJZdBGcmE86f4PDHhj7DlZkWD6/AuLV39B0G0\n" +
            "BjtYZ0x2MLh2my95dC1tj0oF0a7z0avmd3wkDflbUZc8bzbVXMeNBpY6pPos+oqIVqRfZc0dnMJE\n" +
            "e6/YDXSrXEMFoIOnVlg0fPxObfhIYzTWqijL558gd64lCFDF6EZO33t1AoGAAoy1BNWGut/uTM8z\n" +
            "6Meb3oVM9BhLLSWXF2HxRMcWntI9aHDJovFeVqKsWoViRMijxikJRRh7FZLNwts+Ig/tQtqBbjVS\n" +
            "Ekoyvf19TPtiU62jOzxN+VFb6/dOgdym6Nd96fICoBMA6SrPKgRlk7tSyll9kIB3LPfadI7JHIiB\n" +
            "H08CgYAtx3yGYgg1Sl5n0L9dxhJ+OFt/KmFzVX0M9qtzvmL//b3jT1ceoFtQAR3nSZs1h+vtDyOD\n" +
            "QbGO/9IkGzN5Itl7WOewasNoN3UGKKQjTQBAMzId8j/ZO2TFuEelh6YDQ8vdTLCJLs2MX199xwnj\n" +
            "BLSLdCVuKhv0SnslTl9UcKy5rQ==";


    public static final String JWT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgjQ+yQS9d3Em6p4lpwbI6hFectCP3PuI\n" +
            "ggrKM66t63YFuUxUQ4SaRs7b8P8A4JZz2PSlnWbnv4/6oP/z8i+TNkvAqmj8o1pgRCJetQSAmC1Z\n" +
            "tZJRnLXpjuT3z/xixWX6hbqrOSepztWfE8/6gBCd+7HV/hLM6OS1fT4YXKgqSh1YUq5swNYOL9hP\n" +
            "borm5dOclWvZED4SXmvF3i2N9ajO8G6U1GGydWnDFlPiSnaD/FLUiw1//lCRst/ynSFMjGGphy65\n" +
            "Njc/X+8Y6uOx9OAxoBnA0rpwpuZJ9c4UaM9EOkOUgs2gvFV+/17uNuTlSwmUlPvdKYPX6RpP3D+4\n" +
            "/7/T9QIDAQAB";

    //生成秘钥对
    @SneakyThrows
    public static KeyPair getKeyPair() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    //获取公钥(Base64编码)
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    //获取私钥(Base64编码)
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    //将Base64编码后的公钥转换成PublicKey对象
    @SneakyThrows
    public static PublicKey string2PublicKey(String pubStr) {
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);

    }

    //将Base64编码后的私钥转换成PrivateKey对象
    @SneakyThrows
    public static PrivateKey string2PrivateKey(String priStr) {
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    //公钥加密
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    //公钥加密
    @SneakyThrows
    public static String publicEncrypt(String string) {
        return publicEncrypt(string, PUBLIC_KEY);
    }

    @SneakyThrows
    public static String publicEncrypt(String string, String publicKeyString) {
        PublicKey publicKey = string2PublicKey(publicKeyString);
        byte[] publicEncrypt = publicEncrypt(string.getBytes(StandardCharsets.UTF_8), publicKey);
        return byte2Base64(publicEncrypt);
    }


    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }

    public static String privateDecrypt(String base64Code) {

        return privateDecrypt(base64Code, PRIVATE_KEY);
    }

    public static String privateDecrypt(String base64Code, String privateKeyString) {
        try {
            PrivateKey privateKey = string2PrivateKey(privateKeyString);

            //加密后的内容Base64解码
            byte[] base642Byte = base642Byte(base64Code);
            //用私钥解密
            byte[] privateDecrypt = privateDecrypt(base642Byte, privateKey);
            //解密后的明文
            return new String(privateDecrypt, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("privateDecrypt error. ", e);
            throw new RuntimeException("privateDecrypt error");
        }
    }

    //私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }


    public static void main(String[] args) {
        String string = "testss,?2问问";
        String s = publicEncrypt(string);
        System.out.println(s);
        System.out.println();
        System.out.println(privateDecrypt(s));

//        System.out.println(PUBLIC_KEY);
    }
}
