package com.example.shiftmaker

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity

@Configuration
@EnableWebMvcSecurity
public class securityConfig : WebSecurityConfigurerAdapter(){

}
