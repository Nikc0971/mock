package ru.peredera.mock.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.peredera.mock.rest.repository.model.MockEntity;

@Repository
public interface MockRepository extends JpaRepository<MockEntity, String> {
}
