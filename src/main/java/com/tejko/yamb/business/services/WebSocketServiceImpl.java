package com.tejko.yamb.business.services;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.business.interfaces.WebSocketService;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.enums.PlayerStatus;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.WebSocketMessage;
import com.tejko.yamb.util.ActivePlayerDirectory;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<String, String> subscriptionDestinations = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketServiceImpl(ObjectMapper objectMapper, SimpMessagingTemplate simpMessagingTemplate) {
        this.objectMapper = objectMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void publicMessage(WebSocketMessage message, Principal principal) {
        UUID senderExternalId = UUID.fromString(principal.getName());
        message.setSenderId(senderExternalId);
        simpMessagingTemplate.send("/topic/public", message);
    }

    @Override
    public void privateMessage(WebSocketMessage message, Principal principal) {
        UUID senderExternalId = UUID.fromString(principal.getName());
        message.setSenderId(senderExternalId);
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getReceiverId()), "/player/private", message, message.getHeaders());
    }

    @Override
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Player player = (Player) accessor.getUser();
        ActivePlayerDirectory.setPlayerStatus(UUID.fromString(player.getName()), PlayerStatus.ONLINE);
        System.out.println(player.getUsername() + " connected.");
    }

    @Override
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Player player = (Player) accessor.getUser();
        ActivePlayerDirectory.setPlayerStatus(UUID.fromString(player.getName()), PlayerStatus.OFFLINE);
        System.out.println(player.getUsername() + " disconnected.");
    }

    @Override
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Player player = (Player) accessor.getUser();
        String destination = accessor.getDestination();
        String subscriptionId = accessor.getSubscriptionId();
    
        if (subscriptionId != null && destination != null) {
            subscriptionDestinations.put(subscriptionId, destination);
        }
    
        System.out.println(player.getUsername() + " has subscribed to " + destination);
    }
    
    @Override
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Player player = (Player) accessor.getUser();
        String subscriptionId = accessor.getSubscriptionId();
    
        String destination = subscriptionDestinations.get(subscriptionId);
        if (destination != null) {
            subscriptionDestinations.remove(subscriptionId);
            System.out.println(player.getUsername() + " has unsubscribed from " + destination);
        } else {
            System.out.println(player.getUsername() + " has unsubscribed, but the destination is unknown.");
        }
    }

    @Override
    public void convertAndSend(String destination, Object content, MessageType type) {
        WebSocketMessage message = WebSocketMessage.getInstance(generatePayload(content), type);
        simpMessagingTemplate.convertAndSend(destination, message, message.getHeaders());
    }

    private byte[] generatePayload(Object content) {
        if (content == null) {
            System.err.println("Warning: Content is null in WebSocketMessage");
            return new byte[0];
        }    
        try {
            return objectMapper.writeValueAsBytes(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
