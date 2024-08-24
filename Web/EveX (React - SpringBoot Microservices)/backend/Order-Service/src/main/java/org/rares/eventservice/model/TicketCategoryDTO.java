package org.rares.eventservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCategoryDTO {
    private int eventId;
    private String category;
    private int numberOfTickets;
}
