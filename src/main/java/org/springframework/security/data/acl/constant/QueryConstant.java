package org.springframework.security.data.acl.constant;

import java.util.HashMap;
import java.util.Map;

public abstract class QueryConstant {

	public static final String CREATE = "create";

	public static final String UPDATE = "update";

	public static final String READ = "read";

	public static final String DELETE = "delete";

	public static final String LOGIN_USER_ID = "#user.uid";

	public static final String FETCH_USER_RULE = "SELECT sys_guid() ID, r.NAME, r.DOMAIN, NULL group_by, NULL sort_by, r.create, r.update, r.read, r.delete FROM ACL_RULE r JOIN ACL_ENTITY e ON (r.ENTITY_ID=e.ENTITY_ID) WHERE E.NAME=? AND r.%s is true AND (r.RULE_ID IN (SELECT RULE_ID FROM acl_group_rule_rel rg JOIN acl_user_group_rel gu ON (rg.GROUP_ID=gu.GROUP_ID) WHERE gu.USER_ID=?) OR r.gobal)";

	public static final String FETCH_USER_FILTER = "SELECT sys_guid() ID, f.NAME, f.DOMAIN, f.group_by, f.sort_by, f.create, f.update, f.read, f.delete FROM ACL_FILTER f JOIN ACL_ENTITY e ON (f.ENTITY_ID=e.ENTITY_ID) WHERE E.NAME=? AND f.%s is true AND f.FILTER_ID IN (SELECT FILTER_ID FROM acl_user_filter_rel fu WHERE fu.USER_ID=?)";

	public static final String ACL_ACCESS_RULE = "SELECT NVL(MAX(CASE WHEN A.%s THEN 1 ELSE 0 END), 0) AS ACCESS FROM ACL_ACCESS A JOIN ACL_ENTITY E ON (E.ENTITY_ID = A.ENTITY_ID) JOIN acl_user_group_rel gu ON (gu.group_id=A.GROUP_ID) JOIN ACL_GROUP G ON (G.GROUP_ID=A.GROUP_ID) JOIN MODULE M ON (G.MODULE=M.MODULE_ID) WHERE gu.user_id=? AND E.NAME=? AND M.NAME=?";

	public static final String ACL_ACCESS_GENERIC = "SELECT NVL(MAX(CASE WHEN A.%s THEN 1 ELSE 0 END), 0) AS ACCESS FROM ACL_ACCESS A JOIN ACL_ENTITY E ON (E.ENTITY_ID = A.ENTITY_ID) WHERE A.GROUP_ID IS NULL AND E.NAME=?";

	public static final Map<String, String> ACL_ACCESS_ERROR_MSG = new HashMap<String, String>();

	static {
		ACL_ACCESS_ERROR_MSG.put(CREATE, "Sorry, you are not allowed to create this kind of document.");
		ACL_ACCESS_ERROR_MSG.put(UPDATE, "Sorry, you are not allowed to modify this document.");
		ACL_ACCESS_ERROR_MSG.put(READ, "Sorry, you are not allowed to access this document.");
		ACL_ACCESS_ERROR_MSG.put(DELETE, "Sorry, you are not allowed to delete this document.");
	}
}
