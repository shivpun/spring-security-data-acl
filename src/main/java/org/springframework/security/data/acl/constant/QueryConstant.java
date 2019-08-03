package org.springframework.security.data.acl.constant;

public abstract class QueryConstant {
	
	public static final String FETCH_FILTER_RULE ="SELECT SYS_GUID() ID, r.RULE_ID, r.DOMAIN, NULL group_by, NULL sort_by, r.NAME, r.create, r.update, r.read, r.delete FROM ACL_RULE r JOIN ACL_ENTITY e ON (r.ENTITY_ID=e.ENTITY_ID) WHERE E.NAME=? AND %s is true AND (r.RULE_ID IN (SELECT RULE_ID FROM acl_group_rule_rel rg JOIN acl_user_group_rel gu ON (rg.GROUP_ID=gu.GROUP_ID) WHERE gu.USER_ID=?))";
	
	public static final String READ = "read";
}
