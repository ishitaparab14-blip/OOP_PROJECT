package com.campusfind.controller;

import com.campusfind.dto.ItemRequest;
import com.campusfind.dto.ItemResponse;
import com.campusfind.dto.SimilarItemMatchDto;
import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ItemCategory;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;
import com.campusfind.service.ItemMatchingService;
import com.campusfind.service.ItemService;
import com.campusfind.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final ItemMatchingService itemMatchingService;

    public ItemController(ItemService itemService,
                          UserService userService,
                          ItemMatchingService itemMatchingService) {
        this.itemService = itemService;
        this.userService = userService;
        this.itemMatchingService = itemMatchingService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getItems(
            @RequestParam(required = false) ItemType type,
            @RequestParam(required = false) ItemCategory category,
            @RequestParam(required = false) ItemStatus status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String query) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        List<ItemResponse> list = itemService.searchAndFilter(type, category, status, location, query, currentUser);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(itemService.getItemDetails(id, currentUser));
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity<List<SimilarItemMatchDto>> getSimilarMatches(@PathVariable Long id) {
        Item item = itemService.getItemEntity(id);
        List<SimilarItemMatchDto> matches = itemMatchingService.findMatchesForItem(item);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ItemResponse>> getMyItems() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(itemService.getMyItems(currentUser));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest request) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        ItemResponse response = itemService.reportItem(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(itemService.updateItem(id, request, currentUser));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ItemResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        String statusStr = payload.get("status");
        ItemStatus status = ItemStatus.valueOf(statusStr.toUpperCase());
        return ResponseEntity.ok(itemService.updateItemStatus(id, status, currentUser));
    }
}
