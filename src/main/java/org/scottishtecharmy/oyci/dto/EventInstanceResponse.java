package org.scottishtecharmy.oyci.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class EventInstanceResponse {

    private Long id;

    private Long eventTypeId;
    private String eventTypeName;
    private String eventTypeDescription;
    private Integer defDurMins;

    private Long locationId;
    private String locationName;
    private String locationAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime shiftStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime shiftEndTime;

    /** Staff assigned to this event instance. */
    private List<AssignedStaffResponse> assignedStaff;

    // ---- nested DTO ----

    public static class AssignedStaffResponse {
        private Long staffId;
        private String staffName;
        private String staffEmail;

        public Long getStaffId() { return staffId; }
        public void setStaffId(Long staffId) { this.staffId = staffId; }

        public String getStaffName() { return staffName; }
        public void setStaffName(String staffName) { this.staffName = staffName; }

        public String getStaffEmail() { return staffEmail; }
        public void setStaffEmail(String staffEmail) { this.staffEmail = staffEmail; }
    }

    // ---- getters & setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventTypeId() { return eventTypeId; }
    public void setEventTypeId(Long eventTypeId) { this.eventTypeId = eventTypeId; }

    public String getEventTypeName() { return eventTypeName; }
    public void setEventTypeName(String eventTypeName) { this.eventTypeName = eventTypeName; }

    public String getEventTypeDescription() { return eventTypeDescription; }
    public void setEventTypeDescription(String eventTypeDescription) { this.eventTypeDescription = eventTypeDescription; }

    public Integer getDefDurMins() { return defDurMins; }
    public void setDefDurMins(Integer defDurMins) { this.defDurMins = defDurMins; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public LocalDateTime getShiftStartTime() { return shiftStartTime; }
    public void setShiftStartTime(LocalDateTime shiftStartTime) { this.shiftStartTime = shiftStartTime; }

    public LocalDateTime getShiftEndTime() { return shiftEndTime; }
    public void setShiftEndTime(LocalDateTime shiftEndTime) { this.shiftEndTime = shiftEndTime; }

    public List<AssignedStaffResponse> getAssignedStaff() { return assignedStaff; }
    public void setAssignedStaff(List<AssignedStaffResponse> assignedStaff) { this.assignedStaff = assignedStaff; }
}

