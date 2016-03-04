package com.wk.cms.websocket;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

//@Configuration
//@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
	public class MyHander extends TextWebSocketHandler {

		private AtomicInteger count = new AtomicInteger(0);
		@Override
		protected void handleTextMessage(WebSocketSession session,
				TextMessage message) throws Exception {
			log.debug(message.getPayload()+"~~~~~");
			log.debug("count::"+count);
			if(count.incrementAndGet()>5){
				throw new Exception("cannot greater than 5~~");
			}
		}
		
		
		@Override
		public void afterConnectionEstablished(WebSocketSession session)
				throws Exception {
			
			session.sendMessage(new TextMessage("aaaa"));
			log.debug("session.getTextMessageSizeLimit()::"+session.getTextMessageSizeLimit());
		}
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(getHandler(),"/h").withSockJS().setClientLibraryUrl("http://cdn.jsdelivr.net/sockjs/1.0.3/sockjs.min.js");
	}
	
	@Bean
	public WebSocketHandler getHandler() {
		return new MyHander();
	}
	
	@Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(800);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
	
}
