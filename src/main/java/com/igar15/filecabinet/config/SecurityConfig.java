package com.igar15.filecabinet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/signup",
                        "/user/register",
                        "/registrationConfirm*",
                        "/badUser*",
                        "/forgotPassword*",
                        "/user/resetPassword*",
                        "/user/changePassword*",
                        "/user/savePassword*",
                        "/css/**",
                        "/js/**").permitAll()
//                .antMatchers("/**/delete/**").hasAnyRole("OTD-WORKER", "ADMIN")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/goLogin")

                .and()
                .rememberMe()
                .tokenValiditySeconds(604800)
                .key("lssAppKey")
                //.useSecureCookie(true)
                .rememberMeCookieName("sticky-cookie")
                .rememberMeParameter("remember")

                .and()
                .logout().permitAll().logoutUrl("/logout")

                .and()
                .csrf().disable();
    }
}
