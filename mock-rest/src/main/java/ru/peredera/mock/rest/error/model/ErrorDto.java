package ru.peredera.mock.rest.error.model;

/**
 * Модель хранения информации об ошибке
 *
 * @param httpStatus   статус ответа
 * @param errorCode    код ошибки
 * @param errorMessage расшифровка ошибки
 */
public record ErrorDto(Integer httpStatus, String errorCode, String errorMessage) {
}
