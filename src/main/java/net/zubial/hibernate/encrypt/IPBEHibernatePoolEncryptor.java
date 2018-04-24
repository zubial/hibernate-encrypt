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

import net.zubial.hibernate.encrypt.exceptions.PBEException;

public interface IPBEHibernatePoolEncryptor {

    String getRegisteredName();

    void setRegisteredName(String registeredName);

    void setAlgorithm(String algorithm)
            throws PBEException;

    void setMessageCharset(String messageCharset)
            throws PBEException;

    void setPassword(String password)
            throws PBEException;

    void setPasswordCharArray(char[] password)
            throws PBEException;

    void setPasswordPathfile(String pathfile)
            throws PBEException;

    void setPoolSize(final int poolSize)
            throws PBEException;

    void setEnable(final boolean enable)
            throws PBEException;

    boolean isInitialized();

    IPBEEncryptor getEncryptor()
            throws PBEException;
}
