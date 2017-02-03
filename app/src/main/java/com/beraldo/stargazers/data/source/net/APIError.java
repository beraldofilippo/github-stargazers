package com.beraldo.stargazers.data.source.net;

import com.beraldo.stargazers.data.source.GenericError;

public class APIError extends GenericError {
    private String message;
    private String documentation_url;

    public APIError(String m, String d) {
        super(null);
        message = m;
        documentation_url = d;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDocumentation() {
        return documentation_url;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDocumentation_url(String documentation_url) {
        this.documentation_url = documentation_url;
    }

    public String asJson() {
        return "{\"message\": " + getMessage() + ",\"documentation_url\": " + getDocumentation() + "}";
    }
}
