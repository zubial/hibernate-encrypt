package net.zubial.hibernate.encrypt.enums;

public enum PBEErrorCodeEnum {

    // COMMON
    COMMON_OK("Operation Done"),
    COMMON_PARAM_MISSING("Parameters missing : %s"),

    // CRYPTO
    CRYPTO_INITIALIZATION_FAIL("Crypto Initialization failure"),
    CRYPTO_PARAMS_MISSING("Crypto Parameters missing : %s"),
    CRYPTO_ALREADY_INIT("Crypto Already Initialized"),
    CRYPTO_PASSWORD_CLEANED("Crypto Password Already cleaned"),

    CRYPTO_ENCRYPT_FAIL("Crypto Encrypt failure"),
    CRYPTO_DECRYPT_FAIL("Crypto Decrypt failure");

    private final String label;

    PBEErrorCodeEnum(final String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public String label(final Object... args) {
        return String.format(this.label, args);
    }

}
