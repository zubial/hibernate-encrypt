package net.zubial.hibernate.encrypt.impl;

import java.util.HashMap;

import net.zubial.hibernate.encrypt.IPBEHibernatePoolEncryptor;

public final class PBEHibernateRegistry {

    // The singleton instance
    private static final PBEHibernateRegistry instance =
            new PBEHibernateRegistry();

    // Registry maps
    private final HashMap<String, IPBEHibernatePoolEncryptor> hibernatePool = new HashMap<>();

    // The registry cannot be externally instantiated.
    private PBEHibernateRegistry() {
        super();
    }

    public static PBEHibernateRegistry getInstance() {
        return instance;
    }

    public synchronized void registerPBEHibernatePoolEncryptor(
            final String registeredName, final IPBEHibernatePoolEncryptor encryptor) {
        this.hibernatePool.put(registeredName, encryptor);
    }

    public synchronized void unregisterPBEHibernatePoolEncryptor(final String name) {
        this.hibernatePool.remove(name);
    }

    public synchronized IPBEHibernatePoolEncryptor getPBEHibernatePoolEncryptor(
            final String registeredName) {
        return this.hibernatePool.get(registeredName);
    }
}
