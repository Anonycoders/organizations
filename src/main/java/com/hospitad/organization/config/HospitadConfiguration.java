package com.hospitad.organization.config;

import com.hospitad.organization.OrganizationApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class HospitadConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OrganizationApplication.class);

    @Bean
    public RestTemplate getRetTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

}
