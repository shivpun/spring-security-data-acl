package org.springframework.security.data.acl.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.data.acl.entity.AclUser;
import org.springframework.security.data.acl.model.User;

public class AclUserToUser implements Converter<AclUser, UserDetails> {

	@Override
	public UserDetails convert(AclUser aclUser) {
		if(aclUser==null || aclUser.getUserId()==null) {
			throw new UsernameNotFoundException("User doesn't exist");	
		}
		UserDetails userDetails = User.builder()
						.userId((long)aclUser.getUserId())
						.username(aclUser.getName())
						.password(aclUser.getPassword())
						.authorities("DB_CHECK")
						.build();
		return userDetails;
	}
}
