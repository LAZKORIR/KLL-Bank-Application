package edu.miu.cs425.kllbankingsolution.config;

import edu.miu.cs425.kllbankingsolution.entities.Role;
import edu.miu.cs425.kllbankingsolution.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    // Constructor injection
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Load or Create Roles
        Role customerRole = getOrCreateRole("ROLE_CUSTOMER");
        Role tellerRole = getOrCreateRole("ROLE_TELLER");
        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        System.out.println("******************");
        System.out.println(customerRole);
    }
    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
