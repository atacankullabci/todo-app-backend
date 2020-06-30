package com.atacankullabci.todoapp.config;

import com.atacankullabci.todoapp.security.jwt.JwtConfigurer;
import com.atacankullabci.todoapp.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    public SecurityConfiguration(@Qualifier("userDetailServiceImpl") UserDetailsService userDetailsService, JwtProvider jwtProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        // All request which are starting with /api/auth are going to pass to backend
        // The remaining end points are need to be authenticated
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and()
                .apply(getCustomJwtConfigurer());
        //@formatter:on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    private JwtConfigurer getCustomJwtConfigurer() {
        return new JwtConfigurer(jwtProvider, userDetailsService);
    }
}
