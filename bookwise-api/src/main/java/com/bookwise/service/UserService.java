package com.bookwise.service;

import com.bookwise.domain.User;
import com.bookwise.domain.UserRole;
import com.bookwise.exception.ConflictException;
import com.bookwise.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email already in use: " + email);
        }
        User user = new User(email, passwordEncoder.encode(rawPassword), UserRole.USER);
        return userRepository.save(user);
    }
}
