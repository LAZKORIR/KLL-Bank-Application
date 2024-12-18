package edu.miu.cs425.kllbankingsolution.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/teller/**").hasRole("TELLER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/dashboard", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout").permitAll()
                );
        return http.build();
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails customer = User.builder()
                .username("customerUser")
                .password(passwordEncoder.encode("password")) // Correct use of PasswordEncoder
                .roles("CUSTOMER")
                .build();

        UserDetails teller = User.builder()
                .username("tellerUser")
                .password(passwordEncoder.encode("password")) // Correct password encoding
                .roles("TELLER")
                .build();

        UserDetails admin = User.builder()
                .username("adminUser")
                .password(passwordEncoder.encode("password")) // Correct password encoding
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(customer, teller, admin);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
