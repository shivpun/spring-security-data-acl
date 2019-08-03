package org.springframework.security.data.acl.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

	@RequestMapping(value = { "/" })
	public Authentication user() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}
}
