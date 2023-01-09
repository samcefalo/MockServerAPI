package me.samcefalo.mockserverapi.config;

import org.mockserver.client.proxy.ProxyClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockServerConfig {

    @Value("${mock.port}")
    private Integer mockPort;

    @Bean
    public ClientAndServer mockServer() {
        return new ClientAndServer(mockPort);
    }

}
