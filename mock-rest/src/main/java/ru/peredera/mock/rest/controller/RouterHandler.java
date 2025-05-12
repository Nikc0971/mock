package ru.peredera.mock.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.peredera.mock.rest.error.ErrorHandler;
import ru.peredera.mock.rest.service.RestService;

@Service
public class RouterHandler {

    private final ErrorHandler errorHandler;
    private final RestService restService;

    @Autowired
    public RouterHandler(ErrorHandler errorHandler, RestService restService) {
        this.errorHandler = errorHandler;
        this.restService = restService;
    }

    public ServerResponse handle(ServerRequest serverRequest) {
        try {
            return restService.buildMockResponse(serverRequest);
        } catch (Exception e) {
            return errorHandler.buildServerResponse(e);
        }
    }
}
