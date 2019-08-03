package org.springframework.security.data.acl.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.data.acl.provisioning.AclJpaAuthenticationProvider;
import org.springframework.security.data.acl.provisioning.AclJpaUserDetailsManager;
import org.springframework.security.data.acl.repository.AclUserRepository;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired
	private UserDetailsManager userDetailsService;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Value(value = "${spring.security.acl.defaultLoginFrom:true}")
	private boolean defaultLoginForm;

	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		LOGGER.info("Configuration of AuthenticationManagerBuilder \n 1. Added AuthenticationProvider.");
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.info(
				"Configuration of HttpSecurity \n 1. Enabled SessionFixation. \n 2. Enabled Csrf. \n 3. Enabled Cors. \n 4. Enabled Invalidate Session. \n    a. Delete JSESSIONID and SESSION from cookies \n    b. Cleared Authentication");
		http.sessionManagement().sessionFixation().migrateSession().and().authorizeRequests().anyRequest()
				.authenticated().and().formLogin().and().cors().and().csrf().and().httpBasic().disable().logout()
				.invalidateHttpSession(true).logoutSuccessUrl("/").clearAuthentication(true)
				.deleteCookies("JSESSIONID", "SESSION");
	}

	@Bean
	public AuthenticationProvider amberAuthenticationProvider() {
		return new AclJpaAuthenticationProvider(userDetailsService);
	}

	@Bean
	public UserDetailsManager userDetailsManager(@Autowired AclUserRepository aclUserRepository) {
		return new AclJpaUserDetailsManager(aclUserRepository);
	}
}
