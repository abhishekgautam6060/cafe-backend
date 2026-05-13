package com.manager.cafe.repository;

import com.manager.cafe.entity.MenuItem;
import com.manager.cafe.entity.Notification;
import com.manager.cafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);
}
