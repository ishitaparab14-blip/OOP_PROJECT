package com.campusfind.repository;

import com.campusfind.entity.Claim;
import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByClaimantOrderByCreatedAtDesc(User claimant);

    List<Claim> findByItemOrderByCreatedAtDesc(Item item);

    List<Claim> findByStatusOrderByCreatedAtDesc(ClaimStatus status);

    List<Claim> findAllByOrderByCreatedAtDesc();

    boolean existsByItemAndClaimantAndStatusIn(Item item, User claimant, List<ClaimStatus> statuses);

    long countByStatus(ClaimStatus status);
}
