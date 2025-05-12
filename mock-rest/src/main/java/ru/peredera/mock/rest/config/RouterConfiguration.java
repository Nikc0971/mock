package ru.peredera.mock.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import ru.peredera.mock.rest.controller.RouterHandler;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

    @Bean
    RouterFunction<ServerResponse> getRoute(RouterHandler routerHandler) {
        return route(path("mock/rest/*").negate(), routerHandler::handle);

    }
}
