package com.ecommerce.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.ecommerce.model.CustomUserDetail;
import com.ecommerce.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	GoogleOauth2SuccessHandler googleOauth2SuccessHandler;
	@Autowired
	CustomUserDetailService customUserDetailService;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .authorizeRequests()
	        .antMatchers("/", "/home/**","/shop/**", "/forgotpassword", "/register", "/h2-console/**", "/style/**", "/images/**", "/js/**") // Add static resources
	        .permitAll()
//	        .antMatchers("/v3/api-docs").permitAll().antMatchers(HttpMethod.GET).permitAll()
	        .antMatchers("/admin/**").hasRole("ADMIN")
	        .anyRequest()
	        .authenticated()
	        .and()
	        .formLogin()
	        .loginPage("/login")
	        .permitAll()
	        .failureUrl("/login?error=true")
	        .defaultSuccessUrl("/home")
	        .usernameParameter("email")
	        .passwordParameter("password")
	        .and()
	        .oauth2Login()
	        .loginPage("/login")
	        .successHandler(googleOauth2SuccessHandler)
	        .and()
	        .logout()
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	        .logoutSuccessUrl("/login")
	        .invalidateHttpSession(true)
	        .deleteCookies("JSESSIONID")
	        .and()
	        .exceptionHandling()
	        .and()
	        .csrf().disable();

	    http.headers().frameOptions().disable();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return  new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService);
	}

	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**", "/productImages/**", "/css/**", "/js/**");
		
		  
	}
}
