package org.rares.eventservice.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QRCodeService {

    public String generateQRCode(int eventId, String fullName, String ticketCategory) {
        String data = eventId + "|" + fullName + "|" + ticketCategory;

        String qrCode = UUID.randomUUID().toString() + "-" + Math.abs(data.hashCode());

        if (qrCode == null || qrCode.isEmpty()) {
            throw new IllegalStateException("QR code generation failed.");
        }

        return qrCode;
    }
}

