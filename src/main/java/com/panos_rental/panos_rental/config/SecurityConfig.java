package com.panos_rental.panos_rental.config;

import com.panos_rental.panos_rental.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Απενεργοποίηση CSRF
                .authorizeHttpRequests(authz -> authz

                        //edw ta / poy einai gia to login kai sxetika me to auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/login").permitAll() // free prosvasi login
                        .requestMatchers("/auth/userinfo").permitAll()
                        .requestMatchers("/auth/userinfo/details").permitAll()/* stoixeia gia role me jwt (note na dw an to valw
                        sto teliko*/

                        //edw ta endpoints mono gia ton admin
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/clients/all").hasAuthority("ROLE_ADMIN")




                        .anyRequest().permitAll() // ta ypoloipa endpoints xwirs authenticate
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Προσθήκη φίλτρου
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Χρησιμοποιούμε τον NoOpPasswordEncoder
    }
}
