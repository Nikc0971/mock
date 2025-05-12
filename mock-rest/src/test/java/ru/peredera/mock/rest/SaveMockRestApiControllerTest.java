package ru.peredera.mock.rest;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;
import ru.peredera.mock.rest.model.MockRs;
import ru.peredera.mock.rest.repository.MockRepository;

import java.io.IOException;
import java.util.Objects;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class SaveMockRestApiControllerTest {

    @Autowired
    MockRepository mocksRepository;

    @LocalServerPort
    int randomServerPort;

    private final RestClient restClient = RestClient.create();
    public static MockWebServer server;

    final static Dispatcher dispatcher = new Dispatcher() {
        //для примера как можно реализовать е2е тест и заглушить рест интеграции
        @NotNull
        @Override
        public MockResponse dispatch(RecordedRequest request) {

            return switch (Objects.requireNonNull(request.getPath())) {
                case "/api-url-one" -> new MockResponse()
                        .setResponseCode(201);
                case "/api-url-two" -> new MockResponse()
                        .setHeader("x-header-name", "header-value")
                        .setResponseCode(200)
                        .setBody("<response />");
                case "/api-url-three" -> new MockResponse()
                        .setResponseCode(500)
                        .setBodyDelay(5000, SECONDS)
                        .setChunkedBody("<error-response />", 5);
                case "/api-url-four" -> new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"data\":\"\"}")
                        .throttleBody(1024, 5, SECONDS);
                default -> new MockResponse().setResponseCode(404);
            };
        }
    };

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.setDispatcher(dispatcher);
        server.start(8080);
    }

    @Test
    public void testSaveMockEndpoint() {
        var hostPort = "http://localhost:" + randomServerPort;

        var actual = restClient.post().uri(hostPort + "/mock/rest/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                                {
                                    "uri" : "/test",
                                    "httpStatusCode": "${res.success?has_content?then('200', '300')}",
                                    "body" : "{\\"code\\":\\"${code?has_content?then('${code}', 'test_fail_123')}\\"}"
                                }
                                """
                )
                .retrieve()
                .body(MockRs.class);

        assertEquals(MockRs.builder().status("OK").build(), actual);

        var actualResponse = mocksRepository
                .findById("/test")
                .orElseThrow(() -> AssertionFailureBuilder.assertionFailure().message("Сохраненный мок в БД не найден").build())
                .getResponse();

        assertEquals(
                "{\"httpStatusCode\":\"${res.success?has_content?then('200', '300')}\",\"header\":[],\"params\":[],\"cookie\":[],\"body\":\"{\\\"code\\\":\\\"${code?has_content?then('${code}', 'test_fail_123')}\\\"}\"}",
                actualResponse
        );

        var actualMock = restClient.post().uri(hostPort + "/test")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        """
                                {
                                    "res": {
                                        "success": "OK"
                                    },
                                    "code": "test_ok"
                                }
                                                                """
                )
                .retrieve()
                .body(String.class);

        assertEquals(
                "{\"code\":\"test_ok\"}",
                actualMock
        );
    }
}