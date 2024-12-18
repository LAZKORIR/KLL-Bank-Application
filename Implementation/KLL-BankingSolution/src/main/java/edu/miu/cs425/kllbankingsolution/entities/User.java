package edu.miu.cs425.kllbankingsolution.entities;

import edu.miu.cs425.kllbankingsolution.dto.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // Role added for role-based login

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
