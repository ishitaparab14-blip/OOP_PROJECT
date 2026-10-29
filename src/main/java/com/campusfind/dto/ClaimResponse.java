package com.campusfind.dto;

import com.campusfind.entity.Claim;
import com.campusfind.enums.ClaimStatus;

import java.time.LocalDateTime;

public class ClaimResponse {
    private Long id;
    private Long itemId;
    private String itemTitle;
    private String itemCategory;
    private String itemLocation;
    private String itemImageUrl;

    private Long claimantId;
    private String claimantName;
    private String claimantEmail;

    private String claimExplanation;
    private String verificationDetails;
    private ClaimStatus status;
    private String adminNotes;

    private Long reviewerId;
    private String reviewerName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ClaimResponse() {
    }

    public static ClaimResponse fromEntity(Claim claim) {
        ClaimResponse dto = new ClaimResponse();
        dto.setId(claim.getId());

        if (claim.getItem() != null) {
            dto.setItemId(claim.getItem().getId());
            dto.setItemTitle(claim.getItem().getTitle());
            dto.setItemCategory(claim.getItem().getCategory() != null ? claim.getItem().getCategory().getDisplayName() : "");
            dto.setItemLocation(claim.getItem().getLocation());
            dto.setItemImageUrl(claim.getItem().getImageUrl());
        }

        if (claim.getClaimant() != null) {
            dto.setClaimantId(claim.getClaimant().getId());
            dto.setClaimantName(claim.getClaimant().getFullName());
            dto.setClaimantEmail(claim.getClaimant().getEmail());
        }

        dto.setClaimExplanation(claim.getClaimExplanation());
        dto.setVerificationDetails(claim.getVerificationDetails());
        dto.setStatus(claim.getStatus());
        dto.setAdminNotes(claim.getAdminNotes());

        if (claim.getReviewer() != null) {
            dto.setReviewerId(claim.getReviewer().getId());
            dto.setReviewerName(claim.getReviewer().getFullName());
        }

        dto.setCreatedAt(claim.getCreatedAt());
        dto.setUpdatedAt(claim.getUpdatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public Long getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(Long claimantId) {
        this.claimantId = claimantId;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public String getClaimantEmail() {
        return claimantEmail;
    }

    public void setClaimantEmail(String claimantEmail) {
        this.claimantEmail = claimantEmail;
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

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
