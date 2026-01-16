package com.tcs.authService.config;

import com.tcs.authService.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http
	        .csrf(csrf -> csrf.disable()) // This is essential for REST APIs
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/auth/register", "/auth/login", "/auth/users/email/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .build();
	}

    // Spring finds your User in the DB
    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return username -> {
            com.tcs.authService.bean.User user = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            //CUSTOM CHECK: Stop login if status is BLOCKED
            if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
                throw new DisabledException("Account is blocked. Please contact support.");
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities("USER")
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
