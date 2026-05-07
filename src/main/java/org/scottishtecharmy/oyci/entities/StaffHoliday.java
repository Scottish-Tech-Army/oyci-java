package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff_holiday")
public class StaffHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // Constructors
    public StaffHoliday() {
    }

    public StaffHoliday(Staff staff, LocalDate startDate, LocalDate endDate) {
        this.staff = staff;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "StaffHoliday{" +
                "id=" + id +
                ", staff=" + staff +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

