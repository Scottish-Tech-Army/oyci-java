package com.oyci.dto;

import com.oyci.model.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class EventDtos {

    @Data
    public static class CreateEventRequest {
        @NotBlank(message = "Event name is required")
        private String eventName;

        private String venue;

        @NotNull(message = "Start time is required")
        private LocalDateTime eventTimeStart;

        @NotNull(message = "End time is required")
        private LocalDateTime eventTimeEnd;

        @NotEmpty(message = "At least one skill is required")
        private List<String> skills;

        private boolean openForDifferentAbled = false;
        private List<Long> assignedStaffIds;
        private List<Long> optionalStaffIds;
    }

    @Data
    public static class UpdateEventRequest {
        private String eventName;
        private String venue;
        private LocalDateTime eventTimeStart;
        private LocalDateTime eventTimeEnd;
        private List<String> skills;
        private boolean openForDifferentAbled;
        private List<Long> assignedStaffIds;
        private List<Long> optionalStaffIds;
        private Event.EventStatus status;
    }

    @Data
    public static class EventResponse {
        private Long id;
        private String eventName;
        private String venue;
        private LocalDateTime eventTimeStart;
        private LocalDateTime eventTimeEnd;
        private List<String> skills;
        private boolean openForDifferentAbled;
        private List<StaffDtos.StaffResponse> assignedStaff;
        private List<StaffDtos.StaffResponse> optionalStaff;
        private int registeredStudentCount;
        private Event.EventStatus status;
        private double durationHours;
        private boolean feedbackGiven;

        public EventResponse(Long id, String eventName, String venue,
                             LocalDateTime eventTimeStart, LocalDateTime eventTimeEnd,
                             List<String> skills, boolean openForDifferentAbled,
                             List<StaffDtos.StaffResponse> assignedStaff,
                             List<StaffDtos.StaffResponse> optionalStaff,
                             int registeredStudentCount, Event.EventStatus status,
                             double durationHours, boolean feedbackGiven) {
            this.id = id;
            this.eventName = eventName;
            this.venue = venue;
            this.eventTimeStart = eventTimeStart;
            this.eventTimeEnd = eventTimeEnd;
            this.skills = skills;
            this.openForDifferentAbled = openForDifferentAbled;
            this.assignedStaff = assignedStaff;
            this.optionalStaff = optionalStaff;
            this.registeredStudentCount = registeredStudentCount;
            this.status = status;
            this.durationHours = durationHours;
            this.feedbackGiven = feedbackGiven;
        }
    }

    @Data
    public static class AssignStaffRequest {
        private List<Long> staffIds;
        private List<Long> optionalStaffIds;
    }

    @Data
    public static class FeedbackRequest {
        @NotNull
        private Long studentId;
        private int staffRating;       // 1-5
        private int eventRating;       // 1-5
        private String staffComment;
        private String eventComment;
    }

    @Data
    public static class DuplicateEventRequest {
        @NotNull(message = "New start time is required")
        private LocalDateTime eventTimeStart;
        @NotNull(message = "New end time is required")
        private LocalDateTime eventTimeEnd;
        private String venue;
    }
}
