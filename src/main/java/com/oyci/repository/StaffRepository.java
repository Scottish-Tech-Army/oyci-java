package com.oyci.repository;

import com.oyci.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByUsername(String username);

    @Query("SELECT DISTINCT s FROM Staff s JOIN s.skills sk WHERE sk IN :skills")
    List<Staff> findBySkillsIn(@Param("skills") List<String> skills);

    @Query("SELECT s FROM Staff s WHERE s.supportDifferentlyAbled = true")
    List<Staff> findSupportDifferentlyAbled();

    List<Staff> findAll();
}
