package tech.jaya.ridely.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class UserConfig {
    @Bean
    fun userDetailService(): UserDetailsService {
        val passenger = User.withUsername("passenger")
            .password(passwordEncoder().encode("1234"))
            .roles("PASSENGER")
            .build()

        val driver = User.withUsername("driver")
            .password(passwordEncoder().encode("1234"))
            .roles("DRIVER")
            .build()

        return InMemoryUserDetailsManager(passenger,driver)
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}