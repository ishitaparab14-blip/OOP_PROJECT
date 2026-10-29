package com.campusfind.controller;

import com.campusfind.dto.ClaimRequest;
import com.campusfind.dto.ClaimResponse;
import com.campusfind.entity.User;
import com.campusfind.service.ClaimService;
import com.campusfind.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;
    private final UserService userService;

    public ClaimController(ClaimService claimService, UserService userService) {
        this.claimService = claimService;
        this.userService = userService;
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<ClaimResponse> submitClaim(@PathVariable Long itemId, @Valid @RequestBody ClaimRequest request) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        ClaimResponse response = claimService.submitClaim(itemId, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ClaimResponse>> getMyClaims() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(claimService.getMyClaims(currentUser));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsForItem(@PathVariable Long itemId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(claimService.getClaimsForItem(itemId, currentUser));
    }

    @PostMapping("/{claimId}/withdraw")
    public ResponseEntity<Map<String, Object>> withdrawClaim(@PathVariable Long claimId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        claimService.withdrawClaim(claimId, currentUser);
        return ResponseEntity.ok(Map.of("success", true, "message", "Claim withdrawn successfully."));
    }
}
