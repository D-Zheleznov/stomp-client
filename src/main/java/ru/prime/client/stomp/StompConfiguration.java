package ru.prime.client.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.prime.client.model.ConfigurationParams;

@Slf4j
@Configuration
public class StompConfiguration {

    @Bean
    public WebSocketStompClient stompClient() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new StringMessageConverter());
        return stompClient;
    }

    public StompSession connect(ConfigurationParams configurationParams) throws Exception {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        if (configurationParams.getJwt() != null && configurationParams.getJwt().length() > 0) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + configurationParams.getJwt());
            log.info("Set authorization header: " + headers.getFirst(HttpHeaders.AUTHORIZATION));
        }
        log.info("Stomp Client start connection");
        ListenableFuture<StompSession> listenableFuture = stompClient().connect(configurationParams.getConnectUrl(), headers, new StompHandler(configurationParams));
        return listenableFuture.get();
    }
}
