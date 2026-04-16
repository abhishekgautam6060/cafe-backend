package com.manager.cafe.controller;

import com.manager.cafe.entity.Order;
import com.manager.cafe.entity.OrderItem;
import com.manager.cafe.repository.OrderRepository;
import com.manager.cafe.security.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @Autowired
    private OrderRepository orderRepo;

    @PostMapping
    public Order createOrder(
            @RequestParam(required = false) Long tableNo,
            Authentication auth) {
        String email = auth.getName(); // ✅ logged-in user
        System.out.println("Email : " + email);
        System.out.println("table no " + tableNo);

        return service.createOrder(tableNo, email);
    }

    @GetMapping
    public List<Order> getOrders() {
        return service.getAllOrders();
    }

    @GetMapping
    public List<Order> getOrders(Authentication auth) {
        String email = auth.getName();
        return service.getOrdersByUser(email);
    }

    @GetMapping("/today")
    public List<Order> getTodayOrders(Authentication auth) {
        String email = auth.getName();
        return service.getTodayOrders(email);
    }

    @PostMapping("/{orderId}/items")
    public Order addItem(Authentication auth,  @PathVariable Long orderId, @RequestBody OrderItem item) {
        String email = auth.getName();

        return service.addItem(orderId, item);
    }

    @PostMapping("/{orderId}/remove/items")
    public Order removeItem(Authentication auth,  @PathVariable Long orderId, @RequestBody OrderItem item) {
        String email = auth.getName();

        return service.removeItem(orderId, item);
    }

    @PutMapping("/{orderId}/close")
    public Order closeOrder(@PathVariable Long orderId) {
        return service.closeOrder(orderId);
    }

    @PutMapping("/{orderId}/add-items")
    public Order addMoreItems(@PathVariable Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("ACTIVE"); // ✅ correct way

        return orderRepo.save(order);
    }

    @PutMapping("/{orderId}/bill")
    public Order generateBill(@PathVariable Long orderId) {
        return service.generateBill(orderId);
    }

    @PutMapping("/{orderId}/pay")
    public Order pay(
            @PathVariable Long orderId,
            @RequestParam String method
    ) {
        return service.collectPayment(orderId, method);
    }
}
