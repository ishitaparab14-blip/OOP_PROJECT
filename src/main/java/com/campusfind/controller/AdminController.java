package com.campusfind.controller;

import com.campusfind.dto.ClaimDecisionRequest;
import com.campusfind.dto.ClaimResponse;
import com.campusfind.dto.ItemResponse;
import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ItemStatus;
import com.campusfind.repository.ItemRepository;
import com.campusfind.service.ClaimService;
import com.campusfind.service.ItemService;
import com.campusfind.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ClaimService claimService;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public AdminController(ClaimService claimService,
                           ItemService itemService,
                           UserService userService,
                           ItemRepository itemRepository) {
        this.claimService = claimService;
        this.itemService = itemService;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @GetMapping("/claims")
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaimsForAdmin());
    }

    @PostMapping("/claims/{claimId}/decision")
    public ResponseEntity<ClaimResponse> processDecision(@PathVariable Long claimId,
                                                         @Valid @RequestBody ClaimDecisionRequest decision) {
        User adminUser = userService.getCurrentAuthenticatedUser();
        ClaimResponse response = claimService.processClaimDecision(claimId, decision, adminUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        User adminUser = userService.getCurrentAuthenticatedUser();
        List<Item> items = itemRepository.findAllByOrderByCreatedAtDesc();
        List<ItemResponse> responses = items.stream()
                .map(i -> ItemResponse.fromEntity(i, adminUser.getId(), 0))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/items/{id}/status")
    public ResponseEntity<ItemResponse> changeItemStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        User adminUser = userService.getCurrentAuthenticatedUser();
        String statusStr = payload.get("status");
        ItemStatus status = ItemStatus.valueOf(statusStr.toUpperCase());
        return ResponseEntity.ok(itemService.updateItemStatus(id, status, adminUser));
    }
}
