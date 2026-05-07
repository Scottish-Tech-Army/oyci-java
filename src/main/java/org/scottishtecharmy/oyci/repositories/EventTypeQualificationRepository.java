package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.EventTypeQualification;
import org.scottishtecharmy.oyci.entities.EventTypeQualificationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTypeQualificationRepository extends JpaRepository<EventTypeQualification, EventTypeQualificationId> {
    List<EventTypeQualification> findByIdEventTypeId(Long eventTypeId);
    List<EventTypeQualification> findByIdQualificationId(Long qualificationId);
    void deleteByIdEventTypeId(Long eventTypeId);
}

