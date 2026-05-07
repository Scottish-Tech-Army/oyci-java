package com.oyci.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

public class StudentDtos {

    @Data
    public static class RegisterStudentRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        private List<String> skills;
        private boolean specialNeeds = false;
        private String name;
        private String email;
    }

    @Data
    public static class StudentResponse {
        private Long id;
        private String username;
        private String name;
        private String email;
        private boolean specialNeeds;
        private List<String> skills;
        private List<Long> completedEventIds;
        private boolean showCertificateBanner;

        public StudentResponse(Long id, String username, String name, String email,
                               boolean specialNeeds, List<String> skills,
                               List<Long> completedEventIds, boolean showCertificateBanner) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.email = email;
            this.specialNeeds = specialNeeds;
            this.skills = skills;
            this.completedEventIds = completedEventIds;
            this.showCertificateBanner = showCertificateBanner;
        }
    }

    @Data
    public static class UpdateSkillsRequest {
        private List<String> skills;
    }
}
