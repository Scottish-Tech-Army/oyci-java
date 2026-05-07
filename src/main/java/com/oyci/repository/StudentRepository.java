package com.oyci.repository;

import com.oyci.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);

    @Query("SELECT DISTINCT s FROM Student s JOIN s.skills sk WHERE sk IN :skills")
    List<Student> findBySkillsIn(@Param("skills") List<String> skills);
}
