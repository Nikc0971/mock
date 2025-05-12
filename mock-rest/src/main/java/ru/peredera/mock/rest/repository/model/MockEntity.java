package ru.peredera.mock.rest.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "mocks")
public class MockEntity {

    @Id
    private String uri;

    private String response;

    private Boolean isNeededRender;
}
