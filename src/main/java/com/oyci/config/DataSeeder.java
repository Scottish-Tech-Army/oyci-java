package com.oyci.config;

import com.oyci.model.*;
import com.oyci.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("admin")) {
            log.info("Data already seeded, skipping.");
            return;
        }
        log.info("Seeding initial data...");

        // --- Admin ---
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // --- Staff ---
        Staff s1 = new Staff();
        s1.setUsername("sarah.m"); s1.setPassword(passwordEncoder.encode("staff123"));
        s1.setRole(Role.STAFF); s1.setName("Sarah Mitchell");
        s1.setEmail("sarah.m@oyci.org.uk"); s1.setTimeAvailable("MON-FRI 09:00-17:00");
        s1.setSkills(new ArrayList<>(List.of("Youth Work", "First Aid", "Sports Coaching")));
        s1.setSupportDifferentlyAbled(true);
        staffRepository.save(s1);

        Staff s2 = new Staff();
        s2.setUsername("jamie.t"); s2.setPassword(passwordEncoder.encode("staff123"));
        s2.setRole(Role.STAFF); s2.setName("Jamie Thomson");
        s2.setEmail("jamie.t@oyci.org.uk"); s2.setTimeAvailable("MON-WED 10:00-18:00");
        s2.setSkills(new ArrayList<>(List.of("Music", "Arts & Crafts", "Youth Work")));
        s2.setSupportDifferentlyAbled(false);
        staffRepository.save(s2);

        Staff s3 = new Staff();
        s3.setUsername("priya.k"); s3.setPassword(passwordEncoder.encode("staff123"));
        s3.setRole(Role.STAFF); s3.setName("Priya Kumar");
        s3.setEmail("priya.k@oyci.org.uk"); s3.setTimeAvailable("WED-FRI 09:00-17:00");
        s3.setSkills(new ArrayList<>(List.of("Mentoring", "Leadership", "STEM")));
        s3.setSupportDifferentlyAbled(true);
        staffRepository.save(s3);

        Staff s4 = new Staff();
        s4.setUsername("duncan.r"); s4.setPassword(passwordEncoder.encode("staff123"));
        s4.setRole(Role.STAFF); s4.setName("Duncan Reid");
        s4.setEmail("duncan.r@oyci.org.uk"); s4.setTimeAvailable("MON-FRI 08:00-16:00");
        s4.setSkills(new ArrayList<>(List.of("Sports Coaching", "First Aid", "Outdoor Education")));
        s4.setSupportDifferentlyAbled(false);
        staffRepository.save(s4);

        // --- Students ---
        Student st1 = new Student();
        st1.setUsername("alex.young"); st1.setPassword(passwordEncoder.encode("student123"));
        st1.setRole(Role.STUDENT); st1.setSkills(new ArrayList<>(List.of("Sports Coaching", "Leadership")));
        st1.setSpecialNeeds(false);
        studentRepository.save(st1);

        Student st2 = new Student();
        st2.setUsername("mia.jones"); st2.setPassword(passwordEncoder.encode("student123"));
        st2.setRole(Role.STUDENT); st2.setSkills(new ArrayList<>(List.of("Music", "Arts & Crafts")));
        st2.setSpecialNeeds(false);
        studentRepository.save(st2);

        Student st3 = new Student();
        st3.setUsername("leo.mcallister"); st3.setPassword(passwordEncoder.encode("student123"));
        st3.setRole(Role.STUDENT); st3.setSkills(new ArrayList<>(List.of("STEM", "Mentoring")));
        st3.setSpecialNeeds(true);
        studentRepository.save(st3);

        // --- Events ---
        Event e1 = Event.builder()
                .eventName("After-School Sports Drop-In")
                .venue("Community Sports Hall")
                .eventTimeStart(LocalDateTime.now().plusDays(1).withHour(15).withMinute(0))
                .eventTimeEnd(LocalDateTime.now().plusDays(1).withHour(17).withMinute(0))
                .skills(new ArrayList<>(List.of("Sports Coaching", "First Aid")))
                .openForDifferentAbled(false)
                .assignedStaff(new ArrayList<>(List.of(s1, s4)))
                .optionalStaff(new ArrayList<>())
                .registeredStudents(new ArrayList<>())
                .status(Event.EventStatus.UPCOMING)
                .build();
        eventRepository.save(e1);

        Event e2 = Event.builder()
                .eventName("Youth Music Workshop")
                .venue("Arts Centre Studio B")
                .eventTimeStart(LocalDateTime.now().plusDays(3).withHour(10).withMinute(0))
                .eventTimeEnd(LocalDateTime.now().plusDays(3).withHour(14).withMinute(0))
                .skills(new ArrayList<>(List.of("Music", "Arts & Crafts")))
                .openForDifferentAbled(false)
                .assignedStaff(new ArrayList<>(List.of(s2)))
                .optionalStaff(new ArrayList<>())
                .registeredStudents(new ArrayList<>())
                .status(Event.EventStatus.UPCOMING)
                .build();
        eventRepository.save(e2);

        Event e3 = Event.builder()
                .eventName("Holiday STEM Programme")
                .venue("Ochil Academy Lab")
                .eventTimeStart(LocalDateTime.now().plusDays(7).withHour(9).withMinute(0))
                .eventTimeEnd(LocalDateTime.now().plusDays(9).withHour(17).withMinute(0))
                .skills(new ArrayList<>(List.of("STEM", "Mentoring", "Leadership")))
                .openForDifferentAbled(true)
                .assignedStaff(new ArrayList<>(List.of(s3)))
                .optionalStaff(new ArrayList<>(List.of(s1)))
                .registeredStudents(new ArrayList<>())
                .status(Event.EventStatus.UPCOMING)
                .build();
        eventRepository.save(e3);

        Event e4 = Event.builder()
                .eventName("Leadership Bootcamp")
                .venue("Conference Room A")
                .eventTimeStart(LocalDateTime.now().plusDays(5).withHour(9).withMinute(0))
                .eventTimeEnd(LocalDateTime.now().plusDays(5).withHour(16).withMinute(0))
                .skills(new ArrayList<>(List.of("Leadership", "Mentoring")))
                .openForDifferentAbled(false)
                .assignedStaff(new ArrayList<>(List.of(s3)))
                .optionalStaff(new ArrayList<>())
                .registeredStudents(new ArrayList<>())
                .status(Event.EventStatus.UPCOMING)
                .build();
        eventRepository.save(e4);

        Event e9 = Event.builder()
                .eventName("After-School Sports Drop-In")
                .venue("Community Sports Hall")
                .eventTimeStart(LocalDateTime.now().minusDays(10).withHour(15).withMinute(0))
                .eventTimeEnd(LocalDateTime.now().minusDays(9).withHour(17).withMinute(0))
                .skills(new ArrayList<>(List.of("Sports Coaching", "First Aid")))
                .openForDifferentAbled(false)
                .assignedStaff(new ArrayList<>(List.of(s1, s4)))
                .optionalStaff(new ArrayList<>())
                .registeredStudents(new ArrayList<>())
                .status(Event.EventStatus.COMPLETED)
                .build();
        eventRepository.save(e9);
        
        log.info("Seeding complete. Admin: admin/admin123 | Staff: sarah.m/staff123 | Student: alex.young/student123");
    }
}
