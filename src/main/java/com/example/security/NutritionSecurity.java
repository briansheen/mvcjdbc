package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by bsheen on 5/18/17.
 */

@Configuration
@EnableWebSecurity
public class NutritionSecurity extends WebSecurityConfigurerAdapter{

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests().
                antMatchers("/").permitAll().
                antMatchers("/nutrition/delete/**").access("hasRole('ADMIN')").
                antMatchers("/product/delete/**").access("hasRole('ADMIN')").
                anyRequest().
                authenticated().
                and().
                formLogin().
                loginPage("/login").
                permitAll().
                and().
                logout().
                logoutSuccessUrl("/").
                permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.jdbcAuthentication().dataSource(this.dataSource).usersByUsernameQuery("select username, password, enabled from users where username = ?").authoritiesByUsernameQuery("select username, authority from authorities where username = ?");
    }
}
