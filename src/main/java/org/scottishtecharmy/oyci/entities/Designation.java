package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "designation",
        uniqueConstraints = @UniqueConstraint(name = "designation_name_uq", columnNames = "name"))
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Designation() {
    }

    public Designation(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Designation{id=" + id + ", name='" + name + "'}";
    }
}

