package org.example.freelanceplatform.graphql;

import org.example.freelanceplatform.entities.AuthPayload;
import org.example.freelanceplatform.repositories.AdminRepository;
import org.example.freelanceplatform.repositories.FreelancerRepository;
import org.example.freelanceplatform.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.example.freelanceplatform.entities.Admin;
import org.example.freelanceplatform.services.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AdminGraphql {
    private final AuthService authService;

    public AdminGraphql(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public AuthPayload register(@Argument String username, @Argument String password) {
        Admin admin = authService.registerAdmin(username, password);
        return new AuthPayload(generateToken(admin), admin);
    }

    @MutationMapping
    public AuthPayload login(@Argument String username, @Argument String password) {
        if (authService.authenticate(username, password)) {
            Admin admin = authService.getAdminByUsername(username);
            return new AuthPayload(generateToken(admin), admin);
        }
        throw new RuntimeException("Invalid credentials");
    }

    private String generateToken(Admin admin) {
        // Implement JWT token generation here
        return "sample-token"; // Replace with real JWT
    }


}
