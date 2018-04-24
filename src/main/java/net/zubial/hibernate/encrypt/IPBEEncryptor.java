package net.zubial.hibernate.encrypt;

import net.zubial.hibernate.encrypt.exceptions.PBEException;

public interface IPBEEncryptor {

    boolean isInitialized();

    byte[] encrypt(byte[] message)
            throws PBEException;

    byte[] decrypt(byte[] encryptedMessage)
            throws PBEException;
}
