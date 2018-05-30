# Java Hibernate Encryption library
This library brings new types field for hibernate mapping. These types allow encoding and decoding Text fields.

Searches on these fields remain possible, but only in equal (case-sensitive). It is advisable to manage the case at insertion.

Used with Spring 4 / Hibernate 5 / Oracle db

# ChangeLogs

## 1.x
- Initial commit

# Usage
- Dependency
```
    <dependency>
        <groupId>net.zubial</groupId>
        <artifactId>hibernate-encrypt</artifactId>
        <version>1.4</version>
    </dependency>
```

- Declare the Encryptor pool as a new bean (Spring applicationContext)
```
    <bean id="hibernatePoolEncryptor"
          class="net.zubial.hibernate.encrypt.impl.PBEHibernatePoolStringEncryptor">
        <property name="registeredName">
            <value>hibernatePoolEncryptor</value>
        </property>
        <property name="algorithm">
            <value>[algorithm, default AES/CBC/PKCS5Padding]</value>
        </property>
        <property name="messageCharset">
            <value>[messageCharset, default UTF-8]</value>
        </property>
        <property name="passwordPathfile">
            <value>[textfile included password]</value>
        </property>
        <property name="password">
            <value>[if you don't use a textfile]</value>
        </property>
        <property name="poolSize">
            <value>[size of the pool, default 4]</value>
        </property>
        <property name="enable">
            <value>[true or false]</value>
        </property>
    </bean>
```

- Declare new Hibernate type (package-info.java inside hibernate entities package)
```
    @TypeDefs({
            @TypeDef(name = "encryptedString", typeClass = PBEHibernateStringType.class, parameters = {
                    @Parameter(name = "encryptorRegisteredName", value = "hibernatePoolEncryptor")
            }),
            @TypeDef(name = "encryptedNationalized", typeClass = PBEHibernateNationalizedType.class, parameters = {
                    @Parameter(name = "encryptorRegisteredName", value = "hibernatePoolEncryptor")
            })
    })
    package com.your.package.hibernate;
```

- Declare encrypted fields (Hibernate mapping)
```
    @Column(name = "USER_NAME")
    @Type(type = "encryptedString")
    private String userName;
```

