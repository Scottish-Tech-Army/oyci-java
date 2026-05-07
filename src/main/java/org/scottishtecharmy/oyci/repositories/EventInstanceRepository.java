package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.EventInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventInstanceRepository extends JpaRepository<EventInstance, Long> {
    List<EventInstance> findByEventTypeId(Long eventTypeId);
    List<EventInstance> findByLocationId(Long locationId);
    List<EventInstance> findByStartTimeAfterAndEndTimeBeforeOrderByStartTime(LocalDateTime startTime, LocalDateTime endTime);
    List<EventInstance> findByStartTimeBetweenOrderByStartTime(LocalDateTime from, LocalDateTime to);
    List<EventInstance> findAllByOrderByStartTime();
}

