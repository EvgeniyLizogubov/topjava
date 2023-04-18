package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR("App error"),
    DATA_NOT_FOUND("Data not fount"),
    DATA_ERROR("Data error"),
    VALIDATION_ERROR("Validation error");

    private final String error;

    ErrorType(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
