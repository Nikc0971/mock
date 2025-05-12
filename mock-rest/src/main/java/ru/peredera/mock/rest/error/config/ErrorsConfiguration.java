package ru.peredera.mock.rest.error.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peredera.mock.rest.error.model.ErrorDto;

import java.util.HashMap;

/**
 * локальный справочник ошибок из errors.yaml
 */
@Data
@Component
@ConfigurationProperties(prefix = "error-service")
public class ErrorsConfiguration {

    ErrorDto defaultError;
    HashMap<String, ErrorDto> errorsMap;
}
