package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    Qualification findByName(String name);
}

