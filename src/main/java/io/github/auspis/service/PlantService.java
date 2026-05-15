package io.github.auspis.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.auspis.domain.dto.PlantRequest;
import io.github.auspis.domain.dto.PlantResponse;
import io.github.auspis.domain.entity.Plant;
import io.github.auspis.fluentrepo4j.functional.read.ReadResult;
import io.github.auspis.fluentrepo4j.functional.write.WriteResult;
import io.github.auspis.repository.PlantRepository;

@Service
public class PlantService {

    private static final String PLANT_NOT_FOUND = "Plant not found: ";
    
    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public List<PlantResponse> findAll() {
        return readListOrEmpty(plantRepository.findAll())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlantResponse findById(Long id) {
        return toResponse(readOneOrNotFound(plantRepository.findById(id), PLANT_NOT_FOUND + id));
    }

    public List<PlantResponse> findByWateringLessThan(Integer days) {
        return readListOrEmpty(plantRepository.findByWateringFrequencyDaysLessThan(days))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlantResponse create(PlantRequest request) {
        Plant plant = new Plant(request.getName(), request.getScientificName(), request.getWateringFrequencyDays());
        plant.setId(ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE));
        return toResponse(writeOrServerError(plantRepository.save(plant), "Unable to create plant"));
    }

    public PlantResponse update(Long id, PlantRequest request) {
        Plant plant = readOneOrNotFound(plantRepository.findById(id), PLANT_NOT_FOUND + id);
        plant.setName(request.getName());
        plant.setScientificName(request.getScientificName());
        plant.setWateringFrequencyDays(request.getWateringFrequencyDays());
        return toResponse(writeOrServerError(plantRepository.save(plant), "Unable to update plant: " + id));
    }

    public void delete(Long id) {
        readOneOrNotFound(plantRepository.findById(id), PLANT_NOT_FOUND + id);
        writeOrServerError(plantRepository.deleteById(id), "Unable to delete plant: " + id);
    }

    private Plant readOneOrNotFound(ReadResult<Plant> result, String notFoundMessage) {
        switch (result) {
            case ReadResult.Found<Plant>(Plant value):
                return value;
            case ReadResult.NotFound<Plant> notFound:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMessage);
            case ReadResult.Error<Plant> error:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.message());
            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected read state");
        }
    }

    
    private List<Plant> readListOrEmpty(ReadResult<List<Plant>> result) {
        switch (result) {
            case ReadResult.Found<List<Plant>>(List<Plant> value):
                return value;
            case ReadResult.NotFound<List<Plant>> notFound:
                return List.of();
            case ReadResult.Error<List<Plant>> error:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.message());
            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected read state");
        }
    }

    private <T> T writeOrServerError(WriteResult<T> result, String fallbackMessage) {
        switch (result) {
            case WriteResult.Success<T>(T value):
                return value;
            case WriteResult.Error<T> error:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        error.message() == null ? fallbackMessage : error.message());
            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, fallbackMessage);
        }
    }

    private PlantResponse toResponse(Plant plant) {
        return new PlantResponse(
                plant.getId(),
                plant.getName(),
                plant.getScientificName(),
                plant.getWateringFrequencyDays());
    }
}
