package org.privatechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="application.security.config")
public class ApplicationSecurityConfig {
	@Value("${application.security.config.url-matchers}")
	public String [] urlMatchers;		
}
