package org.scottishtecharmy.oyci.dto;

import java.util.List;

public class EventTypeResponse {

    private Long id;
    private String name;
    private String description;
    private Integer defDurMins;
    private Integer requiredExperienceMonths;
    private String requiredExperience;
    private List<EventTypeQualificationResponse> requiredQualifications;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDefDurMins() {
        return defDurMins;
    }

    public void setDefDurMins(Integer defDurMins) {
        this.defDurMins = defDurMins;
    }

    public Integer getRequiredExperienceMonths() {
        return requiredExperienceMonths;
    }

    public void setRequiredExperienceMonths(Integer requiredExperienceMonths) {
        this.requiredExperienceMonths = requiredExperienceMonths;
    }

    public String getRequiredExperience() {
        return requiredExperience;
    }

    public void setRequiredExperience(String requiredExperience) {
        this.requiredExperience = requiredExperience;
    }

    public List<EventTypeQualificationResponse> getRequiredQualifications() {
        return requiredQualifications;
    }

    public void setRequiredQualifications(List<EventTypeQualificationResponse> requiredQualifications) {
        this.requiredQualifications = requiredQualifications;
    }
}

