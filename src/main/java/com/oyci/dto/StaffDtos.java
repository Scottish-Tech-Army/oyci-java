package com.oyci.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

public class StaffDtos {

    @Data
    public static class CreateStaffRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Valid email required")
        private String email;

        @NotBlank(message = "Availability is required")
        private String timeAvailable;

        @NotEmpty(message = "At least one skill is required")
        private List<String> skills;

        private boolean supportDifferentlyAbled = false;
    }

    @Data
    public static class StaffResponse {
        private Long id;
        private String username;
        private String name;
        private String email;
        private String timeAvailable;
        private List<String> skills;
        private Double weeklyHours;
        private boolean supportDifferentlyAbled;
        private List<String> holidays;
        private int withdrawalsThisMonth;
        // computed for staff-picker
        private Double skillsMatchPercent;

        public StaffResponse(Long id, String username, String name, String email,
                             String timeAvailable, List<String> skills, Double weeklyHours,
                             boolean supportDifferentlyAbled, List<String> holidays,
                             int withdrawalsThisMonth) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.email = email;
            this.timeAvailable = timeAvailable;
            this.skills = skills;
            this.weeklyHours = weeklyHours;
            this.supportDifferentlyAbled = supportDifferentlyAbled;
            this.holidays = holidays;
            this.withdrawalsThisMonth = withdrawalsThisMonth;
        }
    }

    @Data
    public static class UpdateStaffRequest {
        private String name;
        private String email;
        private String timeAvailable;
        private List<String> skills;
        private Boolean supportDifferentlyAbled;
    }

    @Data
    public static class StaffSelfUpdateRequest {
        private List<String> skills;
        private String timeAvailable;
    }

    @Data
    public static class HolidayRequest {
        private String holidayDate; // ISO date "YYYY-MM-DD"
    }

    @Data
    public static class WithdrawRequest {
        private Long eventId;
    }
}
