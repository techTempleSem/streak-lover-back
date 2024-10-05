package com.example.streak.config.property;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:static/properties/env.properties")
public class PropertyConfig{

}