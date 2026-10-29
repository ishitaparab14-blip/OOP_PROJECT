package com.campusfind.service;

import com.campusfind.dto.ItemResponse;
import com.campusfind.dto.SimilarItemMatchDto;
import com.campusfind.entity.Item;
import com.campusfind.enums.ItemType;
import com.campusfind.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service providing explainable matching recommendations between Lost and Found items.
 * Demonstrates business logic abstraction and explainable algorithmic decision-making.
 */
@Service
public class ItemMatchingService {

    private final ItemRepository itemRepository;

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "in", "on", "at", "to", "for", "with", "by", "of", "from", "is", "my", "lost", "found"
    );

    public ItemMatchingService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<SimilarItemMatchDto> findMatchesForItem(Item sourceItem) {
        // We match against candidate items of the OPPOSITE type
        // e.g. for a LOST item, look for candidates among FOUND items
        ItemType oppositeType = (sourceItem.getType() == ItemType.LOST) ? ItemType.FOUND : ItemType.LOST;
        List<Item> candidates = itemRepository.findActiveCandidatesForMatching(oppositeType, sourceItem.getId());

        List<SimilarItemMatchDto> matches = new ArrayList<>();

        for (Item candidate : candidates) {
            int score = 0;
            List<String> reasons = new ArrayList<>();

            // 1. Category Match (40 points)
            if (sourceItem.getCategory() == candidate.getCategory()) {
                score += 40;
                reasons.add("Exact category match: " + sourceItem.getCategory().getDisplayName());
            }

            // 2. Keyword overlap in Title and Description (Up to 30 points)
            Set<String> sourceWords = extractKeywords(sourceItem.getTitle() + " " + sourceItem.getDescription());
            Set<String> candidateWords = extractKeywords(candidate.getTitle() + " " + candidate.getDescription());

            Set<String> intersection = new HashSet<>(sourceWords);
            intersection.retainAll(candidateWords);

            if (!intersection.isEmpty()) {
                int keywordScore = Math.min(30, intersection.size() * 10);
                score += keywordScore;
                reasons.add("Matching keyword(s): " + String.join(", ", intersection));
            }

            // 3. Location Proximity / Similarity (20 points)
            String loc1 = sourceItem.getLocation().toLowerCase().trim();
            String loc2 = candidate.getLocation().toLowerCase().trim();
            if (loc1.equalsIgnoreCase(loc2) || loc1.contains(loc2) || loc2.contains(loc1)) {
                score += 20;
                reasons.add("Same or nearby location: " + candidate.getLocation());
            }

            // 4. Date Proximity (10 points if within 3 days)
            if (sourceItem.getRelevantDate() != null && candidate.getRelevantDate() != null) {
                long daysDiff = Math.abs(ChronoUnit.DAYS.between(sourceItem.getRelevantDate(), candidate.getRelevantDate()));
                if (daysDiff <= 3) {
                    score += 10;
                    reasons.add("Reported dates are within " + daysDiff + " day(s) of each other");
                }
            }

            // Filter out low scores (threshold 30%)
            if (score >= 30) {
                matches.add(new SimilarItemMatchDto(
                        ItemResponse.fromEntity(candidate),
                        score,
                        reasons
                ));
            }
        }

        // Sort descending by match score
        matches.sort((a, b) -> Integer.compare(b.getMatchScorePercent(), a.getMatchScorePercent()));

        // Return top 5 suggestions
        return matches.stream().limit(5).collect(Collectors.toList());
    }

    private Set<String> extractKeywords(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(text.toLowerCase().split("[^a-zA-Z0-9]+"))
                .filter(w -> w.length() > 2)
                .filter(w -> !STOP_WORDS.contains(w))
                .collect(Collectors.toSet());
    }
}
