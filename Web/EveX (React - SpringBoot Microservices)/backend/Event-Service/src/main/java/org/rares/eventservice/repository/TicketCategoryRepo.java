package org.rares.eventservice.repository;

import org.rares.eventservice.model.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketCategoryRepo extends JpaRepository<TicketCategory, Integer> {
    List<TicketCategory> findByEventId(Integer eventId);

    Optional<TicketCategory> findByEventIdAndCategory(Integer eventId, String category);

    @Modifying
    @Query("UPDATE TicketCategory tc SET tc.numberOfTickets = tc.numberOfTickets - 1 WHERE tc.eventId = :eventId AND tc.category = :category AND tc.numberOfTickets > 0")
    int decreaseTicketCount(@Param("eventId") Integer eventId, @Param("category") String category);

    @Query("SELECT CASE WHEN COUNT(tc) > 0 THEN TRUE ELSE FALSE END FROM TicketCategory tc WHERE tc.eventId = :eventId AND tc.category = :category AND tc.numberOfTickets > 0")
    boolean existsByEventIdAndCategoryAndAvailableTickets(@Param("eventId") Integer eventId, @Param("category") String category);

    @Query("SELECT tc.price FROM TicketCategory tc WHERE tc.eventId = :eventId AND tc.category = :category")
    Optional<Double> findPriceByEventIdAndCategory(@Param("eventId") Integer eventId, @Param("category") String category);

    List<TicketCategory> findByEventIdAndNumberOfTicketsGreaterThan(Integer eventId, int numberOfTickets);

    List<TicketCategory> findByEventIdAndPriceBetween(Integer eventId, double minPrice, double maxPrice);
}
