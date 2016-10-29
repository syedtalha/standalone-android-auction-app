package com.talhasyed.bidit.credential;

/**
 * Created by Talha Syed on 29-10-2016.
 */

public class LocalAuthException extends Exception {
    private AuthErrorType errorType;

    public LocalAuthException(String errorMessage, AuthErrorType errorType) {
        super(errorMessage);
        this.errorType = errorType;
    }

    public AuthErrorType getErrorType() {
        return errorType;
    }

    public enum AuthErrorType {
        UserName, Password,General
    }


}
