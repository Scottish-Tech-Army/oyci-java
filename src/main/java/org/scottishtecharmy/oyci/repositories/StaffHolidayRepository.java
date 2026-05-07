package org.scottishtecharmy.oyci.repositories;

import org.scottishtecharmy.oyci.entities.StaffHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffHolidayRepository extends JpaRepository<StaffHoliday, Long> {
    List<StaffHoliday> findByStaffId(Long staffId);
    List<StaffHoliday> findByStartDateBeforeAndEndDateAfter(LocalDate startDate, LocalDate endDate);
    void deleteByStaffId(Long staffId);

    @Query("SELECT DISTINCT sh.staff.id FROM StaffHoliday sh WHERE sh.startDate <= :date AND sh.endDate >= :date")
    List<Long> findStaffIdsOnHoliday(@Param("date") LocalDate date);
}
