package org.example.config;

import org.example.domain.policy.FeePolicy;
import org.example.domain.policy.FeeTablePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public FeePolicy feePolicy() {
        return new FeeTablePolicy();
    }
}