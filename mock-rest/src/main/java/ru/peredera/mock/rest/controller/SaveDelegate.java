package ru.peredera.mock.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import ru.peredera.mock.rest.model.MockRs;
import ru.peredera.mock.rest.model.RestMock;
import ru.peredera.mock.rest.service.RestService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SaveDelegate implements SaveApiDelegate, RestMocksApiDelegate{

    private final RestService stubService;

    @Autowired
    public SaveDelegate(RestService restService) {
        this.stubService = restService;
    }

    @Override
    public ResponseEntity<MockRs> mockRest(RestMock mockRq) {
        stubService.saveMock(mockRq);
        return ResponseEntity.ok(MockRs.builder().status("OK").build());
    }

    @Override
    public ResponseEntity<List<RestMock>> mockRestGet() {
        return ResponseEntity.ok(stubService.getMocks());
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return SaveApiDelegate.super.getRequest();
    }
}
