package com.campusfind.service;

import com.campusfind.dto.ClaimDecisionRequest;
import com.campusfind.dto.ClaimRequest;
import com.campusfind.dto.ClaimResponse;
import com.campusfind.entity.Claim;
import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ClaimStatus;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;
import com.campusfind.enums.Role;
import com.campusfind.exception.BadRequestException;
import com.campusfind.exception.DuplicateResourceException;
import com.campusfind.exception.ResourceNotFoundException;
import com.campusfind.exception.UnauthorizedException;
import com.campusfind.repository.ClaimRepository;
import com.campusfind.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ItemRepository itemRepository;
    private final NotificationService notificationService;

    public ClaimService(ClaimRepository claimRepository,
                        ItemRepository itemRepository,
                        NotificationService notificationService) {
        this.claimRepository = claimRepository;
        this.itemRepository = itemRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public ClaimResponse submitClaim(Long itemId, ClaimRequest request, User currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("User must be logged in to claim an item.");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        // Validation rules:
        // 1. Can only claim FOUND items
        if (item.getType() != ItemType.FOUND) {
            throw new BadRequestException("Ownership claims can only be submitted for FOUND items.");
        }

        // 2. Cannot claim an item you reported yourself
        if (item.getReportingUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot submit an ownership claim for an item you reported finding.");
        }

        // 3. Item must be available
        if (item.getStatus() == ItemStatus.RETURNED || item.getStatus() == ItemStatus.RESOLVED || item.getStatus() == ItemStatus.ARCHIVED) {
            throw new BadRequestException("This item has already been returned or resolved and cannot be claimed.");
        }

        // 4. Prevent duplicate pending claims by the same student
        boolean alreadyClaimed = claimRepository.existsByItemAndClaimantAndStatusIn(
                item, currentUser, List.of(ClaimStatus.PENDING, ClaimStatus.APPROVED)
        );
        if (alreadyClaimed) {
            throw new DuplicateResourceException("You already have an active claim submitted for this item.");
        }

        Claim claim = new Claim(
                item,
                currentUser,
                request.getClaimExplanation().trim(),
                request.getVerificationDetails().trim()
        );

        Claim savedClaim = claimRepository.save(claim);

        // Update item status to CLAIM_PENDING if it was OPEN
        if (item.getStatus() == ItemStatus.OPEN) {
            item.setStatus(ItemStatus.CLAIM_PENDING);
            itemRepository.save(item);
        }

        // Send notifications
        // To claimant:
        notificationService.sendNotification(
                currentUser,
                "Your ownership claim for found item '" + item.getTitle() + "' has been submitted and is pending administrator review.",
                item.getId(),
                savedClaim.getId()
        );

        // To reporting finder:
        notificationService.sendNotification(
                item.getReportingUser(),
                "A student has submitted an ownership claim for your found item '" + item.getTitle() + "'. The campus administrator will review the verification details.",
                item.getId(),
                savedClaim.getId()
        );

        return ClaimResponse.fromEntity(savedClaim);
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> getMyClaims(User currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("User must be logged in to view their claims.");
        }

        return claimRepository.findByClaimantOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(ClaimResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> getClaimsForItem(Long itemId, User currentUser) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isFinder = item.getReportingUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isFinder) {
            throw new UnauthorizedException("You are not authorized to view claims for this item.");
        }

        return claimRepository.findByItemOrderByCreatedAtDesc(item)
                .stream()
                .map(ClaimResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> getAllClaimsForAdmin() {
        return claimRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ClaimResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClaimResponse processClaimDecision(Long claimId, ClaimDecisionRequest decision, User adminUser) {
        if (adminUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Only campus administrators can approve or reject claims.");
        }

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new BadRequestException("This claim has already been decided (" + claim.getStatus() + ").");
        }

        ClaimStatus newStatus = decision.getStatus();
        if (newStatus != ClaimStatus.APPROVED && newStatus != ClaimStatus.REJECTED) {
            throw new BadRequestException("Decision must be either APPROVED or REJECTED.");
        }

        claim.setStatus(newStatus);
        claim.setAdminNotes(decision.getAdminNotes() != null ? decision.getAdminNotes().trim() : null);
        claim.setReviewer(adminUser);

        Item item = claim.getItem();

        if (newStatus == ClaimStatus.APPROVED) {
            // Update item to RETURNED
            item.setStatus(ItemStatus.RETURNED);
            itemRepository.save(item);

            // Prevent conflicting approvals: reject all other pending claims for this item
            List<Claim> otherClaims = claimRepository.findByItemOrderByCreatedAtDesc(item);
            for (Claim other : otherClaims) {
                if (!other.getId().equals(claim.getId()) && other.getStatus() == ClaimStatus.PENDING) {
                    other.setStatus(ClaimStatus.REJECTED);
                    other.setAdminNotes("Another verified ownership claim for this item was approved by the administrator.");
                    other.setReviewer(adminUser);
                    claimRepository.save(other);

                    notificationService.sendNotification(
                            other.getClaimant(),
                            "Your claim for '" + item.getTitle() + "' was not approved because another verified owner was approved.",
                            item.getId(),
                            other.getId()
                    );
                }
            }

            // Notify approved claimant
            notificationService.sendNotification(
                    claim.getClaimant(),
                    "Congratulations! Your claim for '" + item.getTitle() + "' has been APPROVED. Please visit the Campus Lost & Found desk to collect your item.",
                    item.getId(),
                    claim.getId()
            );

            // Notify reporting finder
            notificationService.sendNotification(
                    item.getReportingUser(),
                    "The claim for your found item '" + item.getTitle() + "' has been approved and handed over. Thank you for being a good citizen!",
                    item.getId(),
                    claim.getId()
            );

        } else {
            // If REJECTED, check if there are other pending claims
            List<Claim> allItemClaims = claimRepository.findByItemOrderByCreatedAtDesc(item);
            boolean hasOtherPending = allItemClaims.stream()
                    .anyMatch(c -> !c.getId().equals(claim.getId()) && c.getStatus() == ClaimStatus.PENDING);

            if (!hasOtherPending && item.getStatus() == ItemStatus.CLAIM_PENDING) {
                item.setStatus(ItemStatus.OPEN);
                itemRepository.save(item);
            }

            // Notify claimant of rejection
            String notesMsg = claim.getAdminNotes() != null ? " Reason: " + claim.getAdminNotes() : "";
            notificationService.sendNotification(
                    claim.getClaimant(),
                    "Your claim for '" + item.getTitle() + "' was reviewed and REJECTED." + notesMsg,
                    item.getId(),
                    claim.getId()
            );
        }

        Claim savedClaim = claimRepository.save(claim);
        return ClaimResponse.fromEntity(savedClaim);
    }

    @Transactional
    public void withdrawClaim(Long claimId, User currentUser) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));

        if (!claim.getClaimant().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not permitted to withdraw another student's claim.");
        }

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new BadRequestException("Only PENDING claims can be withdrawn.");
        }

        claim.setStatus(ClaimStatus.WITHDRAWN);
        claimRepository.save(claim);

        // Check if item should revert to OPEN
        Item item = claim.getItem();
        List<Claim> allItemClaims = claimRepository.findByItemOrderByCreatedAtDesc(item);
        boolean hasOtherPending = allItemClaims.stream()
                .anyMatch(c -> !c.getId().equals(claim.getId()) && c.getStatus() == ClaimStatus.PENDING);

        if (!hasOtherPending && item.getStatus() == ItemStatus.CLAIM_PENDING) {
            item.setStatus(ItemStatus.OPEN);
            itemRepository.save(item);
        }
    }
}
