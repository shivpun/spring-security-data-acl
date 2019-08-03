/*
USER
*/
INSERT INTO ACL_USER (user_id, name, password) VALUES(1, 'admin', '{noop}admin');
INSERT INTO ACL_USER (user_id, name, password) VALUES(2, 'sale_in', '{noop}sale');
INSERT INTO ACL_USER (user_id, name, password) VALUES(3, 'sale_usr', '{noop}sale');

/*
Module
*/
INSERT INTO MODULE (module_id, name) VALUES(1, 'Access Right');
INSERT INTO MODULE (module_id, name) VALUES(2, 'Product');

/*
ENTITY
*/
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (1, 'org.springframework.security.data.acl.entity.AclAccess', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (2, 'org.springframework.security.data.acl.entity.AclEntity', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (3, 'org.springframework.security.data.acl.entity.AclFilter', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (4, 'org.springframework.security.data.acl.entity.AclGroup', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (5, 'org.springframework.security.data.acl.entity.AclRule', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (6, 'org.springframework.security.data.acl.entity.AclUser', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (7, 'org.springframework.security.data.acl.entity.CurdAccess', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (8, 'org.springframework.security.data.acl.entity.Module', 1);
INSERT INTO ACL_ENTITY (entity_id, name, module) VALUES (9, 'org.springframework.security.data.acl.entity.Product', 2);

/*
GROUP
*/
INSERT INTO ACL_GROUP (group_id, name, module, create, update, read, delete) VALUES (1, 'CONFIGURATION', 1, 1, 1, 1, 1);
INSERT INTO ACL_GROUP (group_id, name, module, create, update, read, delete) VALUES (2, 'SALE-CONFIGURATION', 2, 1, 1, 1, 1);
INSERT INTO ACL_GROUP (group_id, name, module, create, update, read, delete) VALUES (3, 'SALE', 2, 0, 0, 1, 0);

/*
USER-GROUP
*/
INSERT INTO acl_user_group_rel (user_id, group_id) VALUES (1, 1);
INSERT INTO acl_user_group_rel (user_id, group_id) VALUES (2, 2);
INSERT INTO acl_user_group_rel (user_id, group_id) VALUES (3, 3);

/*
RULE
*/
INSERT INTO ACL_RULE (rule_id, name, domain, entity_id, create, update, read, delete) VALUES (1, 'Active Product', '{{''active'', ''isTrue''}}', 9, 0, 0, 1, 0); 

/*
GROUP-RULE
*/
INSERT INTO acl_group_rule_rel (group_id, rule_id) VALUES (3, 1);


commit;