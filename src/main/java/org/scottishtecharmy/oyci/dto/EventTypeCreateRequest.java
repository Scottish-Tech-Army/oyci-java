package org.scottishtecharmy.oyci.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class EventTypeCreateRequest {

    @NotBlank
    private String name;

    private String description;

    @Min(1)
    private Integer defDurMins;

    @Min(0)
    private Integer requiredExperienceMonths;

    @Valid
    private List<EventTypeQualificationRequest> requiredQualifications = new ArrayList<>();

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

    public List<EventTypeQualificationRequest> getRequiredQualifications() {
        return requiredQualifications;
    }

    public void setRequiredQualifications(List<EventTypeQualificationRequest> requiredQualifications) {
        this.requiredQualifications = requiredQualifications == null ? new ArrayList<>() : requiredQualifications;
    }
}

