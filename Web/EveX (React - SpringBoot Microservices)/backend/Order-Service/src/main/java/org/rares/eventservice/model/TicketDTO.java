package org.rares.eventservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private int eventId;
    private String fullName;
    private String ticketCategory;
    private String qrCode;
}

