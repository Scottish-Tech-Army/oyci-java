package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventTypeQualificationId implements Serializable {

    @Column(name = "event_type_id")
    private Long eventTypeId;

    @Column(name = "qualification_id")
    private Long qualificationId;

    // Constructors
    public EventTypeQualificationId() {
    }

    public EventTypeQualificationId(Long eventTypeId, Long qualificationId) {
        this.eventTypeId = eventTypeId;
        this.qualificationId = qualificationId;
    }

    // Getters and Setters
    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Long getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Long qualificationId) {
        this.qualificationId = qualificationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTypeQualificationId that = (EventTypeQualificationId) o;
        return Objects.equals(eventTypeId, that.eventTypeId) &&
                Objects.equals(qualificationId, that.qualificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTypeId, qualificationId);
    }
}

