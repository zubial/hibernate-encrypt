/*
 * Package info
 */

@TypeDefs({
        @TypeDef(name = "encryptedString", typeClass = PBEHibernateStringType.class, parameters = {
                @Parameter(name = "encryptorRegisteredName", value = "hibernatePoolEncryptor")
        }),
        @TypeDef(name = "encryptedNationalized", typeClass = PBEHibernateNationalizedType.class, parameters = {
                @Parameter(name = "encryptorRegisteredName", value = "hibernatePoolEncryptor")
        })
})
package net.zubial.hibernate.encrypt.types;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;