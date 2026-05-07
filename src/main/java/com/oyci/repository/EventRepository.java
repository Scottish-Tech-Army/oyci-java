package com.oyci.repository;

import com.oyci.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT DISTINCT e FROM Event e JOIN e.skills sk WHERE sk IN :skills AND e.eventTimeEnd > :now AND e.status != 'CANCELLED'")
    List<Event> findEventsMatchingStudentSkills(@Param("skills") List<String> skills, @Param("now") LocalDateTime now);

    @Query("SELECT DISTINCT e FROM Event e WHERE e.eventTimeEnd > :now AND e.status != 'CANCELLED'")
    List<Event> findAllUpcomingEvents(@Param("now") LocalDateTime now);

    @Query("SELECT e FROM Event e JOIN e.assignedStaff s WHERE s.id = :staffId AND e.eventTimeStart >= :from AND e.eventTimeStart <= :to")
    List<Event> findEventsByStaffAndDateRange(@Param("staffId") Long staffId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT e FROM Event e JOIN e.assignedStaff s WHERE s.id = :staffId")
    List<Event> findAllByStaffId(@Param("staffId") Long staffId);

    List<Event> findByEventTimeEndAfterAndStatusNot(LocalDateTime now, Event.EventStatus status);

    @Query("SELECT e FROM Event e JOIN e.registeredStudents s WHERE s.id = :studentId")
    List<Event> findByRegisteredStudentId(@Param("studentId") Long studentId);

    List<Event> findByStatus(Event.EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.status = 'UPCOMING' AND e.eventTimeEnd <= :now")
    List<Event> findExpiredUpcomingEvents(@Param("now") LocalDateTime now);
}
