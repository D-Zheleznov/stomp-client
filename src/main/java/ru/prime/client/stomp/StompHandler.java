package ru.prime.client.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import ru.prime.client.model.ConfigurationParams;

@Slf4j
public class StompHandler extends StompSessionHandlerAdapter {

    private final ConfigurationParams configurationParams;

    public StompHandler(ConfigurationParams configurationParams) {
        this.configurationParams = configurationParams;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session opened: " + session.getSessionId());
        session.subscribe(configurationParams.getTopicUrl(), this);
        log.info("Subscribed to {}", configurationParams.getTopicUrl());
        session.subscribe(configurationParams.getSubscribeUrl(), this);
        log.info("Subscribed to {}", configurationParams.getSubscribeUrl());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Exception: ", exception);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Received message: {}", payload);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Exception: ", exception);
    }
}