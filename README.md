# ChangeLogs

## 1.x
- Initial commit

# Usage
- Add this library /bin/hibernate-encrypt-1.0-SNAPSHOT.jar

- Declare the Encryptor pool as a new bean (Spring applicationContext)
```
    <bean id="hibernatePoolEncryptor"
          class="net.zubial.hibernate.encrypt.impl.PBEHibernatePoolStringEncryptor">
        <property name="registeredName">
            <value>hibernatePoolEncryptor</value>
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

- Declare new Hibernate type (package-info.java inside hibernate entities)
```
@TypeDefs({
        @TypeDef(name = "encryptedString", typeClass = PBEHibernateStringType.class, parameters = {
                @Parameter(name = "encryptorRegisteredName", value = "hibernatePoolEncryptor")
        }),
        @TypeDef(name = "encryptedNationalized", typeClass = PBEHibernateNationalizedType.class, parameters = {
                @Parameter(name = "encryptorRegisteredName", value = "hibernatePoolEncryptor")
        })
})
```

- Declare encrypted fields (Hibernate mapping)
```
    @Column(name = "USER_NAME")
    @Type(type = "encryptedString")
    private String userName;
```

