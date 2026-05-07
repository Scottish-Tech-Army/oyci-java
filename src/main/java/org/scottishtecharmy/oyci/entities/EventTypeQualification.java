package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "event_type_qualification")
public class EventTypeQualification {

    @EmbeddedId
    private EventTypeQualificationId id;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "event_type_id", insertable = false, updatable = false, referencedColumnName = "id")
    private EventType eventType;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "qualification_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Qualification qualification;

    // Constructors
    public EventTypeQualification() {
    }

    public EventTypeQualification(EventType eventType, Qualification qualification) {
        this.id = new EventTypeQualificationId(eventType.getId(), qualification.getId());
        this.eventType = eventType;
        this.qualification = qualification;
    }

    // Getters and Setters
    public EventTypeQualificationId getId() {
        return id;
    }

    public void setId(EventTypeQualificationId id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "EventTypeQualification{" +
                "id=" + id +
                ", eventType=" + eventType +
                ", qualification=" + qualification +
                '}';
    }
}
