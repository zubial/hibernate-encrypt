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
