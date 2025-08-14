package tech.jaya.ridely.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf{ it.disable()}
            .authorizeHttpRequests {
                it.requestMatchers("/trips/estimate").hasRole("PASSENGER")
                    .requestMatchers("/trips/create").hasRole("PASSENGER")
                    .anyRequest().authenticated()
            }
            .httpBasic{ }
        return http.build()
    }
}