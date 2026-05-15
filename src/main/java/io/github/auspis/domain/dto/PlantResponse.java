package io.github.auspis.domain.dto;

public class PlantResponse {

    private Long id;
    private String name;
    private String scientificName;
    private int wateringFrequencyDays;

    public PlantResponse() {
    }

    public PlantResponse(Long id, String name, String scientificName, int wateringFrequencyDays) {
        this.id = id;
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
