package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.dto.LoginForm;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.RoleRepository;
import edu.miu.cs425.kllbankingsolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {
        model.addAttribute("loginForm", new LoginForm());

        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        return "user/login-page";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role) {
        // Find the role and add it to the user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Optional.of(user)
                .ifPresent(u -> u.setPassword(passwordEncoder.encode(u.getPassword())));

        roleRepository.findByName("ROLE_" + role)
                .ifPresentOrElse(
                        userRole -> user.getRoles().add(userRole),
                        () -> {
                            throw new IllegalArgumentException("Invalid role: " + role);
                        }
                );
        userService.addUser(user);
        return "redirect:/login";
    }

    //@GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal, Authentication authentication) {
        String username = principal.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        switch (role) {
            case "CUSTOMER" -> {
                model.addAttribute("customer", username);
                return "customer/customer-home-page"; // Redirect to customer page
            }
            case "TELLER" -> {
                model.addAttribute("teller", username);
                return "teller/teller-home-page"; // Redirect to teller page
            }
            case "ADMIN" -> {
                model.addAttribute("admin", username);
                return "admin/admin-home-page"; // Redirect to admin page
            }
        }
        return "redirect:/login"; // Fallback in case of an unknown role
    }
    @GetMapping("/dashboard")
    public String home(Authentication authentication) {
        // Log Authentication details and roles
        System.out.println("Authentication: " + authentication);
        authentication.getAuthorities().forEach(auth ->
                System.out.println("Authority: " + auth.getAuthority())
        );

        return Optional.of(authentication)
                .map(Authentication::getAuthorities)
                .flatMap(authorities -> authorities.stream()
                        .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                        .findFirst())
                .map(GrantedAuthority::getAuthority)
                .map(role -> switch (role) {
                    case "ROLE_CUSTOMER" -> "redirect:/customer/dashboard";
                    case "ROLE_TELLER" -> "redirect:/teller";
                    case "ROLE_ADMIN" -> "redirect:/admin";
                    default -> throw new IllegalStateException("Unknown role: " + role);
                })
                .orElseThrow(() -> new IllegalStateException("User has no roles assigned"));
    }

}
