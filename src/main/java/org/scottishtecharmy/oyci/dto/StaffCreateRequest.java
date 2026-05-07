package org.scottishtecharmy.oyci.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StaffCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Min(0)
    private Integer weeklyAvailHours;

    @Min(0)
    private Integer experienceMonths;

    private String designation;

    @Valid
    private List<StaffQualificationRequest> qualifications = new ArrayList<>();

    @Valid
    private List<StaffHolidayRequest> holidays = new ArrayList<>();

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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<StaffQualificationRequest> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<StaffQualificationRequest> qualifications) {
        this.qualifications = qualifications == null ? new ArrayList<>() : qualifications;
    }

    public List<StaffHolidayRequest> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<StaffHolidayRequest> holidays) {
        this.holidays = holidays == null ? new ArrayList<>() : holidays;
    }
}
