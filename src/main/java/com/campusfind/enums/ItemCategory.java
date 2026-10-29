package com.campusfind.enums;

public enum ItemCategory {
    ELECTRONICS("Electronics & Gadgets"),
    BOOKS_STATIONERY("Books & Stationery"),
    BAGS_WALLETS("Bags & Wallets"),
    KEYS_CARDS("Keys, IDs & Cards"),
    CLOTHING_ACCESSORIES("Clothing & Accessories"),
    OTHER("Other Miscellaneous");

    private final String displayName;

    ItemCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
