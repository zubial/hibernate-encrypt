package net.zubial.hibernate.encrypt.impl;

import net.zubial.hibernate.encrypt.IPBEStringEncryptor;
import net.zubial.hibernate.encrypt.PBEConfig;
import net.zubial.hibernate.encrypt.enums.PBEErrorCodeEnum;
import net.zubial.hibernate.encrypt.exceptions.PBEException;
import net.zubial.hibernate.encrypt.utils.CryptoUtils;

public final class PBEStringEncryptor extends AbstractPBEEncryptor implements IPBEStringEncryptor {

    public PBEStringEncryptor(PBEConfig config)
            throws PBEException {
        super(config);
    }

    @Override
    public String encrypt(final String message)
            throws PBEException {

        if (message == null) {
            return null;
        }

        // Check initialization
        if (!isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_INITIALIZATION_FAIL);
        }

        try {
            if (this.config.isEnable()) {
                // The input String is converted into bytes using MESSAGE_CHARSET
                // as a fixed charset to avoid problems with different platforms
                // having different default charsets (see MESSAGE_CHARSET doc).
                final byte[] messageBytes = message.getBytes(this.config.getMessageCharset());

                // The StandardPBEByteEncryptor does its job.
                byte[] encryptedMessage = this.encrypt(messageBytes);

                return CryptoUtils.base64EncodeToString(encryptedMessage);
            } else {
                return message;
            }

        } catch (PBEException e) {
            throw e;
        } catch (Exception e) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ENCRYPT_FAIL, e);
        }
    }

    @Override
    public String decrypt(final String encryptedMessage)
            throws PBEException {

        if (encryptedMessage == null) {
            return null;
        }

        // Check initialization
        if (!isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_INITIALIZATION_FAIL);
        }

        try {
            if (this.config.isEnable()) {
                byte[] encryptedMessageBytes = CryptoUtils.base64Decode(encryptedMessage);

                // Let the byte encyptor decrypt
                final byte[] message = this.decrypt(encryptedMessageBytes);

                // Return the resulting decrypted String, using MESSAGE_CHARSET
                // as charset to maintain between encryption and decyption
                // processes.
                return new String(message, this.config.getMessageCharset());
            } else {
                return encryptedMessage;
            }
        } catch (PBEException e) {
            throw e;
        } catch (Exception e) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_DECRYPT_FAIL, e);
        }
    }
}
