package com.chess.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final GameSocketHandler gameSocketHandler;
	
	public WebSocketConfig(GameSocketHandler gameSocketHandler) {
		this.gameSocketHandler = gameSocketHandler;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry
			.addHandler(gameSocketHandler, "/ws/play/{roomId}")
			.setAllowedOrigins("*");

	}

}
