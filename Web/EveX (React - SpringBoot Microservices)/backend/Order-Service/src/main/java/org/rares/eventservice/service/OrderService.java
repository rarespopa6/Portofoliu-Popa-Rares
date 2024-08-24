package org.rares.eventservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.rares.eventservice.feign.EventServiceClient;
import org.rares.eventservice.model.*;
import org.rares.eventservice.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Component
public class OrderService {

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private EventServiceClient eventServiceClient;

    @Autowired
    private QRCodeService qrCodeService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepository.findByUserEmail(email);
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        for (Ticket ticket : order.getTickets()) {
            TicketCategoryDTO ticketCategory = eventServiceClient.getTicketCategory(ticket.getEventId(), ticket.getTicketCategory());

            if (ticketCategory.getNumberOfTickets() < 1) {
                throw new IllegalStateException("Not enough tickets available.");
            }
            eventServiceClient.reduceNumberOfTickets(ticket.getEventId(), ticket.getTicketCategory(), 1);

            String qrCode = qrCodeService.generateQRCode(ticket.getEventId(), ticket.getFullName(), ticket.getTicketCategory());
            ticket.setQrCode(qrCode);

            ticket.setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    public List<Order> getOrdersByEventId(int eventId) {
        return orderRepository.findByEventId(eventId);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}
