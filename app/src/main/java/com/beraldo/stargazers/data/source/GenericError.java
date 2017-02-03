package com.beraldo.stargazers.data.source;

public class GenericError {
    private Throwable throwable;

    public GenericError(Throwable t) {
        throwable = t;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getMessage() {
        return throwable.getLocalizedMessage();
    }
}
