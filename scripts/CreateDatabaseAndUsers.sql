create database sustc;
revoke create on schema public from public;
revoke usage on schema public from public;



create user sustc_manager password 'sustc_manager';
grant usage on schema public to sustc_manager;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO sustc_manager;


create user company_manager password 'company_manager';
grant usage on schema public to company_manager;
grant select,update,insert,delete on staff_company,container,container_item,
    item_state,item_type,item_route,ship,ship_container,ship_state,tax_info to company_manager;
grant select,update on company to company_manager;


create user courier password 'courier';
grant usage on schema public to courier;
grant select,update,insert,delete on item,item_route,item_state,item_type,item_courier to courier;
grant select,update on staff,staff_city to courier;


create user seaport_officer password 'seaport_officer';
grant usage on schema public to seaport_officer;
grant select,update,insert,delete on item,item_route,item_type,item_state to seaport_officer;
grant select,update on staff,staff_city to courier;






