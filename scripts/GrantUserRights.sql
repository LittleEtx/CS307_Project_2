grant usage on schema public to sustc_manager;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO sustc_manager;

grant usage on schema public to company_manager;
grant select, update, insert on staff_company,staff_handle_item,container,item_container,
    item_state,item_type,item_route,ship,item_ship,tax_info to company_manager;
grant select on company, staff_handle_item, item_company, item_fullinfo to company_manager;

grant usage on schema public to courier;
grant select, update, insert on item_state to courier;
grant select, insert on item, item_route, staff_handle_item to courier;
grant select, update on staff to courier;
grant select on city,tax_info,staff_city, staff_company, item_company, item_fullinfo, item_type to courier;

grant usage on schema public to seaport_officer;
grant select, insert on staff_handle_item to seaport_officer;
grant select, update on item_state, staff to seaport_officer;
grant select on staff,staff_city, item,item_route,item_type,item_state, item_company, item_fullinfo to seaport_officer;