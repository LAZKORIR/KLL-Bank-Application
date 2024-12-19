package edu.miu.cs425.kllbankingsolution.config;

import edu.miu.cs425.kllbankingsolution.entities.Role;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.RoleRepository;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // Constructor injection
    public DataLoader(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Load or Create Roles
        Role customerRole = getOrCreateRole("ROLE_CUSTOMER");
        Role tellerRole = getOrCreateRole("ROLE_TELLER");
        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        System.out.println("******************");
        System.out.println(customerRole);

        User user = new User();
        user.setUsername("Lazarus");
        user.setPassword(passwordEncoder.encode("admin123"));
        user.addRole(adminRole);

        //userRepository.save(user);
    }
    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
