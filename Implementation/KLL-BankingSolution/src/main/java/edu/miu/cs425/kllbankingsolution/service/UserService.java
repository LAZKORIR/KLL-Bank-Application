package edu.miu.cs425.kllbankingsolution.service;

import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Add a new user
    public void addUser(User user) {
        userRepository.save(user);
    }

    // Fetch user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
