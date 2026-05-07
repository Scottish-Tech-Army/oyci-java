package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByEmail(String email);
}

