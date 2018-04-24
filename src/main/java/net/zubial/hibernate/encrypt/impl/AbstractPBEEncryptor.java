package net.zubial.hibernate.encrypt.impl;

import java.io.File;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.zubial.hibernate.encrypt.IPBEEncryptor;
import net.zubial.hibernate.encrypt.PBEConfig;
import net.zubial.hibernate.encrypt.enums.PBEErrorCodeEnum;
import net.zubial.hibernate.encrypt.exceptions.PBEException;
import net.zubial.hibernate.encrypt.utils.CryptoUtils;
import net.zubial.hibernate.encrypt.utils.StringUtils;

public abstract class AbstractPBEEncryptor implements IPBEEncryptor {

    private static final Logger LOGGER = LogManager.getLogger(AbstractPBEEncryptor.class); // NOSONAR

    private static final String KEY = "GYUfds60dnkz96";
    protected PBEConfig config = null;

    // Encryption key generated.
    private SecretKeySpec secretKeySpec = null;
    private IvParameterSpec ivParameterSpec = null;
    private boolean initialized = false;

    // Ciphers to be used for encryption and decryption.
    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    public AbstractPBEEncryptor(PBEConfig config)
            throws PBEException {
        this.config = config;
        initialize();
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    private synchronized void initialize()
            throws PBEException {

        if (this.initialized) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }

        // Initialize config
        if (this.config == null) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING, "Configuration missing");
        }

        if (this.config.isEnable()) {
            String configAlgorithm = this.config.getAlgorithm();
            if (StringUtils.isBlank(configAlgorithm)) {
                throw new PBEException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING, "Algorithm cannot be set empty");
            }

            // Initialize password
            char[] configPassword;
            if (StringUtils.isNotBlank(this.config.getPasswordPathfile())) {
                try {
                    File pathFile = new File(this.config.getPasswordPathfile());
                    String fetchedPassword = FileUtils.readFileToString(pathFile, "UTF-8");
                    fetchedPassword = StringUtils.noReturnString(fetchedPassword);

                    configPassword = CryptoUtils.normalizeWithJavaNormalizer(fetchedPassword.toCharArray());

                } catch (IOException e) {
                    throw new PBEException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING, "Password file not found");
                }
            } else {
                configPassword = CryptoUtils.normalizeWithJavaNormalizer(this.config.getPassword());
            }

            if (configPassword.length == 0) {
                throw new PBEException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING, "Password cannot be set empty");
            }

            try {
                byte[] keyPassword = CryptoUtils.hashMD5(new String(configPassword) + KEY);
                CryptoUtils.cleanPassword(configPassword);

                secretKeySpec = new SecretKeySpec(keyPassword, "AES");
                ivParameterSpec = new IvParameterSpec(new byte[16]);

                // Initialize Ciphers
                this.encryptCipher = Cipher.getInstance(configAlgorithm);
                this.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

                this.decryptCipher = Cipher.getInstance(configAlgorithm);
                this.decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            } catch (Exception e) {
                throw new PBEException(PBEErrorCodeEnum.CRYPTO_INITIALIZATION_FAIL, e);
            }
        }
        this.initialized = true;
    }

    @Override
    public byte[] encrypt(final byte[] message)
            throws PBEException {

        if (message == null) {
            return null;
        }

        // Check initialization
        if (!isInitialized()) {
            initialize();
        }

        try {
            byte[] encryptedMessage;
            if (config.isEnable()) {
                synchronized (this.encryptCipher) {
                    encryptedMessage = this.encryptCipher.doFinal(message);
                }
            } else {
                encryptedMessage = message;
            }

            return encryptedMessage;

        } catch (Exception e) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ENCRYPT_FAIL, e);
        }
    }

    @Override
    public byte[] decrypt(final byte[] encryptedMessage)
            throws PBEException {

        if (encryptedMessage == null) {
            return null;
        }

        // Check initialization
        if (!isInitialized()) {
            initialize();
        }

        try {

            byte[] decryptedMessage;

            if (config.isEnable()) {
                synchronized (this.decryptCipher) {
                    decryptedMessage = this.decryptCipher.doFinal(encryptedMessage);
                }
            } else {
                decryptedMessage = encryptedMessage;
            }
            // Return the results
            return decryptedMessage;

        } catch (Exception e) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_DECRYPT_FAIL, e);
        }
    }
}
