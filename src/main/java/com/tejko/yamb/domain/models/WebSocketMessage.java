package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.tejko.yamb.domain.enums.MessageType;

public class WebSocketMessage implements Message<byte[]> {

    private UUID senderId;
    private UUID receiverId;
    private byte[] payload;
    private MessageType type;
    private LocalDateTime timestamp; 
    private MessageHeaders headers;

    protected WebSocketMessage(byte[] payload, MessageType type, LocalDateTime timestamp, MessageHeaders headers) {
        this.payload = payload;
        this.type = type;
        this.timestamp = timestamp;
        this.headers = headers;
    }

    protected WebSocketMessage(UUID senderId, UUID receiverId, byte[] payload, MessageType type, LocalDateTime timestamp, MessageHeaders headers) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.payload = payload;
        this.type = type;
        this.timestamp = timestamp;
        this.headers = headers;
    }

    public static WebSocketMessage getInstance(UUID senderId, UUID receiverId, byte[] payload, MessageType type) {
        LocalDateTime timestamp = LocalDateTime.now();
        MessageHeaders headers = generateHeaders(senderId, receiverId, type, timestamp);
        return new WebSocketMessage(senderId, receiverId, payload, type, timestamp, headers);
    }

    public static WebSocketMessage getInstance(byte[] payload, MessageType type) {
        LocalDateTime timestamp = LocalDateTime.now();
        MessageHeaders headers = generateHeaders(null, null, type, timestamp);
        return new WebSocketMessage(payload, type, timestamp, headers);
    }

    private static MessageHeaders generateHeaders(UUID senderId, UUID receiverId, MessageType type, LocalDateTime timestamp) {
        Map<String, Object> headerMap = new HashMap<>();
        if (senderId != null) headerMap.put("senderId", senderId);
        if (receiverId != null) headerMap.put("receiverId", receiverId);
        headerMap.put("messageType", type);
        headerMap.put("timestamp", timestamp);
        return new MessageHeaders(headerMap);
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public MessageHeaders getHeaders() {
        return headers;
    }
    
}
