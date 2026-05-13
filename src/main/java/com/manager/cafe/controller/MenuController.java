package com.manager.cafe.controller;


import com.manager.cafe.config.CafeAccessService;
import com.manager.cafe.entity.MenuItem;
import com.manager.cafe.entity.User;
import com.manager.cafe.repository.MenuItemRepository;
import com.manager.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuController {


    @Autowired
    private MenuItemRepository menuRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CafeAccessService cafeAccessService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<?> addMenu(
            @RequestBody MenuItem item,
            Authentication authentication
    ) {
        String email = authentication.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow();
        item.setUser(user);
        return ResponseEntity.ok(
                menuRepo.save(item)
        );
    }

    @GetMapping
    public ResponseEntity<?> getMenu(
            Authentication authentication
    ) {
        String email = authentication.getName();
        User currentUser = userRepo.findByEmail(email)
                .orElseThrow();

        User cafeOwner =
                cafeAccessService.getCafeOwner(currentUser);
        return ResponseEntity.ok(
                menuRepo.findByUser(cafeOwner)
        );
    }
}
