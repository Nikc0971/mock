package ru.peredera.mock.rest.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.web.server.Cookie;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    @JsonProperty("httpStatusCode")
    public String httpStatusCode = "";
    @JsonProperty("header")
    public List<Header> headers = new ArrayList<>(0);
    @JsonProperty("params")
    public List<Parameter> params = new ArrayList<>(0);
    @JsonProperty("cookie")
    public List<Cookie> cookies = new ArrayList<>(0);
    @JsonProperty("body")
    public String body = "";
}
