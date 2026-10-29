package com.campusfind.repository;

import com.campusfind.entity.Item;
import com.campusfind.entity.User;
import com.campusfind.enums.ItemCategory;
import com.campusfind.enums.ItemStatus;
import com.campusfind.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByReportingUserOrderByCreatedAtDesc(User user);

    List<Item> findByStatusOrderByCreatedAtDesc(ItemStatus status);

    List<Item> findAllByOrderByCreatedAtDesc();

    @Query("SELECT i FROM Item i WHERE " +
           "(:type IS NULL OR i.type = :type) AND " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:location IS NULL OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:query IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           " OR LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY i.createdAt DESC")
    List<Item> searchAndFilter(@Param("type") ItemType type,
                               @Param("category") ItemCategory category,
                               @Param("status") ItemStatus status,
                               @Param("location") String location,
                               @Param("query") String query);

    // Find candidate opposite-type items for explainable matching algorithm
    @Query("SELECT i FROM Item i WHERE i.type = :targetType AND i.status = 'OPEN' AND i.id <> :excludeId")
    List<Item> findActiveCandidatesForMatching(@Param("targetType") ItemType targetType,
                                              @Param("excludeId") Long excludeId);

    long countByType(ItemType type);

    long countByStatus(ItemStatus status);

    long countByStatusIn(List<ItemStatus> statuses);
}
