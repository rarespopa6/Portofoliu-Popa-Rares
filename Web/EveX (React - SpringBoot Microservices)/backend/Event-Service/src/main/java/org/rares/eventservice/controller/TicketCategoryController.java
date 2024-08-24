package org.rares.eventservice.controller;

import org.rares.eventservice.model.TicketCategory;
import org.rares.eventservice.service.TicketCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ticket-categories")
public class TicketCategoryController {

    @Autowired
    private TicketCategoryService ticketCategoryService;

    // Endpoint pentru a obține toate categoriile de bilete pentru un eveniment
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketCategory>> getTicketCategoriesByEventId(@PathVariable int eventId) {
        List<TicketCategory> ticketCategories = ticketCategoryService.getTicketCategoriesByEventId(eventId);
        return ResponseEntity.ok(ticketCategories);
    }

    @GetMapping("/event/{eventId}/category/{category}")
    public ResponseEntity<TicketCategory> getTicketCategory(@PathVariable int eventId, @PathVariable String category) {
        Optional<TicketCategory> ticketCategoryOpt = ticketCategoryService.getTicketCategory(eventId, category);
        return ticketCategoryOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/event/{eventId}/category/{category}/reduce")
    public ResponseEntity<Void> reduceNumberOfTickets(
            @PathVariable int eventId,
            @PathVariable String category,
            @RequestParam int quantity) {
        try {
            ticketCategoryService.reduceNumberOfTickets(eventId, category, quantity);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
