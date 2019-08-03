package org.springframework.security.data.acl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.data.acl.specification.AclJpaSpecification;
import org.springframework.security.data.acl.specification.SimpleAclJpaSpecification;

@Configuration
public class AclJpaConfiguration {
	
	@Bean(name = "aclJpaSpecification")
	public AclJpaSpecification<Object> aclJpaSpecification() {
		return new SimpleAclJpaSpecification();
	}
}
