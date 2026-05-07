package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StaffQualificationId implements Serializable {

    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "qualification_id")
    private Long qualificationId;

    // Constructors
    public StaffQualificationId() {
    }

    public StaffQualificationId(Long staffId, Long qualificationId) {
        this.staffId = staffId;
        this.qualificationId = qualificationId;
    }

    // Getters and Setters
    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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
        StaffQualificationId that = (StaffQualificationId) o;
        return Objects.equals(staffId, that.staffId) &&
                Objects.equals(qualificationId, that.qualificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId, qualificationId);
    }
}

