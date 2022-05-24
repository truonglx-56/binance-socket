package com.bank.exchange.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${endpoint:/ws-socket-alert}")
    private String endpoint;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {

        registry.addEndpoint(endpoint).setAllowedOriginPatterns("*").withSockJS();
//        registry.addEndpoint(endpoint).withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                log.info("### ChannelInterceptor execution");
                assert accessor != null;
                log.info("### Received StompCommand." + accessor.getCommand());

                return handleAuthentication(message, accessor.getNativeHeader("authorization"));

            }

            private Message<?> handleAuthentication(Message<?> message, List<String> authenticationHeaders) {

                if (!CollectionUtils.isEmpty(authenticationHeaders)) {

                    String token = authenticationHeaders.get(0);

                    //TODO call jwt utils library
                    if(token.equals("jwt-token"))
                    {
                        log.info("valid token for ws connection");
                        return message;
                    }
                }

                log.info("invalid token for ws connection");
                return null;

            }

        });
    }

}