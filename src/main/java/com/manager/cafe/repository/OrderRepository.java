package com.manager.cafe.repository;

import com.manager.cafe.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserEmail(String email);

    List<Order> findByUserOwner_Id(Long ownerId);
    List<Order> findByOwner_Id(Long ownerId);

    @Query(value = """
    SELECT o.* 
    FROM orders o
    JOIN users u ON o.user_id = u.id
    WHERE o.created_at >= CURDATE()
      AND o.created_at < CURDATE() + INTERVAL 1 DAY
      AND u.email = :email
""", nativeQuery = true)
    List<Order> getTodayOrdersByEmail(@Param("email") String email);

}
