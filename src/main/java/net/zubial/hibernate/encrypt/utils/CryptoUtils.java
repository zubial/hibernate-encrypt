package net.zubial.hibernate.encrypt.utils;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import net.zubial.hibernate.encrypt.enums.PBEErrorCodeEnum;
import net.zubial.hibernate.encrypt.exceptions.PBEException;

public class CryptoUtils {

    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_MD = "MD5";

    private CryptoUtils() {

    }

    public static byte[] hashMD5(final String plaintext)
            throws PBEException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(DEFAULT_MD);
            md.update(plaintext.getBytes(DEFAULT_ENCODING));

            return md.digest();

        } catch (Exception e) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_INITIALIZATION_FAIL, e);
        }
    }

    public static char[] normalizeWithJavaNormalizer(final char[] message) {
        final String messageStr = new String(message);
        final String result =
                java.text.Normalizer.normalize(messageStr, java.text.Normalizer.Form.NFC);
        return result.toCharArray();
    }

    public static void cleanPassword(final char[] password) {
        if (password != null) {
            synchronized (password) {
                final int pwdLength = password.length;
                for (int i = 0; i < pwdLength; i++) {
                    password[i] = (char) 0;
                }
            }
        }
    }

    public static String base64EncodeToString(byte[] src) {
        if (src == null) {
            return null;
        }
        if (src.length == 0) {
            return "";
        }
        return DatatypeConverter.printBase64Binary(src);
    }

    public static byte[] base64Decode(String src) {
        if (src == null) {
            return null;
        }
        return DatatypeConverter.parseBase64Binary(src);
    }
}
