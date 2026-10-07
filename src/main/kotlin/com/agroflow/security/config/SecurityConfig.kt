package com.agroflow.security.config

import com.agroflow.security.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher("/api/auth/**")).permitAll()
                it.requestMatchers(org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher("/api/fincas/**")).permitAll()
                it.requestMatchers(org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher("/api/users/cliente")).permitAll()
                it.requestMatchers(org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher("/uploads/**")).permitAll()
                it.requestMatchers(org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher("/error")).permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
