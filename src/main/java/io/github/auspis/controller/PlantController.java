package io.github.auspis.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.auspis.domain.dto.PlantRequest;
import io.github.auspis.domain.dto.PlantResponse;
import io.github.auspis.service.PlantService;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    public List<PlantResponse> findAll() {
        return plantService.findAll();
    }

    @GetMapping("/{id}")
    public PlantResponse findById(@PathVariable Long id) {
        return plantService.findById(id);
    }

    @GetMapping("/watering-less-than")
    public List<PlantResponse> findByWateringLessThan(@RequestParam Integer days) {
        return plantService.findByWateringLessThan(days);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlantResponse create(@RequestBody PlantRequest request) {
        return plantService.create(request);
    }

    @PutMapping("/{id}")
    public PlantResponse update(@PathVariable Long id, @RequestBody PlantRequest request) {
        return plantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        plantService.delete(id);
    }
}
