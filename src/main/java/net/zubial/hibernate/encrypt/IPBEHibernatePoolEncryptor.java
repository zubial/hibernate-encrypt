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
