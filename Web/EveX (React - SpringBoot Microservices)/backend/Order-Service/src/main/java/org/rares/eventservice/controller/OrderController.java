package org.rares.eventservice.controller;

import org.rares.eventservice.model.Order;
import org.rares.eventservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // Endpoint pentru a obține toate comenzile (doar pentru admin)
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/event/view/{eventId}")
    public ResponseEntity<List<Order>> getOrdersByEvent(@PathVariable int eventId) {
        List<Order> orders = orderService.getOrdersByEventId(eventId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable String email) {
        List<Order> orders = orderService.getOrdersByUserEmail(email);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


}
