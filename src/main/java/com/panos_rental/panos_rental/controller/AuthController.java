package com.panos_rental.panos_rental.controller;

import com.panos_rental.panos_rental.DTO.LoginRequest;
import com.panos_rental.panos_rental.DTO.RegisterRequest;
import com.panos_rental.panos_rental.entity.Account;
import com.panos_rental.panos_rental.entity.Client;
import com.panos_rental.panos_rental.entity.Role;
import com.panos_rental.panos_rental.repository.AccountRepository;
import com.panos_rental.panos_rental.service.AccountService;
import com.panos_rental.panos_rental.service.ClientService;
import com.panos_rental.panos_rental.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );


        String role = authentication.getAuthorities().iterator().next().getAuthority();


        String token = jwtUtil.generateToken(authentication.getName(), role);

        System.out.println("Generated JWT: " + token);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //na to allaksw se paragwgi
        cookie.setPath("/");
        response.addCookie(cookie);

        return "Login successful";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Δημιουργία ενός cookie με το ίδιο όνομα ("jwt") για overwrite
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // kai afto prepei na to allaksw opws to panw
        cookie.setPath("/");
        cookie.setMaxAge(0);


        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }


    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserRole(@CookieValue("jwt") String token) {
        try {
            Claims claims = jwtUtil.parseToken(token);
            String role = claims.get("role", String.class);
            String username = claims.getSubject();

            return ResponseEntity.ok(Map.of("role", role, "username", username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists.");
        }

        // idia logiki me thn nea enoikiasi apo client
        Client client = clientService.findByPhoneOrIdCard(request.getTel(), request.getIdCard());
        if (client == null) {
            client = new Client();
            client.setFirstName(request.getFirstName());
            client.setLastName(request.getLastName());
            client.setPhone(request.getTel());
            client.setIdCard(request.getIdCard());
            client = clientService.save(client);
        }


        Account user = new Account();
        user.setUsername(request.getUsername());
        user.setPassword((request.getPassword()));
        user.setRole(Role.CLIENT);
        user.setClient(client);
        accountRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/userinfo/details")
    public ResponseEntity<?> getUserDetails(@CookieValue("jwt") String token) {
        try {

            Claims claims = jwtUtil.parseToken(token);

            String username = claims.getSubject();
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username not found in token");
            }


            Optional<Account> accountOptional = accountService.findByUsername(username);
            if (accountOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for username: " + username);
            }

            Account account = accountOptional.get();
            if (account.getClient() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No associated client found for account: " + username);
            }


            Optional<Client> clientOptional = clientService.findById(account.getClient().getId());
            if (clientOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found for account: " + username);
            }

            Client client = clientOptional.get();


            Map<String, Object> response = Map.of(
                    "username", account.getUsername(),
                    "points", account.getPoints(),
                    "firstName", client.getFirstName(),
                    "lastName", client.getLastName(),
                    "phone", client.getPhone(),
                    "clientId", client.getId(),
                    "idCard", client.getIdCard()
            );

            return ResponseEntity.ok(response);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user details: " + e.getMessage());
        }
    }


    @PostMapping("/update-points")
    public ResponseEntity<?> updatePoints(
            @CookieValue("jwt") String token,
            @RequestBody Map<String, Integer> payload) {
        try {
            Claims claims = jwtUtil.parseToken(token);
            String username = claims.getSubject();

            int pointsChange = payload.getOrDefault("points", 0);

            if (pointsChange < 0) {
                return ResponseEntity.badRequest().body("Points change cannot be negative.");
            }


            accountService.updatePoints(username, pointsChange);

            return ResponseEntity.ok("{\"message\": \"Points updated successfully\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating points: " + e.getMessage());
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            Optional<Account> accountOptional = accountService.findById(id);
            if (accountOptional.isPresent()) {
                accountService.deleteAccountById(id);
                return ResponseEntity.ok("Account deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting account: " + e.getMessage());
        }
    }

}


