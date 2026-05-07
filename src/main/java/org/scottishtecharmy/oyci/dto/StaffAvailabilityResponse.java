package org.scottishtecharmy.oyci.dto;

import java.util.List;

public class StaffAvailabilityResponse {

    private Long id;
    private String name;
    private String email;
    private String designation;
    private Integer weeklyAvailHours;
    private Integer experienceMonths;
    private List<Long> qualificationIds;
    private List<StaffAvailabilityEventInstanceResponse> weeklyEventInstances;
    private Integer weeklyCommittedHours;
    private Integer remainingHours;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getWeeklyAvailHours() {
        return weeklyAvailHours;
    }

    public void setWeeklyAvailHours(Integer weeklyAvailHours) {
        this.weeklyAvailHours = weeklyAvailHours;
    }

    public Integer getExperienceMonths() {
        return experienceMonths;
    }

    public void setExperienceMonths(Integer experienceMonths) {
        this.experienceMonths = experienceMonths;
    }

    public List<Long> getQualificationIds() {
        return qualificationIds;
    }

    public void setQualificationIds(List<Long> qualificationIds) {
        this.qualificationIds = qualificationIds;
    }

    public List<StaffAvailabilityEventInstanceResponse> getWeeklyEventInstances() {
        return weeklyEventInstances;
    }

    public void setWeeklyEventInstances(List<StaffAvailabilityEventInstanceResponse> weeklyEventInstances) {
        this.weeklyEventInstances = weeklyEventInstances;
    }

    public Integer getWeeklyCommittedHours() {
        return weeklyCommittedHours;
    }

    public void setWeeklyCommittedHours(Integer weeklyCommittedHours) {
        this.weeklyCommittedHours = weeklyCommittedHours;
    }

    public Integer getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(Integer remainingHours) {
        this.remainingHours = remainingHours;
    }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

}
