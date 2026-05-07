package org.scottishtecharmy.oyci;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scottishtecharmy.oyci.controllers.StaffController;
import org.scottishtecharmy.oyci.dto.StaffCreateRequest;
import org.scottishtecharmy.oyci.dto.StaffHolidayRequest;
import org.scottishtecharmy.oyci.dto.StaffQualificationRequest;
import org.scottishtecharmy.oyci.dto.StaffResponse;
import org.scottishtecharmy.oyci.entities.Qualification;
import org.scottishtecharmy.oyci.repositories.EventTypeQualificationRepository;
import org.scottishtecharmy.oyci.repositories.QualificationRepository;
import org.scottishtecharmy.oyci.repositories.StaffHolidayRepository;
import org.scottishtecharmy.oyci.repositories.StaffQualificationRepository;
import org.scottishtecharmy.oyci.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.sql.init.mode=never")
@Import(TestMailConfig.class)
class StaffControllerIntegrationTest {

    @Autowired
    private StaffController staffController;

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffQualificationRepository staffQualificationRepository;

    @Autowired
    private StaffHolidayRepository staffHolidayRepository;

    @Autowired
    private EventTypeQualificationRepository eventTypeQualificationRepository;

    @BeforeEach
    void setUp() {
        staffQualificationRepository.deleteAll();
        staffHolidayRepository.deleteAll();
        staffRepository.deleteAll();
        eventTypeQualificationRepository.deleteAll();
        qualificationRepository.deleteAll();
    }

    @Test
    void createStaffSavesExperienceAtStaffLevelWithQualificationsAndHolidayRanges() {
        Qualification qualification = qualificationRepository.save(new Qualification("First Aid"));

        StaffCreateRequest request = new StaffCreateRequest();
        request.setName("Alex Morgan");
        request.setEmail("alex.morgan@example.com");
        request.setWeeklyAvailHours(24);
        request.setExperienceMonths(26);   // experience is staff-level, not per-qualification
        request.setDesignation("Youth Worker");

        StaffQualificationRequest qualificationRequest = new StaffQualificationRequest();
        qualificationRequest.setId(qualification.getId());
        // no experienceMonths here — qualification request only carries the id
        request.setQualifications(List.of(qualificationRequest));

        StaffHolidayRequest holidayRequest = new StaffHolidayRequest();
        holidayRequest.setStartDate(LocalDate.of(2026, 4, 10));
        holidayRequest.setEndDate(LocalDate.of(2026, 4, 15));
        request.setHolidays(List.of(holidayRequest));

        ResponseEntity<StaffResponse> response = staffController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        StaffResponse body = response.getBody();
        assertThat(body.getName()).isEqualTo("Alex Morgan");
        assertThat(body.getEmail()).isEqualTo("alex.morgan@example.com");
        assertThat(body.getWeeklyAvailHours()).isEqualTo(24);

        // experience lives at staff level
        assertThat(body.getExperienceMonths()).isEqualTo(26);
        assertThat(body.getExperience()).isEqualTo("2 yrs 2 months");
        assertThat(body.getDesignation()).isEqualTo("Youth Worker");

        // qualification has id and name only — no experience
        assertThat(body.getQualifications()).hasSize(1);
        assertThat(body.getQualifications().get(0).getId()).isEqualTo(qualification.getId());
        assertThat(body.getQualifications().get(0).getName()).isEqualTo("First Aid");

        assertThat(body.getHolidays()).hasSize(1);
        assertThat(body.getHolidays().get(0).getStartDate()).isEqualTo(LocalDate.of(2026, 4, 10));
        assertThat(body.getHolidays().get(0).getEndDate()).isEqualTo(LocalDate.of(2026, 4, 15));

        // verify persistence
        assertThat(staffRepository.findAll()).hasSize(1);
        assertThat(staffRepository.findAll().get(0).getExperienceMonths()).isEqualTo(26);
        assertThat(staffQualificationRepository.findAll()).hasSize(1);
        assertThat(staffHolidayRepository.findAll()).hasSize(1);
    }

    @Test
    void createStaffWithNoExperienceDefaultsToNull() {
        StaffCreateRequest request = new StaffCreateRequest();
        request.setName("Sam Lee");
        request.setEmail("sam.lee@example.com");
        request.setWeeklyAvailHours(16);
        // experienceMonths intentionally omitted

        ResponseEntity<StaffResponse> response = staffController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getExperienceMonths()).isNull();
        assertThat(response.getBody().getExperience()).isEqualTo("0 yrs 0 months");
    }
}
