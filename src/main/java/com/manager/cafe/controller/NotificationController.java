package com.manager.cafe.controller;


import com.manager.cafe.config.CafeAccessService;
import com.manager.cafe.entity.Notification;
import com.manager.cafe.entity.User;
import com.manager.cafe.repository.NotificationRepository;
import com.manager.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CafeAccessService cafeAccessService;

    @GetMapping
    public ResponseEntity<?> getAll(Authentication auth){
        String email = auth.getName();
        User currentUser = userRepo.findByEmail(email).orElseThrow();

        User cafeOwner =
                cafeAccessService.getCafeOwner(currentUser);

        return ResponseEntity.ok(repo.findByUser(cafeOwner));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Notification request,
            Authentication auth
    ) {

        User currentUser =
                userRepo.findByEmail(auth.getName()).get();

        User cafeOwner =
                cafeAccessService.getCafeOwner(currentUser);

        request.setCreatedAt(LocalDateTime.now());
        request.setUser(cafeOwner);
        return ResponseEntity.ok(repo.save(request));
    }

}
