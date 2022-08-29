package com.zylear.commons.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * @author xiezongyu
 * @date 2021/3/4
 */
public class JwtUtil {

    public static PublicKey publicKey = RSAUtil.string2PublicKey(RSAUtil.JWT_PUBLIC_KEY);
    public static PrivateKey privateKey = RSAUtil.string2PrivateKey(RSAUtil.JWT_PRIVATE_KEY);

    public static Map<String, Object> parse(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token).getBody();
    }

    public static String createJwt(Map<String, Object> map, Long expireTimeMillis) {
        long nowTime = System.currentTimeMillis();
        long expirationTime = nowTime + expireTimeMillis;
        //2.创建jwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder();
        //3.根据map设置claims
        jwtBuilder.setClaims(map);
        //设置当前时间
        jwtBuilder.setIssuedAt(new Date());

        //设置加密方式
        jwtBuilder.signWith(SignatureAlgorithm.RS256, privateKey);
        //设置过期时间
        jwtBuilder.setExpiration(new Date(expirationTime));
        //4.创建token
        return jwtBuilder.compact();
    }

}
