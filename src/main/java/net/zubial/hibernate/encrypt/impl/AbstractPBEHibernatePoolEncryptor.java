package net.zubial.hibernate.encrypt.impl;

import net.zubial.hibernate.encrypt.IPBEEncryptor;
import net.zubial.hibernate.encrypt.IPBEHibernatePoolEncryptor;
import net.zubial.hibernate.encrypt.IPBEStringEncryptor;
import net.zubial.hibernate.encrypt.PBEConfig;
import net.zubial.hibernate.encrypt.enums.PBEErrorCodeEnum;
import net.zubial.hibernate.encrypt.exceptions.PBEException;

public abstract class AbstractPBEHibernatePoolEncryptor implements IPBEHibernatePoolEncryptor {

    protected String registeredName = null;
    protected PBEConfig config = null;

    protected int poolSize = 1;

    protected IPBEStringEncryptor[] pool;
    protected int roundRobin = 0;

    protected boolean initialized = false;

    public AbstractPBEHibernatePoolEncryptor() {
        config = new PBEConfig();
    }

    @Override
    public String getRegisteredName() {
        return registeredName;
    }

    @Override
    public void setRegisteredName(String registeredName) {
        if (this.registeredName != null) {
            // It had another name before, we have to clean
            PBEHibernateRegistry.getInstance().
                    unregisterPBEHibernatePoolEncryptor(this.registeredName);
        }
        this.registeredName = registeredName;
        PBEHibernateRegistry.getInstance().
                registerPBEHibernatePoolEncryptor(registeredName, this);
    }

    @Override
    public void setAlgorithm(String algorithm)
            throws PBEException {
        if (isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }
        this.config.setAlgorithm(algorithm);
    }

    @Override
    public void setMessageCharset(String messageCharset)
            throws PBEException {
        if (isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }
        this.config.setMessageCharset(messageCharset);
    }

    @Override
    public void setPasswordCharArray(char[] passwordCharArray)
            throws PBEException {
        if (isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }
        this.config.setPassword(passwordCharArray);
    }

    @Override
    public void setPassword(String password)
            throws PBEException {
        if (isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }
        this.config.setPassword(password);
    }

    @Override
    public void setPasswordPathfile(String pathfile)
            throws PBEException {
        if (isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }
        this.config.setPasswordPathfile(pathfile);
    }

    @Override
    public synchronized void setPoolSize(final int poolSize)
            throws PBEException {
        if (isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }

        if (poolSize < 1) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING, "Pool size be > 0");
        }
        this.poolSize = poolSize;
    }

    @Override
    public void setEnable(boolean enable)
            throws PBEException {
        if (isInitialized()) {
            throw new PBEException(PBEErrorCodeEnum.CRYPTO_ALREADY_INIT);
        }
        this.config.setEnable(enable);
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    protected abstract void initialize()
            throws PBEException;

    @Override
    public IPBEEncryptor getEncryptor()
            throws PBEException {

        // Check initialization
        if (!isInitialized()) {
            initialize();
        }

        int poolPosition;
        synchronized (this) {
            poolPosition = this.roundRobin;
            this.roundRobin = (this.roundRobin + 1) % this.poolSize;
        }

        return this.pool[poolPosition];
    }
}
