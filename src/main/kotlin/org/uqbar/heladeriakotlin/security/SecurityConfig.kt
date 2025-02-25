package org.uqbar.heladeriakotlin.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "/login").permitAll()
                it.requestMatchers("/error").permitAll()
                // No hace falta permisos para ver datos
                it.requestMatchers(HttpMethod.GET, "/**").permitAll()
                // Permisos de admin para modificar
                it.requestMatchers(HttpMethod.POST, "/heladerias").hasAuthority("ROLE_ADMIN")
                it.requestMatchers(HttpMethod.POST, "/duenios").hasAuthority("ROLE_ADMIN")
                it.requestMatchers(HttpMethod.PUT, "/heladerias/**").hasAuthority("ROLE_ADMIN")
                    //
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .sessionManagement { configurer ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // agregado para JWT, si comentás estas dos líneas tendrías Basic Auth
            //.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
            // fin agregado
            .exceptionHandling(Customizer.withDefaults())
            .build()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }
}