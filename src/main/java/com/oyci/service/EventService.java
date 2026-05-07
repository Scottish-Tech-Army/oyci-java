package com.oyci.service;

import com.oyci.dto.EventDtos;
import com.oyci.dto.StaffDtos;
import com.oyci.model.*;
import com.oyci.repository.EventRepository;
import com.oyci.repository.StaffRepository;
import com.oyci.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final StaffService staffService;

    // ── Auto-complete scheduler (every minute) ────────────────────
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void autoCompleteExpiredEvents() {
        List<Event> expired = eventRepository.findExpiredUpcomingEvents(LocalDateTime.now());
        for (Event ev : expired) {
            ev.setStatus(Event.EventStatus.COMPLETED);
            // Award certificates + skills to students
            for (Student student : ev.getRegisteredStudents()) {
                if (!student.getCompletedEventIds().contains(ev.getId())) {
                    student.getCompletedEventIds().add(ev.getId());
                    student.setShowCertificateBanner(true);
                    // Auto-add event skills to student if "other event" (no skill overlap)
                    boolean hasOverlap = student.getSkills().stream().anyMatch(ev.getSkills()::contains);
                    if (!hasOverlap) {
                        for (String skill : ev.getSkills()) {
                            if (!student.getSkills().contains(skill)) {
                                student.getSkills().add(skill);
                            }
                        }
                    }
                    studentRepository.save(student);
                }
            }
            eventRepository.save(ev);
        }
    }

    // ── Create Event ──────────────────────────────────────────────
    @Transactional
    public EventDtos.EventResponse createEvent(EventDtos.CreateEventRequest request) {
        if (request.getEventTimeStart().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event start time must be in the future");
        }
        if (request.getEventTimeEnd().isBefore(request.getEventTimeStart())) {
            throw new RuntimeException("End time must be after start time");
        }

        List<Staff> assignedStaff = new ArrayList<>();
        if (request.getAssignedStaffIds() != null && !request.getAssignedStaffIds().isEmpty()) {
            assignedStaff = new ArrayList<>(staffRepository.findAllById(request.getAssignedStaffIds()));
        }
        List<Staff> optionalStaff = new ArrayList<>();
        if (request.getOptionalStaffIds() != null && !request.getOptionalStaffIds().isEmpty()) {
            optionalStaff = new ArrayList<>(staffRepository.findAllById(request.getOptionalStaffIds()));
        }

        Event event = Event.builder()
                .eventName(request.getEventName())
                .venue(request.getVenue())
                .eventTimeStart(request.getEventTimeStart())
                .eventTimeEnd(request.getEventTimeEnd())
                .skills(request.getSkills() == null ? new ArrayList<>() : new ArrayList<>(request.getSkills()))
                .openForDifferentAbled(request.isOpenForDifferentAbled())
                .assignedStaff(assignedStaff)
                .optionalStaff(optionalStaff)
                .registeredStudents(new ArrayList<>())
                .status(Event.EventStatus.UPCOMING)
                .build();

        return toResponse(eventRepository.save(event), null);
    }

    // ── Duplicate a completed event ───────────────────────────────
    @Transactional
    public EventDtos.EventResponse duplicateEvent(Long sourceId, EventDtos.DuplicateEventRequest request) {
        Event source = eventRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + sourceId));
        if (source.getStatus() != Event.EventStatus.COMPLETED) {
            throw new RuntimeException("Only completed events can be duplicated");
        }
        if (request.getEventTimeStart().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("New event start time must be in the future");
        }
        if (request.getEventTimeEnd().isBefore(request.getEventTimeStart())) {
            throw new RuntimeException("End time must be after start time");
        }

        Event copy = Event.builder()
                .eventName(source.getEventName() + " (Copy)")
                .venue(request.getVenue() != null ? request.getVenue() : source.getVenue())
                .eventTimeStart(request.getEventTimeStart())
                .eventTimeEnd(request.getEventTimeEnd())
                .skills(new ArrayList<>(source.getSkills()))
                .openForDifferentAbled(source.isOpenForDifferentAbled())
                .assignedStaff(new ArrayList<>())
                .optionalStaff(new ArrayList<>())
                .registeredStudents(new ArrayList<>())
                .status(Event.EventStatus.UPCOMING)
                .build();

        return toResponse(eventRepository.save(copy), null);
    }

    public List<EventDtos.EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(e -> toResponse(e, null))
                .collect(Collectors.toList());
    }

    public EventDtos.EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));
        return toResponse(event, null);
    }

    @Transactional
    public EventDtos.EventResponse updateEvent(Long id, EventDtos.UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));

        if (request.getEventName() != null) event.setEventName(request.getEventName());
        if (request.getVenue() != null) event.setVenue(request.getVenue());
        if (request.getEventTimeStart() != null) event.setEventTimeStart(request.getEventTimeStart());
        if (request.getEventTimeEnd() != null) event.setEventTimeEnd(request.getEventTimeEnd());
        if (request.getSkills() != null) event.setSkills(new ArrayList<>(request.getSkills()));
        event.setOpenForDifferentAbled(request.isOpenForDifferentAbled());
        if (request.getStatus() != null) event.setStatus(request.getStatus());
        if (request.getAssignedStaffIds() != null) {
            event.setAssignedStaff(new ArrayList<>(staffRepository.findAllById(request.getAssignedStaffIds())));
        }
        if (request.getOptionalStaffIds() != null) {
            event.setOptionalStaff(new ArrayList<>(staffRepository.findAllById(request.getOptionalStaffIds())));
        }

        return toResponse(eventRepository.save(event), null);
    }

    @Transactional
    public EventDtos.EventResponse assignStaff(Long eventId, EventDtos.AssignStaffRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
        if (request.getStaffIds() != null) {
            event.setAssignedStaff(new ArrayList<>(staffRepository.findAllById(request.getStaffIds())));
        }
        if (request.getOptionalStaffIds() != null) {
            event.setOptionalStaff(new ArrayList<>(staffRepository.findAllById(request.getOptionalStaffIds())));
        }
        return toResponse(eventRepository.save(event), null);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // ── Eligible staff with skillsMatch%, availability filter, sorted ──
    public List<StaffDtos.StaffResponse> getEligibleStaffForEvent(Long eventId, boolean differentAbledOnly) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
        return computeEligibleStaff(event.getSkills(), event.getEventTimeStart(), event.getEventTimeEnd(), differentAbledOnly);
    }

    public List<StaffDtos.StaffResponse> getEligibleStaffForSkills(
            List<String> skills, LocalDateTime start, LocalDateTime end, boolean differentAbledOnly) {
        return computeEligibleStaff(skills, start, end, differentAbledOnly);
    }

    private List<StaffDtos.StaffResponse> computeEligibleStaff(
            List<String> eventSkills, LocalDateTime eventStart, LocalDateTime eventEnd, boolean differentAbledOnly) {

        List<Staff> candidates;
        if (differentAbledOnly) {
            candidates = staffRepository.findSupportDifferentlyAbled();
        } else {
            candidates = staffRepository.findAll();
        }

        // Required window = 1hr before event start to 1hr after event end
        LocalDateTime requiredFrom = eventStart != null ? eventStart.minusHours(1) : null;
        LocalDateTime requiredTo = eventEnd != null ? eventEnd.plusHours(1) : null;

        List<StaffDtos.StaffResponse> result = new ArrayList<>();
        for (Staff staff : candidates) {
            // Compute skills match %
            double matchPct = 0;
            if (eventSkills != null && !eventSkills.isEmpty() && staff.getSkills() != null) {
                long matched = eventSkills.stream().filter(staff.getSkills()::contains).count();
                matchPct = (double) matched / eventSkills.size() * 100;
            }
            if (matchPct < 60) continue; // must have >= 60% skill match

            // Check availability window
            if (requiredFrom != null && !isStaffAvailable(staff, requiredFrom, requiredTo)) continue;

            StaffDtos.StaffResponse resp = staffService.toResponse(staff);
            resp.setSkillsMatchPercent(matchPct);
            result.add(resp);
        }

        // Sort by weekly hours ascending (less loaded first)
        result.sort(Comparator.comparingDouble(r -> r.getWeeklyHours() == null ? 0 : r.getWeeklyHours()));
        return result;
    }

    /**
     * Parse timeAvailable string like "MON-FRI 09:00-17:00" and check if staff
     * is available for the required window, also checking no holiday overlaps.
     */
    private boolean isStaffAvailable(Staff staff, LocalDateTime from, LocalDateTime to) {
        if (staff.getTimeAvailable() == null || staff.getTimeAvailable().isBlank()) return false;

        // Check holiday
        LocalDate checkDate = from.toLocalDate();
        while (!checkDate.isAfter(to.toLocalDate())) {
            if (staff.getHolidays() != null && staff.getHolidays().contains(checkDate.toString())) {
                return false;
            }
            checkDate = checkDate.plusDays(1);
        }

        // Parse "MON-FRI 09:00-17:00"
        try {
            String avail = staff.getTimeAvailable().trim();
            String[] parts = avail.split(" ");
            if (parts.length < 2) return true; // can't parse, assume available

            String dayRange = parts[0];
            String timeRange = parts[1];

            String[] timeParts = timeRange.split("-");
            LocalTime availStart = LocalTime.parse(timeParts[0]);
            LocalTime availEnd = LocalTime.parse(timeParts[1]);

            // Staff must be available every day in the event window
            LocalDate day = from.toLocalDate();
            while (!day.isAfter(to.toLocalDate())) {
                // Check if this day is in the staff's available days
                if (!isDayAvailable(day, dayRange)) return false;
                // Check time coverage for this day
                LocalTime reqFrom = day.equals(from.toLocalDate()) ? from.toLocalTime() : LocalTime.MIN;
                LocalTime reqTo = day.equals(to.toLocalDate()) ? to.toLocalTime() : LocalTime.MAX;
                if (availStart.isAfter(reqFrom) || availEnd.isBefore(reqTo)) return false;
                day = day.plusDays(1);
            }
            return true;
        } catch (Exception e) {
            return true; // If parse fails, don't block
        }
    }

    private static final Map<String, Integer> DAY_MAP = new LinkedHashMap<>();
    static {
        DAY_MAP.put("MON", 1); DAY_MAP.put("TUE", 2); DAY_MAP.put("WED", 3);
        DAY_MAP.put("THU", 4); DAY_MAP.put("FRI", 5); DAY_MAP.put("SAT", 6); DAY_MAP.put("SUN", 7);
    }

    private boolean isDayAvailable(LocalDate day, String dayRange) {
        // "MON-FRI" or "MON-SUN"
        String[] days = dayRange.split("-");
        if (days.length < 2) return true;
        int startDay = DAY_MAP.getOrDefault(days[0].toUpperCase(), 1);
        int endDay = DAY_MAP.getOrDefault(days[1].toUpperCase(), 7);
        int dayOfWeek = day.getDayOfWeek().getValue(); // 1=MON
        return dayOfWeek >= startDay && dayOfWeek <= endDay;
    }

    // ── Student: events matching skills ───────────────────────────
    public List<EventDtos.EventResponse> getAvailableEventsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<String> studentSkills = student.getSkills() == null ? new ArrayList<>() : student.getSkills();

        List<Event> allUpcoming = eventRepository.findAllUpcomingEvents(LocalDateTime.now());
        List<Long> myEventIds = eventRepository.findByRegisteredStudentId(studentId)
                .stream().map(Event::getId).collect(Collectors.toList());

        List<EventDtos.EventResponse> matchEvents = new ArrayList<>();
        List<EventDtos.EventResponse> otherEvents = new ArrayList<>();

        for (Event ev : allUpcoming) {
            if (myEventIds.contains(ev.getId())) continue; // skip already registered
            boolean hasMatch = !Collections.disjoint(studentSkills, ev.getSkills());
            EventDtos.EventResponse resp = toResponse(ev, studentId);
            if (hasMatch) matchEvents.add(resp);
            else otherEvents.add(resp);
        }

        // Return combined with match events first; frontend will split them
        List<EventDtos.EventResponse> combined = new ArrayList<>(matchEvents);
        combined.addAll(otherEvents);
        return combined;
    }

    public List<EventDtos.EventResponse> getAllEventsForStudent(Long studentId) {
        return eventRepository.findAllUpcomingEvents(LocalDateTime.now()).stream()
                .map(e -> toResponse(e, studentId))
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDtos.EventResponse registerStudent(Long eventId, Long studentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        boolean alreadyRegistered = event.getRegisteredStudents().stream()
                .anyMatch(s -> s.getId().equals(studentId));
        if (alreadyRegistered) throw new RuntimeException("Already registered for this event");

        event.getRegisteredStudents().add(student);
        return toResponse(eventRepository.save(event), studentId);
    }

    @Transactional
    public EventDtos.EventResponse unregisterStudent(Long eventId, Long studentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
        event.getRegisteredStudents().removeIf(s -> s.getId().equals(studentId));
        return toResponse(eventRepository.save(event), studentId);
    }

    public List<EventDtos.EventResponse> getStudentEvents(Long studentId) {
        return eventRepository.findByRegisteredStudentId(studentId).stream()
                .map(e -> toResponse(e, studentId))
                .collect(Collectors.toList());
    }

    // ── Feedback ─────────────────────────────────────────────────
    @Transactional
    public void submitFeedback(Long eventId, EventDtos.FeedbackRequest req) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
        if (event.getStatus() != Event.EventStatus.COMPLETED) {
            throw new RuntimeException("Feedback can only be submitted for completed events");
        }
        // Check not already given
        String prefix = req.getStudentId() + "|";
        boolean already = event.getFeedbackEntries().stream().anyMatch(f -> f.startsWith(prefix));
        if (already) throw new RuntimeException("Feedback already submitted");

        String entry = req.getStudentId() + "|" + req.getStaffRating() + "|" + req.getEventRating()
                + "|" + safe(req.getStaffComment()) + "|" + safe(req.getEventComment());
        event.getFeedbackEntries().add(entry);
        eventRepository.save(event);
    }

    private String safe(String s) { return s == null ? "" : s.replace("|", " "); }

    // ── toResponse ────────────────────────────────────────────────
    EventDtos.EventResponse toResponse(Event event, Long studentId) {
        List<StaffDtos.StaffResponse> assignedResp = event.getAssignedStaff() == null ? new ArrayList<>()
                : event.getAssignedStaff().stream().map(staffService::toResponse).collect(Collectors.toList());
        List<StaffDtos.StaffResponse> optionalResp = event.getOptionalStaff() == null ? new ArrayList<>()
                : event.getOptionalStaff().stream().map(staffService::toResponse).collect(Collectors.toList());

        boolean feedbackGiven = false;
        if (studentId != null && event.getFeedbackEntries() != null) {
            String prefix = studentId + "|";
            feedbackGiven = event.getFeedbackEntries().stream().anyMatch(f -> f.startsWith(prefix));
        }

        return new EventDtos.EventResponse(
                event.getId(),
                event.getEventName(),
                event.getVenue(),
                event.getEventTimeStart(),
                event.getEventTimeEnd(),
                event.getSkills(),
                event.isOpenForDifferentAbled(),
                assignedResp,
                optionalResp,
                event.getRegisteredStudents() == null ? 0 : event.getRegisteredStudents().size(),
                event.getStatus(),
                event.getDurationHours(),
                feedbackGiven
        );
    }
}
