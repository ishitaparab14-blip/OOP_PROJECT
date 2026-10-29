package com.campusfind.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClaimRequest {

    @NotBlank(message = "Claim explanation is required")
    @Size(min = 10, max = 1500, message = "Explanation must be between 10 and 1500 characters")
    private String claimExplanation;

    @NotBlank(message = "Verification details are required")
    @Size(min = 5, max = 1500, message = "Verification details must provide distinguishing features (e.g., marks, contents, serials)")
    private String verificationDetails;

    public ClaimRequest() {
    }

    public ClaimRequest(String claimExplanation, String verificationDetails) {
        this.claimExplanation = claimExplanation;
        this.verificationDetails = verificationDetails;
    }

    public String getClaimExplanation() {
        return claimExplanation;
    }

    public void setClaimExplanation(String claimExplanation) {
        this.claimExplanation = claimExplanation;
    }

    public String getVerificationDetails() {
        return verificationDetails;
    }

    public void setVerificationDetails(String verificationDetails) {
        this.verificationDetails = verificationDetails;
    }
}
