/*
 * Copyright (c) 2017 SBE S.A. All rights reserved.
 * Deposit IDDN.FR.001.210001.000.S.P.2016.000.30000
 * Redistribution and use of this software, in source and binary forms, with or without modification, are not permitted.
 * In order to obtain a license for this software, you should contact :
 * SBE, 35 rue Winston Churchill, 59160 Lille, France
 * +33 972 468 034 or sales@sbeglobalservice.com
 * This software is provided by the Copyright Holders "as is" and any express or implied warranties, including,
 * but not limited to, the implied warranties of merchantability and fitness for a particular purpose are disclaimed.
 *
 */
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
