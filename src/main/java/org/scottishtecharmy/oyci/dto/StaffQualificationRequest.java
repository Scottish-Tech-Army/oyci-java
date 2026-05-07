package org.scottishtecharmy.oyci.dto;

import jakarta.validation.constraints.NotNull;

public class StaffQualificationRequest {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
