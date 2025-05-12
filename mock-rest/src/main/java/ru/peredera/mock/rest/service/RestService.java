package ru.peredera.mock.rest.service;


import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.peredera.mock.rest.error.exceptions.MockRestException;
import ru.peredera.mock.rest.model.MockRq;
import ru.peredera.mock.rest.repository.MockRepository;
import ru.peredera.mock.rest.repository.model.EntityConverter;
import ru.peredera.mock.rest.repository.model.MockEntity;
import ru.peredera.mock.rest.repository.model.Response;

import java.net.URI;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static ru.peredera.mock.rest.repository.model.JsonConverter.convertToEntity;

@Service
public class RestService {

    private final MockRepository mocksRepository;
    private final FreemarkerService freemarkerService;

    @Autowired
    public RestService(MockRepository mocksRepository, FreemarkerService freemarkerService) {
        this.mocksRepository = mocksRepository;
        this.freemarkerService = freemarkerService;
    }

    /**
     * Сохранение описание мок ответа интеграции
     *
     * @param rq описание сохраняемой заглушки
     */
    public void saveMock(MockRq rq) {
        var entity = EntityConverter.convert(rq);
        enrichIsNeededRender(entity, rq);
        mocksRepository.save(entity);
    }

    /**
     * Возвращает имитацию ответа заглушки по сохраненному макету
     *
     * @param serverRequest запрос в интеграцию
     * @return объект ответа сервера с ответом
     */
    public ServerResponse buildMockResponse(ServerRequest serverRequest) {
        return mocksRepository
                .findById(matchPathFromUri(serverRequest.uri().toString().trim()))
                .map(entity -> renderResponse(serverRequest, entity))
                .map(this::buildServerResponse)
                .orElseThrow(() -> MockRestException.builder().errorCode("mock_not_found_error").message("Запрашиваемая заглушка отсутствует").build());
    }

    private ServerResponse buildServerResponse(Response response) {
        var serverResponse = ServerResponse
                .status(requireNonNull(HttpStatus.resolve(Integer.parseInt(response.getHttpStatusCode()))))
                .headers(httpHeaders -> httpHeaders.addAll(convertHeaders(response)))
                .cookies(cookies -> cookies.addAll(convertCookies(response)));

        if (isNotEmpty(response.getBody())) {
            return serverResponse.body(response.getBody());
        }

        return serverResponse.build();
    }

    private MultiValueMap<String, String> convertHeaders(Response response) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        response.getHeaders().forEach(h -> multiValueMap.addIfAbsent(h.getKey(), h.getValue()));
        return multiValueMap;
    }

    private MultiValueMap<String, Cookie> convertCookies(Response response) {
        MultiValueMap<String, Cookie> multiValueMap = new LinkedMultiValueMap<>();
        response.getCookies().forEach(c -> {
            var cookie = new Cookie(c.getName(), c.getPath());
            multiValueMap.addIfAbsent(c.getName(), cookie);
        });
        return multiValueMap;
    }

    private String matchPathFromUri(String stringURL) {
        try {
            var url = new URI(stringURL).toURL();
            return requireNonNull(url.getPath());
        } catch (Exception e) {
            throw MockRestException.builder().errorCode("default_error").message("Ошибка парсинга path").build();
        }
    }

    private Response renderResponse(ServerRequest serverRequest, MockEntity entity) {
        try {
            var responseString = entity.getResponse();

            if (entity.getIsNeededRender()) {
                var params = convertToEntity(serverRequest.body(String.class), Map.class);
                responseString = freemarkerService.render(entity.getResponse(), params);
            }

            return convertToEntity(responseString, Response.class);
        } catch (Exception e) {
            throw MockRestException.builder().errorCode("default_error").message("Ошибка рендера ответа").build();
        }
    }

    private void enrichIsNeededRender(MockEntity entity, MockRq rq) {
        entity.setIsNeededRender(rq.getBody() != null && rq.getBody().contains("${"));
    }
}
