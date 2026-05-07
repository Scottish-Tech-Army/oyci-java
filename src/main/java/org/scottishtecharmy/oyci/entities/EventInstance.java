package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_instance")
public class EventInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "shift_start_time")
    private LocalDateTime shiftStartTime;

    @Column(name = "shift_end_time")
    private LocalDateTime shiftEndTime;

    // Constructors
    public EventInstance() {
    }

    public EventInstance(EventType eventType, Location location, LocalDateTime startTime, LocalDateTime endTime) {
        this.eventType = eventType;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EventInstance(EventType eventType, Location location, LocalDateTime startTime, LocalDateTime endTime,
                         LocalDateTime shiftStartTime, LocalDateTime shiftEndTime) {
        this.eventType = eventType;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(LocalDateTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public LocalDateTime getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(LocalDateTime shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    @Override
    public String toString() {
        return "EventInstance{" +
                "id=" + id +
                ", eventType=" + eventType +
                ", location=" + location +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", shiftStartTime=" + shiftStartTime +
                ", shiftEndTime=" + shiftEndTime +
                '}';
    }
}

