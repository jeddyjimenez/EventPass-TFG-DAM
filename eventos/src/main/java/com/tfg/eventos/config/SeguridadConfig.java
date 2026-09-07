package com.tfg.eventos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        // Configuración principal de la seguridad de la aplicación
        http
            // Se desactiva CSRF para facilitar el uso de la API móvil
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas que no necesitan inicio de sesión
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/registro_exito",
                    "/public/**",
                    "/eventos",
                    "/eventos/*",
                    "/busqueda",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/uploads/**",
                    "/webjars/**",
                    "/favicon.ico",
                    "/*.png",
                    "/*.jpg",
                    "/*.jpeg",
                    "/*.gif",
                    "/*.webp",
                    "/*.svg"
                ).permitAll()
                // Rutas protegidas por rol
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/validador/**").hasRole("VALIDADOR")
                .requestMatchers("/api/validador/**").hasRole("VALIDADOR")
                // Rutas para cualquier usuario autenticado
                .requestMatchers("/mis_entradas", "/eventos/*/reservar").authenticated()
                .anyRequest().authenticated()
            )
            // Configuración del login usando email y contraseña personalizados
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("contrasena")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .httpBasic(httpBasic -> {})
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt se usa para guardar las contraseñas cifradas
        return new BCryptPasswordEncoder();
    }
}
