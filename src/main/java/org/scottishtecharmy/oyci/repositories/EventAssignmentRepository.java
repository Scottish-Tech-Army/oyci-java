package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.EventAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventAssignmentRepository extends JpaRepository<EventAssignment, Long> {
    List<EventAssignment> findByEventInstanceId(Long eventInstanceId);
    List<EventAssignment> findByStaffId(Long staffId);

    @Query("SELECT ea FROM EventAssignment ea JOIN FETCH ea.eventInstance ei JOIN FETCH ei.eventType JOIN FETCH ei.location WHERE ea.staff.id IN :staffIds AND ei.startTime >= :weekStart AND ei.startTime < :weekEnd ORDER BY ea.staff.id, ei.startTime")
    List<EventAssignment> findAssignmentsForStaffInWeek(
            @Param("staffIds") List<Long> staffIds,
            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd);
}
