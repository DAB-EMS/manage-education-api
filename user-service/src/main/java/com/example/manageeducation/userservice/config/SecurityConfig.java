package com.example.manageeducation.userservice.config;
;
import com.example.manageeducation.userservice.security.jwt.JwtAuthenticationFilter;
import com.example.manageeducation.userservice.service.impl.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher publicUrls = new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/auth/**"),
                new AntPathRequestMatcher("/v2/api-docs"),
                new AntPathRequestMatcher("/v3/api-docs"),
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/swagger-resources"),
                new AntPathRequestMatcher("/swagger-resources/**"),
                new AntPathRequestMatcher("/configuration/ui"),
                new AntPathRequestMatcher("/configuration/security"),
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/webjars/**"),
                new AntPathRequestMatcher("/swagger-ui.html"),
                new AntPathRequestMatcher("/bot")
        );

        http
                .cors().configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOrigins(Arrays.asList("http://localhost:3001","http://localhost:3000","https://manage-education-6wsgppv0y-anhbang369s-projects.vercel.app"));
                    cors.setAllowedMethods(Arrays.asList("*"));
                    cors.setAllowedHeaders(Arrays.asList("*"));
                    cors.setAllowCredentials(true);
                    return cors;
                })
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .expressionHandler(webExpressionHandler())
                .requestMatchers(publicUrls)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutService)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        ;

        return http.build();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(
                "FULL_ACCESS_SYLLABUS > CREATE_SYLLABUS\n CREATE_SYLLABUS > MODIFY_SYLLABUS\n MODIFY_SYLLABUS > VIEW_SYLLABUS\n"
                        +
                        "FULL_ACCESS_TRAINING_PROGRAM > CREATE_TRAINING_PROGRAM\n CREATE_TRAINING_PROGRAM > MODIFY_TRAINING_PROGRAM\n MODIFY_TRAINING_PROGRAM > VIEW_TRAINING_PROGRAM\n"
                        +
                        "FULL_ACCESS_CLASS > CREATE_CLASS\n CREATE_CLASS > MODIFY_CLASS\n MODIFY_CLASS > VIEW_CLASS\n" +
                        "FULL_ACCESS_LEARNING_MATERIAL > CREATE_LEARNING_MATERIAL\n CREATE_LEARNING_MATERIAL > MODIFY_LEARNING_MATERIAL\n MODIFY_LEARNING_MATERIAL > VIEW_LEARNING_MATERIAL\n"
                        +
                        "FULL_ACCESS_USER > CREATE_USER\n CREATE_USER > MODIFY_USER\n MODIFY_USER > VIEW_USER > NO_ACCESS_USER\n");
        return roleHierarchy;
    }

    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

}
