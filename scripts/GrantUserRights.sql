grant usage on schema public to sustc_manager;
grant update on verification, staff to sustc_manager;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO sustc_manager;

grant usage on schema public to company_manager;
grant select, update on verification, staff, item_state to company_manager;
grant select, update, insert on item_container to company_manager;
grant select, insert on item_ship to company_manager;
grant select on company, staff_handle_item, item_company, item_info_extra,tax_info, ship,
    item_fullinfo, staff_info, ship_info, container_info, staff_company to company_manager;

grant usage on schema public to courier;
grant select, update, insert on item_state to courier;
grant select, insert on item, item_route, staff_handle_item to courier;
grant select, update on staff, verification to courier;
grant select on city,tax_info,staff_city, staff_company, item_company, item_fullinfo, item_type to courier;

grant usage on schema public to seaport_officer;
grant select, insert on staff_handle_item to seaport_officer;
grant select, update on item_state, staff, verification to seaport_officer;
grant select on staff_city, item,item_route,item_type, item_company, item_fullinfo, ship_info to seaport_officer;