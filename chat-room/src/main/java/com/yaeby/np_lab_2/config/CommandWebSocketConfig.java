package com.yaeby.np_lab_2.config;

import com.yaeby.np_lab_2.handler.CommandWebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
@EnableWebSocket
public class CommandWebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CommandWebSocketHandler(), "/cmd")
                .setAllowedOrigins("*");
    }
}
