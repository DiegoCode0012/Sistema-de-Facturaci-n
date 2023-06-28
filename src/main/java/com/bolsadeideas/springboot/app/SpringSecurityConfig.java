package com.bolsadeideas.springboot.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity

public class SpringSecurityConfig {
	
	@Autowired
	private LoginSuccessHandler successHandler;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 

	@Autowired
    private JpaUserDetailsService userDetailService;
	/* 
	    @Bean
	    public InMemoryUserDetailsManager userDetailsService(){
	        UserDetails user = User.withUsername("user")
	                .password(passwordEncoder.encode("user"))
	                .roles("USER")
	                .build();
	 
	        UserDetails admin = User.withUsername("admin")
	                .password(passwordEncoder.encode("admin"))
	                .roles("USER","ADMIN")
	                .build();
	 
	        return  new InMemoryUserDetailsManager(user,admin);
	    }*/
	
	 /*
	@Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                //.userDetailsService(userDetailsService())
                .jdbcAuthentication()
                .dataSource(data)
                .passwordEncoder(passwordEncoder)
	                .usersByUsernameQuery("select username, password, enabled from users where username=?") //la contraseña lo verifica por debajo, basta con el username
                .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?")
                .and().build();
    }
 */
	 
	  @Autowired
      public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
         build.userDetailsService(userDetailService)
         .passwordEncoder(passwordEncoder);
      }
	    
	    @Bean
	     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf().disable().cors().and()
	                .authorizeHttpRequests()
	                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar**","/locale","/api/clientes/**")
	               .permitAll()
	                .requestMatchers("/ver/**").hasAnyRole("USER")
	                .requestMatchers("/uploads/**").hasAnyRole("USER")
	                .requestMatchers("/form/**").hasAnyRole("ADMIN")
	                .requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
	                .requestMatchers("/factura/**").hasAnyRole("ADMIN")
	                .anyRequest().authenticated()
	                .and().formLogin().successHandler(successHandler).loginPage("/login")
	                .permitAll()
	                .and()
	                .exceptionHandling().accessDeniedPage("/error_403") // error 403 nos lleva al MVcConfig al controlador donde no hay logica
	                .and().logout()
	                .permitAll();
	 
	        return http.build();
	    }
}

	    
 
