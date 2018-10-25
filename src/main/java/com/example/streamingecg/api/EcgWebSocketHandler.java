package com.example.streamingecg.api;

import com.example.streamingecg.domain.EcgUnit;
import com.example.streamingecg.reactive.EcgReactiveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class EcgWebSocketHandler implements WebSocketHandler {

    private final EcgReactiveService ecgService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(ecgService
                .createInfiniteEcgStream()
                .map(this::toJson)
                .map(session::textMessage)
        );
    }

    private String toJson(EcgUnit i) {
        try {
            return objectMapper.writeValueAsString(i);
        } catch (JsonProcessingException e) {
            return "ERROR";
        }
    }

}
