package com.campusfind.dto;

import com.campusfind.enums.ClaimStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClaimDecisionRequest {

    @NotNull(message = "Decision status is required (APPROVED or REJECTED)")
    private ClaimStatus status;

    @Size(max = 1500, message = "Administrator notes cannot exceed 1500 characters")
    private String adminNotes;

    public ClaimDecisionRequest() {
    }

    public ClaimDecisionRequest(ClaimStatus status, String adminNotes) {
        this.status = status;
        this.adminNotes = adminNotes;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
