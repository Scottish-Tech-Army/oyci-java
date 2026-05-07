package org.scottishtecharmy.oyci.dto;

import java.util.List;

public class StaffResponse {

    private Long id;
    private String name;
    private String email;
    private Integer weeklyAvailHours;
    private Integer experienceMonths;
    private String experience;
    private String designation;
    private List<StaffQualificationResponse> qualifications;
    private List<StaffHolidayResponse> holidays;

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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<StaffQualificationResponse> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<StaffQualificationResponse> qualifications) {
        this.qualifications = qualifications;
    }

    public List<StaffHolidayResponse> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<StaffHolidayResponse> holidays) {
        this.holidays = holidays;
    }
}
