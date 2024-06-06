package org.example.model;

import org.springframework.data.repository.CrudRepository;

public interface MeasurementRepository extends CrudRepository<MeasurementEntity, String> {
}
