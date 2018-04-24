package net.zubial.hibernate.encrypt;

import net.zubial.hibernate.encrypt.exceptions.PBEException;

public interface IPBEStringEncryptor extends IPBEEncryptor {

    String encrypt(String message)
            throws PBEException;

    String decrypt(String encryptedMessage)
            throws PBEException;
}
