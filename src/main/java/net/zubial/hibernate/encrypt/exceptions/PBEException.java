package net.zubial.hibernate.encrypt.exceptions;

import net.zubial.hibernate.encrypt.enums.PBEErrorCodeEnum;

public class PBEException extends Exception {

    private static final String DEFAULT_MESSAGE = "PBEException";
    private static final String DEFAULT_CODE = "-1";

    private final String errorCode;

    public PBEException() {
        super(DEFAULT_MESSAGE);

        this.errorCode = DEFAULT_CODE;
    }

    public PBEException(final String errorMessage) {
        super(errorMessage);

        this.errorCode = DEFAULT_CODE;
    }

    public PBEException(final PBEErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.label());

        this.errorCode = errorCodeEnum.name();
    }

    public PBEException(final PBEErrorCodeEnum errorCodeEnum, final String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCodeEnum.name();
    }

    public PBEException(final PBEErrorCodeEnum errorCodeEnum, final Throwable runtimeError, final Object... args) {
        super(errorCodeEnum.label(args), runtimeError);

        this.errorCode = errorCodeEnum.name();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return getMessage();
    }
}
