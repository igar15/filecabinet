package com.igar15.filecabinet.config;

import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDecisionManager unnanimous(){
        final List<AccessDecisionVoter<? extends Object>> voters = List.of(new RoleVoter(), new AuthenticatedVoter(), new RealTimeLockVoter(), new WebExpressionVoter());
        return new UnanimousBased(voters);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

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
                        "/resetPassword*",
                        "/users/resetPassword*",
                        "/users/changePassword*",
                        "/users/savePassword*",
                        "/css/**",
                        "/js/**").permitAll()
//                .antMatchers("/**/delete/**").hasAnyRole("OTD-WORKER", "ADMIN")
                .anyRequest().authenticated().accessDecisionManager(unnanimous())

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

                .and().sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry()).and().sessionFixation().none()

                .and()
                .csrf().disable();
    }

    private class RealTimeLockVoter implements AccessDecisionVoter<Object> {

        @Override
        public boolean supports(ConfigAttribute attribute) {
            return true;
        }

        @Override
        public boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
            List<User> lockedUsers = userService.findAllByNonLocked(false);
            if (lockedUsers.stream()
                    .anyMatch(user -> user.getEmail().equals(authentication.getName()))) {
                return ACCESS_DENIED;
            }
            return ACCESS_GRANTED;
        }
    }


}
