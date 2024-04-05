package com.example.Quantum_Dynamics_Backend.WebSocketController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class WebSocketController {

    @MessageMapping("/loggedin")
    @SendTo("/topic/loggedin")
    public ResponseEntity<?> sendLoginMessage(@Payload String message) {
        return (ResponseEntity.ok().body(message + "logged in"));
    }

    // simpMessagingTemplate.convertAndSend("/topic/loggedin",
    // userDetails.getUsername() + " joined the chat.");

    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnect")
    public ResponseEntity<?> handleDisconnectEntity(@RequestBody Map<String, String> request) {
        return (ResponseEntity.ok().body(request.get("username") + " logged out"));
    }

}
