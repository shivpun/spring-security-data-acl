package org.springframework.security.data.acl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.data.acl.config.EnableAclJpaRepositories;

@SpringBootApplication
@EnableAclJpaRepositories(basePackages = { "org.springframework.security.data.acl.repository" })
public class ApplicationLocal {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationLocal.class, args);
	}
}
