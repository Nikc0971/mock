package ru.peredera.mock.rest.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.peredera.mock.rest.error.config.ErrorsConfiguration;
import ru.peredera.mock.rest.error.model.ErrorDto;

/**
 * Абстрактный клиент над источником данных для сервиса ошибок
 * Пока сделан через локальный yaml справочник, при желании можно заменить на отдельный микросервис с БД
 */
@Service
public class ErrorClient {

    private final ErrorsConfiguration errorsConfiguration;

    @Autowired
    public ErrorClient(ErrorsConfiguration errorsConfiguration) {
        this.errorsConfiguration = errorsConfiguration;
    }

    public ErrorDto getError(String errorCode) {
        return errorsConfiguration.getErrorsMap().getOrDefault(
                errorCode.toLowerCase(),
                errorsConfiguration.getDefaultError()
        );
    }
}
