package net.zubial.hibernate.encrypt.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.zubial.hibernate.encrypt.exceptions.PBEException;
import net.zubial.hibernate.encrypt.utils.CryptoUtils;

public final class PBEHibernatePoolStringEncryptor extends AbstractPBEHibernatePoolEncryptor {

    private static final Logger LOGGER = LogManager.getLogger(PBEHibernatePoolStringEncryptor.class); // NOSONAR

    public PBEHibernatePoolStringEncryptor() {
        super();
    }

    protected synchronized void initialize()
            throws PBEException {

        if (!this.initialized) {

            // Create Pool
            this.pool = new PBEStringEncryptor[this.poolSize];
            for (int i = 0; i < this.poolSize; i++) {
                this.pool[i] = new PBEStringEncryptor(this.config);
            }

            // Clean ConfigPassword
            CryptoUtils.cleanPassword(this.config.getPassword());

            this.initialized = true;
        }
    }
}
