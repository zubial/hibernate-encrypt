package net.zubial.hibernate.encrypt.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import net.zubial.hibernate.encrypt.IPBEHibernatePoolEncryptor;
import net.zubial.hibernate.encrypt.IPBEStringEncryptor;
import net.zubial.hibernate.encrypt.enums.PBEErrorCodeEnum;
import net.zubial.hibernate.encrypt.exceptions.PBEException;
import net.zubial.hibernate.encrypt.impl.PBEHibernateRegistry;
import net.zubial.hibernate.encrypt.utils.StringUtils;

public class PBEHibernateNationalizedType
        implements UserType, ParameterizedType {

    private static final int sqlType = Types.NVARCHAR;
    private static final int[] sqlTypes = new int[] { sqlType };

    private IPBEHibernatePoolEncryptor pooledEncryptor = null;

    private boolean initialized = false;
    private String encryptorName = null;

    private String convertToString(final Object object) {
        return object == null ? null : object.toString();
    }

    @Override
    public final int[] sqlTypes() {
        return sqlTypes.clone();
    }

    private Object convertToObject(final String stringValue) {
        return stringValue;
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public final boolean equals(final Object x, final Object y)
            throws HibernateException {
        return x == y || (x != null && x.equals(y));
    }

    @Override
    public final Object deepCopy(final Object value)
            throws HibernateException {
        return value;
    }

    @Override
    public final Object assemble(final Serializable cached, final Object owner)
            throws HibernateException {
        if (cached == null) {
            return null;
        }
        return deepCopy(cached);
    }

    @Override
    public final Serializable disassemble(final Object value)
            throws HibernateException {
        if (value == null) {
            return null;
        }
        return (Serializable) deepCopy(value);
    }

    @Override
    public final boolean isMutable() {
        return false;
    }

    @Override
    public final int hashCode(final Object x)
            throws HibernateException {
        return x.hashCode();
    }

    @Override
    public final Object replace(final Object original, final Object target, final Object owner)
            throws HibernateException {
        return original;
    }

    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names,
            final SessionImplementor session, final Object owner)
            throws HibernateException, SQLException {

        checkInitialization();
        final String message = rs.getNString(names[0]);
        try {
            return rs.wasNull() ? null : convertToObject(((IPBEStringEncryptor) this.pooledEncryptor.getEncryptor()).decrypt(message));
        } catch (PBEException e) {
            throw new HibernateException(e);
        }

    }

    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index,
            final SessionImplementor session)
            throws HibernateException, SQLException {

        checkInitialization();
        if (value == null) {
            st.setNull(index, sqlType);
        } else {
            try {
                st.setNString(index, ((IPBEStringEncryptor) this.pooledEncryptor.getEncryptor()).encrypt(convertToString(value)));
            } catch (PBEException e) {
                throw new HibernateException(e);
            }
        }
    }

    @Override
    public synchronized void setParameterValues(final Properties parameters) {

        final String paramEncryptorName =
                parameters.getProperty(PBEHibernateParameterNaming.ENCRYPTOR_NAME);

        if (paramEncryptorName != null) {
            this.encryptorName = paramEncryptorName;
        } else {
            throw new HibernateException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING.label(PBEHibernateParameterNaming.ENCRYPTOR_NAME));
        }
    }

    protected synchronized final void checkInitialization() {

        if (!this.initialized) {
            if (StringUtils.isBlank(this.encryptorName)) {
                throw new HibernateException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING.label("encryptorName"));
            }

            final PBEHibernateRegistry registry =
                    PBEHibernateRegistry.getInstance();
            final IPBEHibernatePoolEncryptor pbePooledEncryptor;
            pbePooledEncryptor = registry.getPBEHibernatePoolEncryptor(this.encryptorName);
            if (pbePooledEncryptor == null) {
                throw new HibernateException(PBEErrorCodeEnum.CRYPTO_PARAMS_MISSING.label(this.encryptorName + " not found"));
            }

            this.pooledEncryptor = pbePooledEncryptor;

            this.initialized = true;
        }
    }
}
