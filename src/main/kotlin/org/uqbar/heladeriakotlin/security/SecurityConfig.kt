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
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.uqbar.heladeriakotlin.model.ROLES


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    lateinit var jwtAuthorizationFilter: JWTAuthorizationFilter

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                // Queremos que cualquier persona se pueda loguear y mostrar errores
                // Importante: no definirlo solo para el method POST
                // porque CORS requiere que también puedas mandar el OPTIONS para hacer el pre-flight
                it.requestMatchers("/login").permitAll()
                it.requestMatchers("/error").permitAll()
                it.requestMatchers(HttpMethod.OPTIONS).permitAll()
                // Permisos de admin para modificar
                it.requestMatchers(HttpMethod.POST, "/heladerias").hasAuthority(ROLES.ADMIN.name)
                it.requestMatchers(HttpMethod.POST, "/duenios").hasAuthority(ROLES.ADMIN.name)
                it.requestMatchers(HttpMethod.PUT, "/heladerias/**").hasAuthority(ROLES.ADMIN.name)
                    // Default: que se autentique para poder ver información
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .sessionManagement { configurer ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // agregado para JWT, si comentás estas dos líneas tendrías Basic Auth
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
            // fin agregado
            .exceptionHandling(Customizer.withDefaults())
            .build()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedHeaders("*")
                    .allowedMethods("POST", "OPTIONS", "GET", "PUT", "DELETE")
                    .allowCredentials(true)
            }
        }
    }
}