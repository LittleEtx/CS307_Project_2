drop owned by courier;
drop user courier;

drop owned by company_manager;
drop user company_manager;

drop owned by seaport_officer;
drop user seaport_officer;

drop owned by sustc_manager;
drop user sustc_manager;

--select rolname from pg_roles;  查user列表
--select * from information_schema.table_privileges where grantee='seaport_officer';  查某个user的权限