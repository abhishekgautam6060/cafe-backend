package com.manager.cafe.repository;

import com.manager.cafe.entity.MenuItem;
import com.manager.cafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByUser(User user);
}
