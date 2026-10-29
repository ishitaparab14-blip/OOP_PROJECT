package com.campusfind.dto;

public class DashboardStatsDto {
    private long totalItems;
    private long lostItems;
    private long foundItems;
    private long returnedOrResolvedItems;
    private long pendingClaims;
    private long totalStudents;

    public DashboardStatsDto() {
    }

    public DashboardStatsDto(long totalItems, long lostItems, long foundItems,
                             long returnedOrResolvedItems, long pendingClaims, long totalStudents) {
        this.totalItems = totalItems;
        this.lostItems = lostItems;
        this.foundItems = foundItems;
        this.returnedOrResolvedItems = returnedOrResolvedItems;
        this.pendingClaims = pendingClaims;
        this.totalStudents = totalStudents;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public long getLostItems() {
        return lostItems;
    }

    public void setLostItems(long lostItems) {
        this.lostItems = lostItems;
    }

    public long getFoundItems() {
        return foundItems;
    }

    public void setFoundItems(long foundItems) {
        this.foundItems = foundItems;
    }

    public long getReturnedOrResolvedItems() {
        return returnedOrResolvedItems;
    }

    public void setReturnedOrResolvedItems(long returnedOrResolvedItems) {
        this.returnedOrResolvedItems = returnedOrResolvedItems;
    }

    public long getPendingClaims() {
        return pendingClaims;
    }

    public void setPendingClaims(long pendingClaims) {
        this.pendingClaims = pendingClaims;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }
}
