package me.samcefalo.mockserverapi.config;

import me.samcefalo.mockserverapi.logs.LogGenerator;
import me.samcefalo.mockserverapi.factories.LogGeneratorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogGeneratorConfig {

    @Value("${mock.save_log_type}")
    private String saveLogType;

    @Bean
    public LogGenerator logGenerator() {
        return LogGeneratorFactory.getLogGenerator(saveLogType);
    }

}
