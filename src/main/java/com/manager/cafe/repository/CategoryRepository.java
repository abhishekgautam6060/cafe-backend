package com.manager.cafe.repository;

import com.manager.cafe.entity.Category;
import com.manager.cafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    List<Category> findByUser(User user);

    boolean existsByName(String name);
}