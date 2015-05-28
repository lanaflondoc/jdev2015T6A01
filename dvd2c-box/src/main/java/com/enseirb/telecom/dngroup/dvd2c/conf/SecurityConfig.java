package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.enseirb.telecom.dngroup.dvd2c.filter.CsrfHeaderFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
//	 @Override
//	    protected void configure(HttpSecurity http) throws Exception {
//
//	       http
//	       .authorizeRequests()
//			.anyRequest().hasRole("USER")
//			.and()
//        .httpBasic();
//	    }
//	 @Bean
//	    public RememberMeServices rememberMeServices() {
//	        // Key must be equal to rememberMe().key() 
//	        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("your_key", null);
//	        rememberMeServices.setCookieName("remember_me_cookie");
//	        rememberMeServices.setParameter("remember_me_checkbox");
//	        rememberMeServices.setTokenValiditySeconds(2678400); // 1month
//	        return rememberMeServices;
//	    }
//	        http
//	        
//	            .authorizeRequests()
//	                .anyRequest().authenticated()
//	                .and()
//	            .formLogin()
//	                .loginPage("/index.html").permitAll()
//	                .and()
//	            .formLogin()
//	            	.defaultSuccessUrl("/main.html")
//	            	.and().formLogin().failureUrl("/error.html").permitAll();
//	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        	.httpBasic()
	        		.and()
	            .authorizeRequests()
	                .antMatchers("/api/application.wadl").permitAll()
	                .antMatchers("/api/app/account/").permitAll() 
	                .anyRequest().hasAnyRole("USER")
	                .and()
	            .formLogin()
	                .loginPage("/index.html").usernameParameter("email")
//	                .loginProcessingUrl("/j_spring_security_check")
//	                .defaultSuccessUrl("/main.html")
//	                .failureUrl("/")
	                .permitAll()
	                .and()
	                
	            .logout()                                    
	                .permitAll()
	                .and()
	            .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
	            	.csrf()
	            	.csrfTokenRepository(csrfTokenRepository());
	    }
	    
	    private CsrfTokenRepository csrfTokenRepository() {
	    	  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	    	  repository.setHeaderName("X-XSRF-TOKEN");
	    	  return repository;
	    	}


	// @formatter:off
	@Autowired
	public void configureGlobal(
			AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER");
	}
	// @formatter:on
}
