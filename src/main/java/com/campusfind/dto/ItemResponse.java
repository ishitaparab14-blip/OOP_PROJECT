package com.campusfind.dto;

import com.campusfind.entity.Item;
import com.campusfind.enums.ItemCategory;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ItemResponse {
    private Long id;
    private String title;
    private String description;
    private ItemCategory category;
    private String categoryDisplayName;
    private ItemType type;
    private String location;
    private LocalDate relevantDate;
    private String imageUrl;
    private ItemStatus status;
    private Long reportingUserId;
    private String reportingUserName;
    private String reportingUserEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isOwner;
    private int claimsCount;

    public ItemResponse() {
    }

    public static ItemResponse fromEntity(Item item) {
        return fromEntity(item, null, 0);
    }

    public static ItemResponse fromEntity(Item item, Long currentUserId, int claimsCount) {
        ItemResponse dto = new ItemResponse();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setCategory(item.getCategory());
        dto.setCategoryDisplayName(item.getCategory() != null ? item.getCategory().getDisplayName() : "");
        dto.setType(item.getType());
        dto.setLocation(item.getLocation());
        dto.setRelevantDate(item.getRelevantDate());
        dto.setImageUrl(item.getImageUrl());
        dto.setStatus(item.getStatus());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());

        if (item.getReportingUser() != null) {
            dto.setReportingUserId(item.getReportingUser().getId());
            dto.setReportingUserName(item.getReportingUser().getFullName());
            dto.setReportingUserEmail(item.getReportingUser().getEmail());
            if (currentUserId != null) {
                dto.setOwner(item.getReportingUser().getId().equals(currentUserId));
            }
        }
        dto.setClaimsCount(claimsCount);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public String getCategoryDisplayName() {
        return categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this.categoryDisplayName = categoryDisplayName;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getRelevantDate() {
        return relevantDate;
    }

    public void setRelevantDate(LocalDate relevantDate) {
        this.relevantDate = relevantDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Long getReportingUserId() {
        return reportingUserId;
    }

    public void setReportingUserId(Long reportingUserId) {
        this.reportingUserId = reportingUserId;
    }

    public String getReportingUserName() {
        return reportingUserName;
    }

    public void setReportingUserName(String reportingUserName) {
        this.reportingUserName = reportingUserName;
    }

    public String getReportingUserEmail() {
        return reportingUserEmail;
    }

    public void setReportingUserEmail(String reportingUserEmail) {
        this.reportingUserEmail = reportingUserEmail;
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

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public int getClaimsCount() {
        return claimsCount;
    }

    public void setClaimsCount(int claimsCount) {
        this.claimsCount = claimsCount;
    }
}
