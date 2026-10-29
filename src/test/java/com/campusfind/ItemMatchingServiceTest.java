package com.campusfind;

import com.campusfind.dto.SimilarItemMatchDto;
import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ItemCategory;
import com.campusfind.enums.ItemType;
import com.campusfind.enums.Role;
import com.campusfind.repository.ItemRepository;
import com.campusfind.service.ItemMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemMatchingServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemMatchingService matchingService;

    private User student1;
    private User student2;

    @BeforeEach
    void setUp() {
        student1 = new User("Student One", "one@campusfind.edu", "pass", Role.ROLE_STUDENT);
        student1.setId(1L);

        student2 = new User("Student Two", "two@campusfind.edu", "pass", Role.ROLE_STUDENT);
        student2.setId(2L);
    }

    @Test
    @DisplayName("Should recommend found item for lost item when category and keywords match")
    void shouldRecommendMatchingItem() {
        Item lostItem = new Item(
                "Lost Apple AirPods Pro with White Case",
                "Dropped my airpods in the library reading room",
                ItemCategory.ELECTRONICS,
                ItemType.LOST,
                "Central Library",
                LocalDate.now(),
                null,
                student1
        );
        lostItem.setId(10L);

        Item foundCandidate = new Item(
                "Found White Wireless AirPods Earbuds",
                "Found airpods resting on chair in the central library",
                ItemCategory.ELECTRONICS,
                ItemType.FOUND,
                "Central Library",
                LocalDate.now(),
                null,
                student2
        );
        foundCandidate.setId(20L);

        when(itemRepository.findActiveCandidatesForMatching(eq(ItemType.FOUND), eq(10L)))
                .thenReturn(List.of(foundCandidate));

        List<SimilarItemMatchDto> matches = matchingService.findMatchesForItem(lostItem);

        assertNotNull(matches);
        assertFalse(matches.isEmpty());
        SimilarItemMatchDto topMatch = matches.get(0);
        assertTrue(topMatch.getMatchScorePercent() >= 70, "Score should reflect high category, location and keyword similarity");
        assertTrue(topMatch.getMatchReasons().size() >= 3);
    }
}
