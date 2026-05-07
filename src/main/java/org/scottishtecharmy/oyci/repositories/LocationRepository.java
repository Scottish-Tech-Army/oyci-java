package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByName(String name);
}

