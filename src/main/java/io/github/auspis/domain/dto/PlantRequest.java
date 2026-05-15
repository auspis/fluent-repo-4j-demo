package io.github.auspis.domain.dto;

public class PlantRequest {

    private String name;
    private String scientificName;
    private int wateringFrequencyDays;

    public PlantRequest() {
    }

    public PlantRequest(String name, String scientificName, int wateringFrequencyDays) {
        this.name = name;
        this.scientificName = scientificName;
        this.wateringFrequencyDays = wateringFrequencyDays;
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
