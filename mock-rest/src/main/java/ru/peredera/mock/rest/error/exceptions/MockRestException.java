package ru.peredera.mock.rest.error.exceptions;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MockRestException extends RuntimeException {

    private String errorCode = "DEFAULT_ERROR";
    private String message = "DEFAULT_MESSAGE";

    @Override
    public String toString() {
        return String.format("[%s] : %s", this.getErrorCode(), this.getMessage());
    }
}
