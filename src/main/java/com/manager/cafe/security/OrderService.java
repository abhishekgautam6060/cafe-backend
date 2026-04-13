package com.manager.cafe.security;

import com.manager.cafe.entity.Order;
import com.manager.cafe.entity.OrderItem;
import com.manager.cafe.entity.User;
import com.manager.cafe.repository.OrderItemRepository;
import com.manager.cafe.repository.OrderRepository;
import com.manager.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository itemRepo;

    @Autowired
    private UserRepository userRepository;


    public Order createOrder(Long tableNo, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (tableNo == null) {
            tableNo = 0L; // ✅ TAKEAWAY SUPPORT
        }

        Order order = new Order();

        order.setTableNo(tableNo);
        order.setUser(user);

        order.setStatus("ACTIVE"); // ✅ IMPORTANT
        order.setCreatedAt(LocalDateTime.now()); // 🔥 FIX
        order.setTotalAmount(0.0); // 🔥 FIX

        return orderRepo.save(order);
    }

    public List<Order> getOrdersByUser(String email) {
        return orderRepo.findByUserEmail(email);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order addItem(Long orderId, OrderItem item) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        item.setOrder(order); // 🔥 VERY IMPORTANT

        order.getItems().add(item);

        return orderRepo.save(order);
    }

    public Order removeItem(Long orderId, OrderItem item) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> items = order.getItems();

        for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext();) {
            OrderItem existing = iterator.next();

            if (existing.getItemName().equals(item.getItemName())) {

                if (existing.getQuantity() > 1) {
                    existing.setQuantity(existing.getQuantity() - 1); // ✅ decrease
                } else {
                    iterator.remove(); // ✅ remove item completely
                }

                break;
            }
        }

        return orderRepo.save(order);
    }

    public Order closeOrder(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus("BILLED");
        return orderRepo.save(order);
    }


    public Order generateBill(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("Cannot generate bill: No items added");
        }

        double total = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setTotalAmount(total);
        order.setStatus("BILLED"); // ✅ FIX
        order.setBilledAt(LocalDateTime.now());

        return orderRepo.save(order);
    }

    public Order collectPayment(Long orderId, String method) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PAID"); // ✅ FINAL STATE
        order.setPaymentMethod(method);
        order.setPaidAt(LocalDateTime.now());

        return orderRepo.save(order);
    }

    public List<Order> getTodayOrders (String email){
        return orderRepo.getTodayOrdersByEmail(email);
    }
}
