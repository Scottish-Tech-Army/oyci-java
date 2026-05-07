package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "event_type")
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "def_dur_mins")
    private Integer defDurMins;

    @Column(name = "required_experience_months")
    private Integer requiredExperienceMonths;

    // Constructors
    public EventType() {
    }

    public EventType(String name, String description, Integer defDurMins) {
        this.name = name;
        this.description = description;
        this.defDurMins = defDurMins;
    }

    public EventType(String name, String description, Integer defDurMins, Integer requiredExperienceMonths) {
        this.name = name;
        this.description = description;
        this.defDurMins = defDurMins;
        this.requiredExperienceMonths = requiredExperienceMonths;
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

    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", defDurMins=" + defDurMins +
                ", requiredExperienceMonths=" + requiredExperienceMonths +
                '}';
    }
}

