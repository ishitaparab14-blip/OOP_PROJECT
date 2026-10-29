package com.campusfind.entity;

import jakarta.persistence.*;

/**
 * Entity representing user alerts for report updates and claim outcomes.
 * Demonstrates Observer/Notification design pattern persistence.
 */
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "related_item_id")
    private Long relatedItemId;

    @Column(name = "related_claim_id")
    private Long relatedClaimId;

    public Notification() {
    }

    public Notification(User recipient, String message, Long relatedItemId, Long relatedClaimId) {
        this.recipient = recipient;
        this.message = message;
        this.relatedItemId = relatedItemId;
        this.relatedClaimId = relatedClaimId;
        this.isRead = false;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getRelatedItemId() {
        return relatedItemId;
    }

    public void setRelatedItemId(Long relatedItemId) {
        this.relatedItemId = relatedItemId;
    }

    public Long getRelatedClaimId() {
        return relatedClaimId;
    }

    public void setRelatedClaimId(Long relatedClaimId) {
        this.relatedClaimId = relatedClaimId;
    }
}
