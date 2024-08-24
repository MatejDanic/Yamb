package com.tejko.yamb.interfaces.services;

import java.security.Principal;

import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.tejko.yamb.api.payload.WebSocketMessage;

public interface WebSocketService {

    public void publicMessage(WebSocketMessage message, Principal principal);

    public void privateMessage(WebSocketMessage message, Principal principal);

    public void handleSessionConnected(SessionConnectEvent event);

    public void handleSessionDisconnect(SessionDisconnectEvent event);

    public void handleSessionSubscribeEvent(SessionSubscribeEvent event);
    
}
