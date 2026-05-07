package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    EventType findByName(String name);
}

