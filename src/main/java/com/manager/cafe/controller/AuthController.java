package com.manager.cafe.controller;

import com.manager.cafe.config.JwtUtil;
import com.manager.cafe.dto.AuthResponse;
import com.manager.cafe.dto.LoginRequest;
import com.manager.cafe.dto.SignupRequest;
import com.manager.cafe.entity.User;
import com.manager.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        return repo.save(user);
    }

    @GetMapping("/me")
    public User getProfile(Authentication auth) {
        return repo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Login
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        AuthResponse auth= new AuthResponse();
        auth.setToken(token);
        auth.setName(user.getName());
        auth.setEmail(user.getEmail());
        return auth;
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
