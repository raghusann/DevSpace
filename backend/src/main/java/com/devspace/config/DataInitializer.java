package com.devspace.config;

import com.devspace.auth.entity.Role;
import com.devspace.auth.entity.User;
import com.devspace.auth.repository.RoleRepository;
import com.devspace.auth.repository.UserRepository;
import com.devspace.common.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        for (RoleType roleType : RoleType.values()) {
            roleRepository.findByName(roleType).orElseGet(() -> {
                Role role = new Role(roleType);
                return roleRepository.save(role);
            });
        }

        if (!userRepository.existsByEmail("admin@devspace.com")) {
            User admin = new User();
            admin.setEmail("admin@devspace.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmailVerified(true);
            admin.setEnabled(true);

            Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseThrow();
            Role developerRole = roleRepository.findByName(RoleType.DEVELOPER)
                    .orElseThrow();
            admin.setRoles(Set.of(adminRole, developerRole));

            userRepository.save(admin);
        }
    }
}
