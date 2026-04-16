package com.manager.cafe.controller;

import com.manager.cafe.config.JwtUtil;
import com.manager.cafe.dto.AuthResponse;
import com.manager.cafe.dto.LoginRequest;
import com.manager.cafe.dto.SignupRequest;
import com.manager.cafe.entity.User;
import com.manager.cafe.repository.UserRepository;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Signup
    @PostMapping("/signup")
    public User signup(@RequestBody SignupRequest req) {

        User user = new User();
        user.setName(req.fullName);
        user.setEmail(req.email);
        user.setPhone(req.phone);
        user.setPassword(encoder.encode(req.password));
        user.setRole(User.Role.ADMIN);

        return repo.save(user);
    }

    @GetMapping("/me")
    public User getProfile(Authentication auth) {
        return repo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        User user = repo.findByEmail(req.getEmail()).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(401)
                    .body("User not found ❌");
        }

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(401)
                    .body("Invalid credentials ❌");
        }

        String token = jwtUtil.generateToken(user.getEmail(),user.getRole().name());

        AuthResponse auth = new AuthResponse();
        auth.setToken(token);
        auth.setRole(user.getRole().name());
        auth.setName(user.getName());
        auth.setEmail(user.getEmail());


        return ResponseEntity.ok(auth);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-staff")
    public User createStaff(Authentication auth, @RequestBody SignupRequest req) {


        String email = auth.getName();
        User admin = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        User user = new User();
        user.setName(req.fullName);
        user.setEmail(req.email);
        user.setPhone(req.phone);
        user.setPassword(encoder.encode(req.password));

        user.setRole(User.Role.valueOf(req.role)); // 🔥 dynamic role
        user.setOwner(admin); // 🔥 LINK TO ADMIN

        return repo.save(user);
    }

    @PutMapping("/me")
    public User updateProfile(@RequestBody User updated, Authentication auth) {

        User user = repo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updated.getName());
        user.setEmail(updated.getEmail());
        user.setPhone(updated.getPhone());

        return repo.save(user);
    }
}
