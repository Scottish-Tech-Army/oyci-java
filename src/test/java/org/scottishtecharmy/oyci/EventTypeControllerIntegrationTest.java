package org.scottishtecharmy.oyci;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scottishtecharmy.oyci.controllers.EventTypeController;
import org.scottishtecharmy.oyci.dto.EventTypeCreateRequest;
import org.scottishtecharmy.oyci.dto.EventTypeQualificationRequest;
import org.scottishtecharmy.oyci.dto.EventTypeResponse;
import org.scottishtecharmy.oyci.entities.Qualification;
import org.scottishtecharmy.oyci.repositories.EventTypeQualificationRepository;
import org.scottishtecharmy.oyci.repositories.EventTypeRepository;
import org.scottishtecharmy.oyci.repositories.QualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.sql.init.mode=never")
@Import(TestMailConfig.class)
class EventTypeControllerIntegrationTest {

    @Autowired
    private EventTypeController eventTypeController;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private EventTypeQualificationRepository eventTypeQualificationRepository;

    @BeforeEach
    void setUp() {
        eventTypeQualificationRepository.deleteAll();
        eventTypeRepository.deleteAll();
        qualificationRepository.deleteAll();
    }

    @Test
    void createEventTypeWithRequiredQualificationsAndExperience() {
        Qualification firstAid = qualificationRepository.save(new Qualification("First Aid"));
        Qualification safeguarding = qualificationRepository.save(new Qualification("Safeguarding"));

        EventTypeCreateRequest request = new EventTypeCreateRequest();
        request.setName("Youth Workshop");
        request.setDescription("A workshop for young people in the community");
        request.setDefDurMins(90);
        request.setRequiredExperienceMonths(12);

        EventTypeQualificationRequest qr1 = new EventTypeQualificationRequest();
        qr1.setId(firstAid.getId());

        EventTypeQualificationRequest qr2 = new EventTypeQualificationRequest();
        qr2.setId(safeguarding.getId());

        request.setRequiredQualifications(List.of(qr1, qr2));

        ResponseEntity<EventTypeResponse> response = eventTypeController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        EventTypeResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo("Youth Workshop");
        assertThat(body.getDescription()).isEqualTo("A workshop for young people in the community");
        assertThat(body.getDefDurMins()).isEqualTo(90);
        assertThat(body.getRequiredExperienceMonths()).isEqualTo(12);
        assertThat(body.getRequiredExperience()).isEqualTo("1 yrs 0 months");
        assertThat(body.getRequiredQualifications()).hasSize(2);

        assertThat(body.getRequiredQualifications())
                .anyMatch(q -> q.getName().equals("First Aid"));
        assertThat(body.getRequiredQualifications())
                .anyMatch(q -> q.getName().equals("Safeguarding"));
    }

    @Test
    void getEventTypeByIdReturnsExperienceAndQualifications() {
        Qualification firstAid = qualificationRepository.save(new Qualification("First Aid"));

        EventTypeCreateRequest request = new EventTypeCreateRequest();
        request.setName("First Aid Course");
        request.setDescription("Teaching first aid basics");
        request.setDefDurMins(120);
        request.setRequiredExperienceMonths(36);

        EventTypeQualificationRequest qr = new EventTypeQualificationRequest();
        qr.setId(firstAid.getId());
        request.setRequiredQualifications(List.of(qr));

        ResponseEntity<EventTypeResponse> createResponse = eventTypeController.create(request);
        Long eventTypeId = createResponse.getBody().getId();

        ResponseEntity<EventTypeResponse> getResponse = eventTypeController.getById(eventTypeId);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventTypeResponse body = getResponse.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getRequiredExperienceMonths()).isEqualTo(36);
        assertThat(body.getRequiredExperience()).isEqualTo("3 yrs 0 months");
        assertThat(body.getRequiredQualifications()).hasSize(1);
        assertThat(body.getRequiredQualifications().get(0).getName()).isEqualTo("First Aid");
        assertThat(body.getRequiredQualifications().get(0).getId()).isEqualTo(firstAid.getId());
    }

    @Test
    void updateEventTypeReplacesExperienceAndQualifications() {
        Qualification firstAid = qualificationRepository.save(new Qualification("First Aid"));
        Qualification safeguarding = qualificationRepository.save(new Qualification("Safeguarding"));

        EventTypeCreateRequest createRequest = new EventTypeCreateRequest();
        createRequest.setName("Community Event");
        createRequest.setDescription("Original description");
        createRequest.setDefDurMins(60);
        createRequest.setRequiredExperienceMonths(6);

        EventTypeQualificationRequest qr1 = new EventTypeQualificationRequest();
        qr1.setId(firstAid.getId());
        createRequest.setRequiredQualifications(List.of(qr1));

        ResponseEntity<EventTypeResponse> createResponse = eventTypeController.create(createRequest);
        Long eventTypeId = createResponse.getBody().getId();

        EventTypeCreateRequest updateRequest = new EventTypeCreateRequest();
        updateRequest.setName("Community Event Updated");
        updateRequest.setDescription("Updated description");
        updateRequest.setDefDurMins(120);
        updateRequest.setRequiredExperienceMonths(18);

        EventTypeQualificationRequest qr2 = new EventTypeQualificationRequest();
        qr2.setId(safeguarding.getId());
        updateRequest.setRequiredQualifications(List.of(qr2));

        ResponseEntity<EventTypeResponse> updateResponse = eventTypeController.update(eventTypeId, updateRequest);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventTypeResponse body = updateResponse.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo("Community Event Updated");
        assertThat(body.getDescription()).isEqualTo("Updated description");
        assertThat(body.getDefDurMins()).isEqualTo(120);
        assertThat(body.getRequiredExperienceMonths()).isEqualTo(18);
        assertThat(body.getRequiredExperience()).isEqualTo("1 yrs 6 months");
        assertThat(body.getRequiredQualifications()).hasSize(1);
        assertThat(body.getRequiredQualifications().get(0).getName()).isEqualTo("Safeguarding");
    }

    @Test
    void createEventTypeWithNoQualificationsAndNoExperience() {
        EventTypeCreateRequest request = new EventTypeCreateRequest();
        request.setName("Open Day");
        request.setDescription("No qualifications or experience needed");
        request.setDefDurMins(180);

        ResponseEntity<EventTypeResponse> response = eventTypeController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        EventTypeResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo("Open Day");
        assertThat(body.getDescription()).isEqualTo("No qualifications or experience needed");
        assertThat(body.getDefDurMins()).isEqualTo(180);
        assertThat(body.getRequiredExperienceMonths()).isNull();
        assertThat(body.getRequiredExperience()).isEqualTo("0 yrs 0 months");
        assertThat(body.getRequiredQualifications()).isEmpty();
    }
}
