package com.config;

import com.services.PeopleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JWTFilter jwtFilter;
    private final PeopleService peopleService;

    public SecurityConfig(JWTFilter jwtFilter, PeopleService peopleService) {
        this.jwtFilter = jwtFilter;

        this.peopleService = peopleService;
    }


    // Конфигурация HTTP Security
    protected void configure(HttpSecurity http) throws Exception{
        // конфигурируем сам Spring security
        // конфигурируем авторизацию
        http.authorizeRequests()// Разрешаем доступ к некоторым URL без аутентификации
                        .antMatchers("/auth/login", "/auth/registration","/error", "/activate/*").permitAll()
                // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/login")// Страница логина
                .loginProcessingUrl("/process_login")// URL для обработки логина
                .defaultSuccessUrl("/hello", true)// URL при успешной аутентификации
                .failureUrl("/auth/login?error")// URL при ошибке аутентификации
                .and()
                .logout().
                logoutUrl("/logout").// URL для выхода
                logoutSuccessUrl("/auth/login")// URL при успешном выходе
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);// Указываем, что сессии не используются
        http.addFilterBefore((Filter) jwtFilter,  UsernamePasswordAuthenticationFilter.class);
    }

    //Настраиваем аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(peopleService)
                .passwordEncoder(getPasswordEncoder());
    }
    // Бин для получения PasswordEncoder (BCryptPasswordEncoder)
        @Bean
        public PasswordEncoder getPasswordEncoder(){
            return new BCryptPasswordEncoder();
    }
    // Бин для получения AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManagerBean();
    }
}
