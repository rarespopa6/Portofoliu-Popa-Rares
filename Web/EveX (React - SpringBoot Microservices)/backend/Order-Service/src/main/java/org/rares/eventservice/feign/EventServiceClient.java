package org.rares.eventservice.feign;

import org.rares.eventservice.model.TicketCategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "event-service")
public interface EventServiceClient {

    @GetMapping("/api/ticket-categories/event/{eventId}/category/{category}")
    TicketCategoryDTO getTicketCategory(@PathVariable("eventId") int eventId, @PathVariable("category") String category);

    @PostMapping("/api/ticket-categories/event/{eventId}/category/{category}/reduce")
    void reduceNumberOfTickets(@PathVariable("eventId") int eventId,
                               @PathVariable("category") String category,
                               @RequestParam("quantity") int quantity);

}
