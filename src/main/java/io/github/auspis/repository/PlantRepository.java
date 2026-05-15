package io.github.auspis.repository;

import java.util.List;

import io.github.auspis.domain.entity.Plant;
import io.github.auspis.fluentrepo4j.functional.FunctionalCrudRepository;
import io.github.auspis.fluentrepo4j.functional.FunctionalPagingAndSortingRepository;
import io.github.auspis.fluentrepo4j.functional.read.ReadResult;

public interface PlantRepository extends FunctionalCrudRepository<Plant, Long>,
        FunctionalPagingAndSortingRepository<Plant, Long> {

    ReadResult<Plant> findByNameIgnoreCase(String name);

    ReadResult<List<Plant>> findByWateringFrequencyDaysLessThan(Integer days);
}
