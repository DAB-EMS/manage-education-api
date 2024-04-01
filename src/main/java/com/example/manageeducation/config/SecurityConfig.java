//package com.example.manageeducation.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.OrRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        RequestMatcher publicUrls = new OrRequestMatcher(
//                new AntPathRequestMatcher("/api/v1/auth/**"),
//                new AntPathRequestMatcher("/v2/api-docs"),
//                new AntPathRequestMatcher("/v3/api-docs"),
//                new AntPathRequestMatcher("/v3/api-docs/**"),
//                new AntPathRequestMatcher("/swagger-resources"),
//                new AntPathRequestMatcher("/swagger-resources/**"),
//                new AntPathRequestMatcher("/configuration/ui"),
//                new AntPathRequestMatcher("/configuration/security"),
//                new AntPathRequestMatcher("/swagger-ui/**"),
//                new AntPathRequestMatcher("/webjars/**"),
//                new AntPathRequestMatcher("/swagger-ui.html"),
//                new AntPathRequestMatcher("/bot")
//        );
//
//
//        http
//                .csrf()
//                .disable()
//                .authorizeRequests()
//                .requestMatchers(publicUrls)
//                .permitAll()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permit OPTIONS requests globally
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .logout()
//                .logoutUrl("/api/v1/auth/logout")
//                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
//
//        return http.build();
//    }
//}
