package com.campusfind.entity;

import com.campusfind.enums.ClaimStatus;
import jakarta.persistence.*;

/**
 * Entity representing an ownership claim submitted by a student for a found item.
 * Demonstrates OOP Associations and State Lifecycle Management.
 */
@Entity
@Table(name = "claims")
public class Claim extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimant_id", nullable = false)
    private User claimant;

    @Column(name = "claim_explanation", nullable = false, length = 1500)
    private String claimExplanation;

    @Column(name = "verification_details", nullable = false, length = 1500)
    private String verificationDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ClaimStatus status = ClaimStatus.PENDING;

    @Column(name = "admin_notes", length = 1500)
    private String adminNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    public Claim() {
    }

    public Claim(Item item, User claimant, String claimExplanation, String verificationDetails) {
        this.item = item;
        this.claimant = claimant;
        this.claimExplanation = claimExplanation;
        this.verificationDetails = verificationDetails;
        this.status = ClaimStatus.PENDING;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getClaimant() {
        return claimant;
    }

    public void setClaimant(User claimant) {
        this.claimant = claimant;
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

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }
}
