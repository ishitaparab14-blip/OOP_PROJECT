package com.campusfind.service;

import com.campusfind.dto.ItemRequest;
import com.campusfind.dto.ItemResponse;
import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ItemCategory;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;
import com.campusfind.enums.Role;
import com.campusfind.exception.BadRequestException;
import com.campusfind.exception.ResourceNotFoundException;
import com.campusfind.exception.UnauthorizedException;
import com.campusfind.repository.ClaimRepository;
import com.campusfind.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ClaimRepository claimRepository;
    private final NotificationService notificationService;

    public ItemService(ItemRepository itemRepository,
                       ClaimRepository claimRepository,
                       NotificationService notificationService) {
        this.itemRepository = itemRepository;
        this.claimRepository = claimRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public ItemResponse reportItem(ItemRequest request, User currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("User must be logged in to report an item.");
        }

        Item item = new Item(
                request.getTitle().trim(),
                request.getDescription().trim(),
                request.getCategory(),
                request.getType(),
                request.getLocation().trim(),
                request.getRelevantDate(),
                request.getImageUrl() != null && !request.getImageUrl().isBlank() ? request.getImageUrl().trim() : null,
                currentUser
        );

        Item saved = itemRepository.save(item);

        // Send confirmation notification to user
        String alertMsg = "Your " + saved.getType().name().toLowerCase() + " item report '" +
                saved.getTitle() + "' has been successfully published.";
        notificationService.sendNotification(currentUser, alertMsg, saved.getId(), null);

        return ItemResponse.fromEntity(saved, currentUser.getId(), 0);
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> searchAndFilter(ItemType type, ItemCategory category, ItemStatus status,
                                             String location, String query, User currentUser) {
        Long currentUserId = currentUser != null ? currentUser.getId() : null;

        String loc = (location != null && !location.isBlank()) ? location.trim() : null;
        String q = (query != null && !query.isBlank()) ? query.trim() : null;

        List<Item> items = itemRepository.searchAndFilter(type, category, status, loc, q);
        return items.stream()
                .map(item -> {
                    int claimsCount = claimRepository.findByItemOrderByCreatedAtDesc(item).size();
                    return ItemResponse.fromEntity(item, currentUserId, claimsCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemDetails(Long id, User currentUser) {
        Item item = getItemEntity(id);
        Long currentUserId = currentUser != null ? currentUser.getId() : null;
        int claimsCount = claimRepository.findByItemOrderByCreatedAtDesc(item).size();
        return ItemResponse.fromEntity(item, currentUserId, claimsCount);
    }

    @Transactional(readOnly = true)
    public Item getItemEntity(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getMyItems(User currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("User must be logged in to view their reports.");
        }

        List<Item> items = itemRepository.findByReportingUserOrderByCreatedAtDesc(currentUser);
        return items.stream()
                .map(item -> {
                    int claimsCount = claimRepository.findByItemOrderByCreatedAtDesc(item).size();
                    return ItemResponse.fromEntity(item, currentUser.getId(), claimsCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest request, User currentUser) {
        Item item = getItemEntity(id);

        // Security check: Only original reporting user or Admin can edit
        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isOwner = item.getReportingUser().getId().equals(currentUser.getId());

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You are not permitted to edit this item report.");
        }

        if (item.getStatus() == ItemStatus.RETURNED || item.getStatus() == ItemStatus.RESOLVED) {
            throw new BadRequestException("Resolved or returned items cannot be modified.");
        }

        item.setTitle(request.getTitle().trim());
        item.setDescription(request.getDescription().trim());
        item.setCategory(request.getCategory());
        item.setType(request.getType());
        item.setLocation(request.getLocation().trim());
        item.setRelevantDate(request.getRelevantDate());
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            item.setImageUrl(request.getImageUrl().trim());
        }

        Item saved = itemRepository.save(item);
        int claimsCount = claimRepository.findByItemOrderByCreatedAtDesc(saved).size();
        return ItemResponse.fromEntity(saved, currentUser.getId(), claimsCount);
    }

    @Transactional
    public ItemResponse updateItemStatus(Long id, ItemStatus newStatus, User currentUser) {
        Item item = getItemEntity(id);

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isOwner = item.getReportingUser().getId().equals(currentUser.getId());

        // Students can mark their own lost items as RESOLVED / ARCHIVED. Admins can update any status.
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You are not authorized to update this item's status.");
        }

        item.setStatus(newStatus);
        Item saved = itemRepository.save(item);

        // Notify reporting student if updated by admin
        if (isAdmin && !isOwner) {
            notificationService.sendNotification(
                    saved.getReportingUser(),
                    "The status of your item '" + saved.getTitle() + "' was updated to " + newStatus.name() + " by an administrator.",
                    saved.getId(),
                    null
            );
        }

        int claimsCount = claimRepository.findByItemOrderByCreatedAtDesc(saved).size();
        return ItemResponse.fromEntity(saved, currentUser.getId(), claimsCount);
    }
}
