package com.oyci.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
@DiscriminatorValue("STAFF")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Staff extends User {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "time_available")
    private String timeAvailable;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_skills", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    // Whether this staff supports differently-abled students
    @Column(nullable = false)
    private boolean supportDifferentlyAbled = false;

    // Holiday dates as ISO strings e.g. "2025-08-10"
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_holidays", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "holiday_date")
    private List<String> holidays = new ArrayList<>();

    // Withdrawal records per month: "YYYY-MM" -> count, stored as "YYYY-MM:count"
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_withdrawal_records", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "withdrawal_record")
    private List<String> withdrawalRecords = new ArrayList<>();

    @Transient
    private Double weeklyHours;
}
