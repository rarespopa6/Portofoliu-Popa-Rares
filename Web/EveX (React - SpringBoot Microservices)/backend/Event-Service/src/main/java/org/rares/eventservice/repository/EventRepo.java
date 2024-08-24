package org.rares.eventservice.repository;

import org.rares.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Integer> {
    List<Event> findEventsByTitleContainingIgnoreCase(String title);
    List<Event> findEventsByLocationContainingIgnoreCase(String location);
    Integer countByLocation(String location);
    Integer countByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Event> findEventsByEventDateBefore(LocalDateTime endDate);
    List<Event> findEventsByOrganizerId(Integer organizerId);
}
