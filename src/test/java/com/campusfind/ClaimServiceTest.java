package com.campusfind;

import com.campusfind.dto.ClaimDecisionRequest;
import com.campusfind.dto.ClaimRequest;
import com.campusfind.dto.ClaimResponse;
import com.campusfind.entity.Claim;
import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ClaimStatus;
import com.campusfind.enums.ItemCategory;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;
import com.campusfind.enums.Role;
import com.campusfind.exception.BadRequestException;
import com.campusfind.exception.DuplicateResourceException;
import com.campusfind.repository.ClaimRepository;
import com.campusfind.repository.ItemRepository;
import com.campusfind.service.ClaimService;
import com.campusfind.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ClaimService claimService;

    private User finder;
    private User claimant;
    private User admin;
    private Item foundItem;

    @BeforeEach
    void setUp() {
        finder = new User("Finder Student", "finder@campusfind.edu", "pass", Role.ROLE_STUDENT);
        finder.setId(1L);

        claimant = new User("Claimant Student", "claimant@campusfind.edu", "pass", Role.ROLE_STUDENT);
        claimant.setId(2L);

        admin = new User("Admin", "admin@campusfind.edu", "pass", Role.ROLE_ADMIN);
        admin.setId(99L);

        foundItem = new Item(
                "Found Water Bottle",
                "Blue stainless steel bottle",
                ItemCategory.OTHER,
                ItemType.FOUND,
                "Cafeteria",
                LocalDate.now(),
                null,
                finder
        );
        foundItem.setId(100L);
    }

    @Test
    @DisplayName("Should prevent claiming an item that claimant reported themselves")
    void shouldPreventClaimingOwnItem() {
        when(itemRepository.findById(100L)).thenReturn(Optional.of(foundItem));

        ClaimRequest request = new ClaimRequest("This is my bottle", "Has an eagle sticker on bottom");

        assertThrows(BadRequestException.class, () -> {
            claimService.submitClaim(100L, request, finder); // finder trying to claim own item
        });
    }

    @Test
    @DisplayName("Should prevent claiming a LOST item report")
    void shouldPreventClaimingLostItem() {
        Item lostItem = new Item("Lost Keys", "Room keys", ItemCategory.KEYS_CARDS, ItemType.LOST, "Hall", LocalDate.now(), null, finder);
        lostItem.setId(101L);

        when(itemRepository.findById(101L)).thenReturn(Optional.of(lostItem));

        ClaimRequest request = new ClaimRequest("Those are my keys", "3 brass keys");

        assertThrows(BadRequestException.class, () -> {
            claimService.submitClaim(101L, request, claimant);
        });
    }

    @Test
    @DisplayName("Should prevent duplicate active claims by same student")
    void shouldPreventDuplicateActiveClaims() {
        when(itemRepository.findById(100L)).thenReturn(Optional.of(foundItem));
        when(claimRepository.existsByItemAndClaimantAndStatusIn(eq(foundItem), eq(claimant), anyList()))
                .thenReturn(true);

        ClaimRequest request = new ClaimRequest("Duplicate claim attempt", "Proof details here");

        assertThrows(DuplicateResourceException.class, () -> {
            claimService.submitClaim(100L, request, claimant);
        });
    }

    @Test
    @DisplayName("Admin approving claim should mark item as RETURNED and reject other pending claims")
    void shouldApproveClaimAndRejectOthers() {
        Claim claim1 = new Claim(foundItem, claimant, "Explanation 1", "Proof 1");
        claim1.setId(501L);

        User claimant2 = new User("Other Student", "other@campusfind.edu", "pass", Role.ROLE_STUDENT);
        claimant2.setId(3L);
        Claim claim2 = new Claim(foundItem, claimant2, "Explanation 2", "Proof 2");
        claim2.setId(502L);

        when(claimRepository.findById(501L)).thenReturn(Optional.of(claim1));
        when(claimRepository.findByItemOrderByCreatedAtDesc(foundItem)).thenReturn(List.of(claim1, claim2));
        when(claimRepository.save(any(Claim.class))).thenAnswer(i -> i.getArgument(0));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArgument(0));

        ClaimDecisionRequest decision = new ClaimDecisionRequest(ClaimStatus.APPROVED, "Verified student ID and scratch marks.");
        ClaimResponse response = claimService.processClaimDecision(501L, decision, admin);

        assertEquals(ClaimStatus.APPROVED, response.getStatus());
        assertEquals(ItemStatus.RETURNED, foundItem.getStatus());
        assertEquals(ClaimStatus.REJECTED, claim2.getStatus(), "Conflicting pending claims must be automatically rejected upon approval");
    }
}
