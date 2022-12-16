grant usage on schema public to sustc_manager;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO sustc_manager;

grant usage on schema public to company_manager;
grant select, update, insert on staff_company,container,item_container,
    item_state,item_type,item_route,ship,container_ship,ship_state,tax_info to company_manager;
grant select, update on company to company_manager;

grant usage on schema public to courier;
grant select, update, insert on item,item_route,item_state,item_type,staff_handle_item to courier;
grant select, update on staff,staff_city to courier;

grant usage on schema public to seaport_officer;
grant select, update on item_state to seaport_officer;
grant select on staff,staff_city, item,item_route,item_type,item_state, staff_handle_item, ship_state to seaport_officer;