package com.kakeibo.kakeibo.util.user;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PasswordUtil {

  /**
   * パスワードのハッシュ化処理
   * @param password ユーザ入力のパスワード
   * @return hashPw SHA256でハッシュ化されたパスワードp
   */
  public static String hashSHA256(String password) {
    byte[] byteHashPw;
    String hashPw = "";
    try {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        md.update(password.getBytes("utf8"));
        byteHashPw = md.digest();
        hashPw = String.format("%064x", new BigInteger(1, byteHashPw));
        return hashPw;
    } catch(Exception e) {
        e.printStackTrace();
    }
    return hashPw;
  }
}
