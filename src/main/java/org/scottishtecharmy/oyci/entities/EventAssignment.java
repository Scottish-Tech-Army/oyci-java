package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "event_assignment")
public class EventAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_instance_id", nullable = false)
    private EventInstance eventInstance;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    // Constructors
    public EventAssignment() {
    }

    public EventAssignment(EventInstance eventInstance, Staff staff) {
        this.eventInstance = eventInstance;
        this.staff = staff;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventInstance getEventInstance() {
        return eventInstance;
    }

    public void setEventInstance(EventInstance eventInstance) {
        this.eventInstance = eventInstance;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
        return "EventAssignment{" +
                "id=" + id +
                ", eventInstance=" + eventInstance +
                ", staff=" + staff +
                '}';
    }
}

