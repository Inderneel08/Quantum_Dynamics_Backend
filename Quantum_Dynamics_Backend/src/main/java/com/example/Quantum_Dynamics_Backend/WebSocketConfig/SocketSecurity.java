package com.example.Quantum_Dynamics_Backend.WebSocketConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class SocketSecurity {

    @Bean
    public MessageMatcherDelegatingAuthorizationManager.Builder messageMatcherDelegatingAuthorizationManagerBuilder() {
        return MessageMatcherDelegatingAuthorizationManager.builder();
    }

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages) {

        messages.simpDestMatchers("/ws/**").permitAll();

        return (messages.build());
    }
}
