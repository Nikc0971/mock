package ru.peredera.mock.rest.error.model;

/**
 * Модель ответа сервиса в случае ошибки
 *
 * @param errorCode    код ошибки
 * @param errorMessage расшифровка ошибки
 */
public record ErrorRs(String errorCode, String errorMessage) {
}
