package ru.peredera.mock.rest.repository.model;

import org.springframework.boot.web.server.Cookie;
import ru.peredera.mock.rest.model.MockRq;

public class EntityConverter {
    public static MockEntity convert(MockRq rq) {
        var entity = new MockEntity();
        var response = new Response();

        response.setHttpStatusCode(rq.getHttpStatusCode());
        response.setParams(rq.getParams().stream().map(h -> new ru.peredera.mock.rest.repository.model.Parameter(h.getKey(), h.getValue())).toList());
        response.setHeaders(rq.getHeaders().stream().map(h -> new ru.peredera.mock.rest.repository.model.Header(h.getKey(), h.getValue())).toList());
        response.setCookies(rq.getCookies().stream().map(EntityConverter::mapCookie).toList());
        response.setBody(rq.getBody());

        entity.setUri(rq.getUri());
        entity.setResponse(JsonConverter.convertToString(response));

        return entity;
    }

    private static Cookie mapCookie(ru.peredera.mock.rest.model.Cookie cookieRq) {
        var cookie = new Cookie();
        cookie.setName(cookieRq.getName());
        cookie.setPath(cookieRq.getPath());
        return cookie;
    }
}
