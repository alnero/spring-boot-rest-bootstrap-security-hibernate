package com.example.project.security;

import com.example.project.model.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;
    private SuccessUserHandler successUserHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/users/{id:\\d+}").hasAnyAuthority(UserAuthority.Role.USER.name(), UserAuthority.Role.ADMIN.name())
                .antMatchers("/users/**").hasAuthority(UserAuthority.Role.ADMIN.name())
                .antMatchers("/api/users/{id:\\d+}").hasAnyAuthority(UserAuthority.Role.USER.name(), UserAuthority.Role.ADMIN.name())
                .antMatchers("/api/users/**").hasAuthority(UserAuthority.Role.ADMIN.name())
                .antMatchers("/webjars/**", "/js/**", "/css/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .permitAll()
                .loginPage("/login")
                .successHandler(successUserHandler)
                .loginProcessingUrl("/login");

        http.logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

