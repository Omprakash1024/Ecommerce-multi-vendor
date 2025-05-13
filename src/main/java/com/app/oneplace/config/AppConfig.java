package com.app.oneplace.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class AppConfig {
	@Bean
	SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
		http.sessionManagement(management -> management.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS
				)).authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/api/**").authenticated()
						.requestMatchers("/api/products/*/reviews").permitAll()
						.anyRequest().permitAll())
		                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
		                .csrf(csrf->csrf.disable())
		                .cors(cors -> cors.configurationSource(corsConfiguration()));
		
		return http.build();
	}

	private CorsConfigurationSource corsConfiguration() {
		return new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration corsConfiguration =new CorsConfiguration();
				corsConfiguration.setAllowedOrigins(Collections.singletonList("*")); //all the urls will be accessed 
				corsConfiguration.setAllowedMethods(Collections.singletonList("*"));  //all the methods will be accessed, (post,get,delete)you can configure specific methods
				corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
				corsConfiguration.setAllowCredentials(true);
				corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
				corsConfiguration.setMaxAge(3600l);
				return corsConfiguration;
			}
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
