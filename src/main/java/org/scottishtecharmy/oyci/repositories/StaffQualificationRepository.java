package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.StaffQualification;
import org.scottishtecharmy.oyci.entities.StaffQualificationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffQualificationRepository extends JpaRepository<StaffQualification, StaffQualificationId> {
    List<StaffQualification> findByIdStaffId(Long staffId);
    List<StaffQualification> findByIdQualificationId(Long qualificationId);
    void deleteByIdStaffId(Long staffId);
}
