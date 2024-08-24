package org.rares.eventservice.repository;

import org.rares.eventservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByEventId(int eventId);
}
