package org.rares.eventservice.repository;

import org.rares.eventservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    List<Order> findByUserEmail(String email);
    @Query("SELECT o FROM Order o JOIN o.tickets t WHERE t.eventId = :eventId")
    List<Order> findByEventId(@Param("eventId") int eventId);
}
