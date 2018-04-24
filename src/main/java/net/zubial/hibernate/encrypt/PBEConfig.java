package net.zubial.hibernate.encrypt;

public class PBEConfig {

    public static final String DEFAULT_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String DEFAULT_MESSAGE_CHARSET = "UTF-8";

    private String algorithm = DEFAULT_ALGORITHM;
    private String messageCharset = DEFAULT_MESSAGE_CHARSET;

    private char[] password = null;
    private String passwordPathfile = null;

    private boolean enable = false;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getMessageCharset() {
        return messageCharset;
    }

    public void setMessageCharset(String messageCharset) {
        this.messageCharset = messageCharset;
    }

    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getPasswordPathfile() {
        return passwordPathfile;
    }

    public void setPasswordPathfile(String passwordPathfile) {
        this.passwordPathfile = passwordPathfile;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
