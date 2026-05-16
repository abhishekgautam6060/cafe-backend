package com.manager.cafe.controller;


import com.manager.cafe.config.CafeAccessService;
import com.manager.cafe.entity.Category;
import com.manager.cafe.entity.User;
import com.manager.cafe.repository.CategoryRepository;
import com.manager.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CafeAccessService cafeAccessService;

    // CREATE CATEGORY
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<?> createCategory(
            @RequestBody Category category,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow();

        User cafeOwner =
                cafeAccessService.getCafeOwner(currentUser);

        category.setUser(cafeOwner);

        return ResponseEntity.ok(
                categoryRepo.save(category)
        );
    }

    // GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<?> getCategories(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow();

        User cafeOwner =
                cafeAccessService.getCafeOwner(currentUser);

        return ResponseEntity.ok(
                categoryRepo.findByUser(cafeOwner)
        );
    }

    // UPDATE CATEGORY
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody Category updatedCategory,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow();

        User cafeOwner =
                cafeAccessService.getCafeOwner(currentUser);

        Category category = categoryRepo.findById(id)
                .orElseThrow();

        if (!category.getUser().getId().equals(cafeOwner.getId())) {
            return ResponseEntity.badRequest()
                    .body("Unauthorized");
        }

        category.setName(updatedCategory.getName());

        return ResponseEntity.ok(
                categoryRepo.save(category)
        );
    }

    // DELETE CATEGORY
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow();

        User cafeOwner =
                cafeAccessService.getCafeOwner(currentUser);

        Category category = categoryRepo.findById(id)
                .orElseThrow();

        if (!category.getUser().getId().equals(cafeOwner.getId())) {
            return ResponseEntity.badRequest()
                    .body("Unauthorized");
        }

        categoryRepo.delete(category);

        return ResponseEntity.ok(
                "Category deleted successfully"
        );
    }
}
