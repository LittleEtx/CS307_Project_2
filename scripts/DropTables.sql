
drop owned by sustc_manager;
drop user sustc_manager;

drop owned by courier;
drop user courier;

drop owned by company_manager;
drop user company_manager;

drop owned by seaport_officer;
drop user seaport_officer;

drop table container_item;
drop table ship_container;
drop table item_state;
drop table ship_state;
drop table staff_city;
drop table staff_company;
drop table tax_info;
drop table item_courier;
drop table item_route;
drop table verification;
drop table staff;
drop table city;
drop table ship;
drop table company;
drop table item;
drop table container;
drop table item_type;


--select rolname from pg_roles;  查user列表
--select * from information_schema.table_privileges where grantee='seaport_officer';  查某个user的权限
