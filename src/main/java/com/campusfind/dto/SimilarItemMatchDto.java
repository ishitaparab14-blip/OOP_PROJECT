package com.campusfind.dto;

import java.util.List;

public class SimilarItemMatchDto {
    private ItemResponse item;
    private int matchScorePercent;
    private List<String> matchReasons;

    public SimilarItemMatchDto() {
    }

    public SimilarItemMatchDto(ItemResponse item, int matchScorePercent, List<String> matchReasons) {
        this.item = item;
        this.matchScorePercent = matchScorePercent;
        this.matchReasons = matchReasons;
    }

    public ItemResponse getItem() {
        return item;
    }

    public void setItem(ItemResponse item) {
        this.item = item;
    }

    public int getMatchScorePercent() {
        return matchScorePercent;
    }

    public void setMatchScorePercent(int matchScorePercent) {
        this.matchScorePercent = matchScorePercent;
    }

    public List<String> getMatchReasons() {
        return matchReasons;
    }

    public void setMatchReasons(List<String> matchReasons) {
        this.matchReasons = matchReasons;
    }
}
