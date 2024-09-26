package org.do_an.quiz_java.configuration;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.components.CustomAccessDeniedHandler;
import org.do_an.quiz_java.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request.anyRequest().permitAll();
                })
                .authenticationProvider(authenticationProvider)
                            .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler));;
        return http.build();
    }
}
