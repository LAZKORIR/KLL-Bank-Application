//package edu.miu.cs425.kllbankingsolution.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//@Bean
//public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//    UserDetails customer = User.builder()
//            .username("customerUser")
//            .password(passwordEncoder.encode("password")) // Correct use of PasswordEncoder
//            .roles("CUSTOMER")
//            .build();
//
//    UserDetails teller = User.builder()
//            .username("tellerUser")
//            .password(passwordEncoder.encode("password")) // Correct password encoding
//            .roles("TELLER")
//            .build();
//
//    UserDetails admin = User.builder()
//            .username("adminUser")
//            .password(passwordEncoder.encode("password")) // Correct password encoding
//            .roles("ADMIN")
//            .build();
//
//    return new InMemoryUserDetailsManager(customer, teller, admin);
//}
