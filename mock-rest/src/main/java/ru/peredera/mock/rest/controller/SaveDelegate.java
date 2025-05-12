package ru.peredera.mock.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.peredera.mock.rest.service.RestService;
import ru.peredera.mock.rest.model.MockRq;
import ru.peredera.mock.rest.model.MockRs;

@Slf4j
@Service
public class SaveDelegate implements SaveApiDelegate {

    private final RestService stubService;

    @Autowired
    public SaveDelegate(RestService restService) {
        this.stubService = restService;
    }

    @Override
    public ResponseEntity<MockRs> mockRest(MockRq mockRq) {
        stubService.saveMock(mockRq);
        return ResponseEntity.ok(MockRs.builder().status("OK").build());
    }
}
