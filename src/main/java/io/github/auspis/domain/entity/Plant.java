package io.github.auspis.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "plants")
public class Plant {
    @Id
    private Long id;

    private String name;

    @Column(name = "scientific_name")
    private String scientificName;

    @Column(name = "watering_frequency_days")
    private int wateringFrequencyDays;

    public Plant() {
    }

    public Plant(String name, String scientificName, int wateringFrequencyDays) {
        this.name = name;
        this.scientificName = scientificName;
        this.wateringFrequencyDays = wateringFrequencyDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public int getWateringFrequencyDays() {
        return wateringFrequencyDays;
    }

    public void setWateringFrequencyDays(int wateringFrequencyDays) {
        this.wateringFrequencyDays = wateringFrequencyDays;
    }
}
