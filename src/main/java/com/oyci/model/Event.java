package com.oyci.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventName;

    @Column
    private String venue;

    @Column(nullable = false)
    private LocalDateTime eventTimeStart;

    @Column(nullable = false)
    private LocalDateTime eventTimeEnd;

    @Column(nullable = false)
    @Builder.Default
    private boolean openForDifferentAbled = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_skills", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_staff",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "staff_id"))
    @Builder.Default
    private List<Staff> assignedStaff = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_optional_staff",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "staff_id"))
    @Builder.Default
    private List<Staff> optionalStaff = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_students",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id"))
    @Builder.Default
    private List<Student> registeredStudents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus status = EventStatus.UPCOMING;

    // feedback format: "studentId|staffRating|eventRating|staffComment|eventComment"
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_feedback", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "feedback_entry", length = 1000)
    @Builder.Default
    private List<String> feedbackEntries = new ArrayList<>();

    public enum EventStatus {
        UPCOMING, ONGOING, COMPLETED, CANCELLED
    }

    public double getDurationHours() {
        if (eventTimeStart == null || eventTimeEnd == null) return 0;
        return java.time.Duration.between(eventTimeStart, eventTimeEnd).toMinutes() / 60.0;
    }
}
