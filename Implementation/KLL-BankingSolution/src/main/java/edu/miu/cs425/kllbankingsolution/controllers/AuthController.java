package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.dto.LoginForm;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.RoleRepository;
import edu.miu.cs425.kllbankingsolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               Model model) {
        // Check if the username already exists
        System.out.println("******************");
        System.out.println(userService.findByUsername(username) );
        if (userService.findByUsername(username) != null) {
            // Add an error message to the model
            model.addAttribute("errorMessage", "Username '" + username + "' is already taken. Please choose a different one.");
            return "user/register"; // Return to the registration page with the error message
        }

        // Create a new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // Encode the password
        Optional.of(user)
                .ifPresent(u -> u.setPassword(passwordEncoder.encode(u.getPassword())));

        // Assign the role
        roleRepository.findByName("ROLE_" + role)
                .ifPresentOrElse(
                        userRole -> user.getRoles().add(userRole),
                        () -> {
                            throw new IllegalArgumentException("Invalid role: " + role);
                        }
                );

        // Save the user
        userService.addUser(user);

        return "redirect:/login";
    }


    @GetMapping("/home")
    public String home() {
        // Retrieve the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Log authentication and roles for debugging
        System.out.println("Authentication: " + authentication);
        System.out.println("Authorities: " + authentication.getAuthorities());

        // Map the authenticated user's role to a redirect path
        return Optional.of(authentication)
                .map(Authentication::getAuthorities)
                .flatMap(authorities -> authorities.stream()
                        .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                        .findFirst())
                .map(GrantedAuthority::getAuthority)
                .map(role -> switch (role) {
                    case "ROLE_CUSTOMER" -> "redirect:/customer/dashboard";
                    case "ROLE_TELLER" -> "redirect:/teller/dashboard";
                    case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
                    default -> throw new IllegalStateException("Unknown role: " + role);
                })
                .orElse("redirect:/error?message=User%20has%20no%20roles%20assigned");
    }

}
