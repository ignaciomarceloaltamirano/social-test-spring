package com.social.test.exceptions;

import javax.naming.AuthenticationException;

public class ResourceAlreadyExistsException extends AuthenticationException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
