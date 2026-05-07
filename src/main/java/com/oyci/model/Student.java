package com.oyci.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@DiscriminatorValue("STUDENT")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {

    @Column
    private String name;

    @Column
    private String email;

    @Column(nullable = false)
    private boolean specialNeeds = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    // IDs of completed events for certificates
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_completed_events", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "event_id")
    private List<Long> completedEventIds = new ArrayList<>();

    // Flag to show certificate banner on next login
    @Column(nullable = false)
    private boolean showCertificateBanner = false;
}
