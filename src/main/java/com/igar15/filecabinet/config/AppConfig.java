package com.igar15.filecabinet.config;

import org.passay.LengthRule;
import org.passay.PasswordValidator;
import org.passay.WhitespaceRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

import java.util.Arrays;

@Configuration
public class AppConfig {

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidator(Arrays.asList(new LengthRule(4, 30), new WhitespaceRule()));
    }



}
