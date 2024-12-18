package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.dto.Role;
import edu.miu.cs425.kllbankingsolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "/user/login-page";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam Role role) {
        userService.addUser(username, password, role);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal, Authentication authentication) {
        String username = principal.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_CUSTOMER")) {
            model.addAttribute("customer", username);
            return "customer/customer-home-page"; // Redirect to customer page
        } else if (role.equals("ROLE_TELLER")) {
            model.addAttribute("teller", username);
            return "teller/teller-home-page"; // Redirect to teller page
        } else if (role.equals("ROLE_ADMIN")) {
            model.addAttribute("admin", username);
            return "admin/admin-home-page"; // Redirect to admin page
        }
        return "redirect:/login"; // Fallback in case of an unknown role
    }
}
