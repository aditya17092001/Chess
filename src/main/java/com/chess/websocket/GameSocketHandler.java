package com.chess.websocket;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class GameSocketHandler extends TextWebSocketHandler {
	private final ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("✅ Client connected: " + session.getId());
        String roomId = getRoomId(session);
        String playerId = session.getId();

        rooms.putIfAbsent(roomId, new ConcurrentHashMap<>());
        rooms.get(roomId).put(playerId, session);

        System.out.println("Player connected: " + playerId + " to room " + roomId);
    }
	
	private String getRoomId(WebSocketSession session) {
        return session.getUri().getPath().split("/ws/play/")[1];
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomId(session);
        var players = rooms.get(roomId);
        System.out.println("Message: "+message.getPayload().toString());

        for (WebSocketSession s : players.values()) {
            if (s.isOpen()) {
                s.sendMessage(message);
            }
        }
    }
	
	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomId = getRoomId(session);
        rooms.getOrDefault(roomId, new ConcurrentHashMap<>()).remove(session.getId());
    }
}
