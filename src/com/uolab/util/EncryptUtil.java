//package com.uolab.util;
//
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//public class EncryptUtil {
//
//    // MD5加密
//    public static String md5(String input) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] messageDigest = md.digest(input.getBytes());
//            BigInteger no = new BigInteger(1, messageDigest);
//            String hashtext = no.toString(16);
//
//            // 补齐32位
//            while (hashtext.length() < 32) {
//                hashtext = "0" + hashtext;
//            }
//            return hashtext;
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // 简单的手机号加密（实际项目应使用更安全的加密方式）
//    public static String encryptPhone(String phone) {
//        if (phone == null || phone.length() < 11) {
//            return phone;
//        }
//        // 这里使用简单的加密，实际项目中应该使用AES等加密算法
//        return "ENCRYPTED_" + phone;
//    }
//
//    // 手机号解密
//    public static String decryptPhone(String encryptedPhone) {
//        if (encryptedPhone == null || !encryptedPhone.startsWith("ENCRYPTED_")) {
//            return encryptedPhone;
//        }
//        return encryptedPhone.substring(10);
//    }
//}