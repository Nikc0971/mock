package ru.peredera.mock.rest.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.peredera.mock.rest.error.exceptions.MockRestException;
import ru.peredera.mock.rest.error.model.ErrorDto;

/**
 * Сервис обработки исключений и маппинга кодов ошибок
 */
//TODO Нефункциональные требования - обработка ошибок
@Service
public class ErrorService {

    private final ErrorClient errorClient;

    @Autowired
    public ErrorService(ErrorClient errorClient) {
        this.errorClient = errorClient;
    }

    public ErrorDto getError(Exception ex) {
        MockRestException wrapException = wrapException(ex);
        return errorClient.getError(wrapException.getErrorCode());
    }

    //TODO Нефункциональные требования - обработка ошибок
    //     можно раскрутить логику обработки разных исключений с разными техническими кодами
    private MockRestException wrapException(Exception ex) {
        return ex instanceof MockRestException ? (MockRestException) ex : MockRestException.builder().message(ex.getMessage()).build();
    }
}
