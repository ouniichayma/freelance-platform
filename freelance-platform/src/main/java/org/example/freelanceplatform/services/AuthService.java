package org.example.freelanceplatform.services;

import org.example.freelanceplatform.entities.Admin;
import org.example.freelanceplatform.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthService(AdminRepository adminRepository, BCryptPasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    public boolean authenticate(String username, String password) {
        return adminRepository.findByUsername(username)
                .map(admin -> encoder.matches(password, admin.getPassword()))
                .orElse(false);
    }

    public Admin registerAdmin(String username, String rawPassword) {
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(encoder.encode(rawPassword));
        return adminRepository.save(admin);
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}