package com.mape.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.log4j.Log4j;

@Log4j
public final class PasswordEncryption {

  private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
  private static final String SALT = "MAPEconf100";

  private PasswordEncryption() {
  }

  private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    int v;
    for (int j = 0; j < bytes.length; j++) {
      v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static String encryptPassword(String password) {
    try {
      MessageDigest md = MessageDigest
          .getInstance("SHA-256");
      md.update(password.getBytes());
      md.update(SALT.getBytes());

      byte[] out = md.digest();
      return bytesToHex(out);
    } catch (NoSuchAlgorithmException e) {
      log.error("Password encoding error", e);
    }
    return "";
  }
}
