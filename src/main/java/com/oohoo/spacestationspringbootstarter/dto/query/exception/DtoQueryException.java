package com.oohoo.spacestationspringbootstarter.dto.query.exception;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 20 October 2022
 */
public class DtoQueryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DtoQueryException(String message) {
        super(message);
    }

    public DtoQueryException(Throwable throwable) {
        super(throwable);
    }

    public DtoQueryException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
