package org.springframework.security.data.acl.constant;

public abstract class QueryConstant {
	
	public static final String FETCH_USER_RULE ="SELECT sys_guid() ID, r.NAME, r.DOMAIN, NULL group_by, NULL sort_by, r.create, r.update, r.read, r.delete FROM ACL_RULE r JOIN ACL_ENTITY e ON (r.ENTITY_ID=e.ENTITY_ID) WHERE E.NAME=? AND %s is true AND (r.RULE_ID IN (SELECT RULE_ID FROM acl_group_rule_rel rg JOIN acl_user_group_rel gu ON (rg.GROUP_ID=gu.GROUP_ID) WHERE gu.USER_ID=?) OR r.gobal)";
	
	public static final String READ = "read";
	
	public static final String FETCH_USER_FILTER = "SELECT sys_guid() ID, f.NAME, f.DOMAIN, f.group_by, f.sort_by, f.create, f.update, f.read, f.delete FROM ACL_FILTER f JOIN ACL_ENTITY e ON (f.ENTITY_ID=e.ENTITY_ID) WHERE E.NAME=? AND %s is true AND f.FILTER_ID IN (SELECT FILTER_ID FROM acl_user_filter_rel fu WHERE fu.USER_ID=?)";
}
