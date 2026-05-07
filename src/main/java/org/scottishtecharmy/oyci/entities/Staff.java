package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "weekly_avail_hours")
    private Integer weeklyAvailHours;

    @Column(name = "experience_months")
    private Integer experienceMonths;

    @Column(name = "designation")
    private String designation;

    // Constructors
    public Staff() {
    }

    public Staff(String name, String email, Integer weeklyAvailHours) {
        this.name = name;
        this.email = email;
        this.weeklyAvailHours = weeklyAvailHours;
    }

    public Staff(String name, String email, Integer weeklyAvailHours, Integer experienceMonths) {
        this.name = name;
        this.email = email;
        this.weeklyAvailHours = weeklyAvailHours;
        this.experienceMonths = experienceMonths;
    }

    // Getters and Setters
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", weeklyAvailHours=" + weeklyAvailHours +
                ", experienceMonths=" + experienceMonths +
                ", designation='" + designation + '\'' +
                '}';
    }
}

