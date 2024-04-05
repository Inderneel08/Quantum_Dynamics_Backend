package com.example.Quantum_Dynamics_Backend.Jwt;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Quantum_Dynamics_Backend.DAO.User;
import com.example.Quantum_Dynamics_Backend.Repository.UserRepository;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class JwtController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private User users;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> jwtRequest) {
        System.out.println(jwtRequest);

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.get("username"));

            System.out.println(userDetails);

            return ResponseEntity.badRequest().body("User already exists...");
        } catch (UsernameNotFoundException e) {
            users.setName(jwtRequest.get("name"));
            users.setUsername(jwtRequest.get("username"));
            users.setPassword(passwordEncoder.encode(jwtRequest.get("password")));

            System.out.println(users);

            userRepository.save(users);

            return (ResponseEntity.ok().body("User created..."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> jWtrequest) {

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(jWtrequest.get("username"));

            System.out.println(userDetails);
            System.out.println(userDetails.getUsername());
            System.out.println(userDetails.getPassword());

            if (!BCrypt.checkpw(jWtrequest.get("password"), userDetails.getPassword())) {

                return (ResponseEntity.status(401).body("Incorrect Password"));
            }

            String generatedToken = jwtTokenProvider.generateToken(userDetails);

            System.out.println("JWT token " + generatedToken);

            return ResponseEntity.ok(new JwtToken(jWtrequest.get("username"), generatedToken));
        } catch (UsernameNotFoundException e) {
            return (ResponseEntity.status(401).body("Username is not registered"));
        }
    }

    @PostMapping("/broadcast")
    public void broadcastMessage(@RequestBody String message) {
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
    }

    // @PostMapping("/adminLogin")
    // public void generateTokenForAdmin(@RequestBody Map<String, String>
    // jWtrequest)
    // {
    // System.out.println(jWtrequest);

    // System.out.println(jWtrequest.get("email"));
    // }

    @PostMapping("/validate")
    public ResponseEntity<?> validateTokenIdentity() {
        return (ResponseEntity.ok("VALIDATED"));
    }

}
