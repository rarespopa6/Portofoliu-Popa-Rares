package org.rares.eventservice.service;

import org.rares.eventservice.model.TicketCategory;
import org.rares.eventservice.repository.TicketCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketCategoryService {

    @Autowired
    private TicketCategoryRepo ticketCategoryRepo;

    public List<TicketCategory> getTicketCategoriesByEventId(int eventId) {
        return ticketCategoryRepo.findByEventId(eventId);
    }

    public Optional<TicketCategory> getTicketCategory(int eventId, String category) {
        return ticketCategoryRepo.findByEventIdAndCategory(eventId, category);
    }

    public TicketCategory updateTicketCategory(TicketCategory ticketCategory) {
        return ticketCategoryRepo.save(ticketCategory);
    }

    public void reduceNumberOfTickets(int eventId, String category, int quantity) {
        Optional<TicketCategory> ticketCategoryOpt = ticketCategoryRepo.findByEventIdAndCategory(eventId, category);
        if (ticketCategoryOpt.isPresent()) {
            TicketCategory ticketCategory = ticketCategoryOpt.get();
            int updatedNumberOfTickets = ticketCategory.getNumberOfTickets() - quantity;
            if (updatedNumberOfTickets >= 0) {
                ticketCategory.setNumberOfTickets(updatedNumberOfTickets);
                ticketCategoryRepo.save(ticketCategory);
            } else {
                throw new IllegalStateException("Not enough tickets available.");
            }
        } else {
            throw new IllegalStateException("Ticket category not found.");
        }
    }
}
