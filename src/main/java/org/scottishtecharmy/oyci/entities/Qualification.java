package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "qualification")
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // Constructors
    public Qualification() {
    }

    public Qualification(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Qualification{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

