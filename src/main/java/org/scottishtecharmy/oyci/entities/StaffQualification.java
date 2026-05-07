package org.scottishtecharmy.oyci.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "staff_qualification")
public class StaffQualification {

    @EmbeddedId
    private StaffQualificationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Qualification qualification;

    // Constructors
    public StaffQualification() {
    }

    public StaffQualification(Staff staff, Qualification qualification) {
        this.id = new StaffQualificationId(staff.getId(), qualification.getId());
        this.staff = staff;
        this.qualification = qualification;
    }

    // Getters and Setters
    public StaffQualificationId getId() {
        return id;
    }

    public void setId(StaffQualificationId id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "StaffQualification{" +
                "id=" + id +
                ", staff=" + staff +
                ", qualification=" + qualification +
                '}';
    }
}
