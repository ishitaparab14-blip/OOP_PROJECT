package com.campusfind.service;

import com.campusfind.dto.DashboardStatsDto;
import com.campusfind.enums.ClaimStatus;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;
import com.campusfind.enums.Role;
import com.campusfind.repository.ClaimRepository;
import com.campusfind.repository.ItemRepository;
import com.campusfind.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardStatsService {

    private final ItemRepository itemRepository;
    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;

    public DashboardStatsService(ItemRepository itemRepository,
                                 ClaimRepository claimRepository,
                                 UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto getLiveStatistics() {
        long totalItems = itemRepository.count();
        long lostItems = itemRepository.countByType(ItemType.LOST);
        long foundItems = itemRepository.countByType(ItemType.FOUND);
        long returnedOrResolved = itemRepository.countByStatusIn(List.of(ItemStatus.RETURNED, ItemStatus.RESOLVED));
        long pendingClaims = claimRepository.countByStatus(ClaimStatus.PENDING);
        long totalStudents = userRepository.countByRole(Role.ROLE_STUDENT);

        return new DashboardStatsDto(
                totalItems,
                lostItems,
                foundItems,
                returnedOrResolved,
                pendingClaims,
                totalStudents
        );
    }
}
