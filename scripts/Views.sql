create or replace view item_company as
select item_name, company_id
from staff_handle_item
         left join staff_company on staff_handle_item.staff_id = staff_company.staff_id
where stage = 'RETRIEVAL';

create or replace view item_fullInfo as
select item.name        as name,
       item.price       as price,
       item.class       as class,
       item_state.state as state,
       rc.city_id       as retrieval_city,
       rs.name          as retrieval_staff,
       dc.city_id       as delivery_city,
       ds.name          as delivery_staff,
       ic.city_id       as import_city,
       if.name          as import_staff,
       ec.city_id       as export_city,
       es.name          as export_staff
from (select name, price, class from item) as item
         left join (select item_name, state from item_state) as item_state on item.name = item_state.item_name
         left join (select item_name, city_id from item_route where stage = 'RETRIEVAL') as rc
                   on item.name = rc.item_name
         left join (select item_name, city_id from item_route where stage = 'IMPORT') as ic
                   on item.name = ic.item_name
         left join (select item_name, city_id from item_route where stage = 'EXPORT') as ec
                   on item.name = ec.item_name
         left join (select item_name, city_id from item_route where stage = 'DELIVERY') as dc
                   on item.name = dc.item_name
         left join (select item_name, staff_id from staff_handle_item where stage = 'RETRIEVAL') as r
                   on item.name = r.item_name
         left join (select id, name from staff) as rs on r.staff_id = rs.id
         left join (select item_name, staff_id from staff_handle_item where stage = 'IMPORT') as i
                   on item.name = i.item_name
         left join (select id, name from staff) as if on i.staff_id = if.id
         left join (select item_name, staff_id from staff_handle_item where stage = 'EXPORT') as e
                   on item.name = e.item_name
         left join (select id, name from staff) as es on e.staff_id = es.id
         left join (select item_name, staff_id from staff_handle_item where stage = 'DELIVERY') as d
                   on item.name = d.item_name
         left join (select id, name from staff) as ds on d.staff_id = ds.id;

create or replace view staff_info as
select id,
       name,
       authority,
       password,
       phone_number,
       gender,
       birth,
       city_id    city,
       company_id company
from staff
         join verification on staff.id = verification.staff_id
         left join staff_company on staff.id = staff_company.staff_id
         left join staff_city on staff.id = staff_city.staff_id;

create or replace view ship_info as
select ship.name       as name,
       ship.company_id as company,
       case a.shipping is not null
           when true then 'SAILING'
           else 'DOCKED'
           end         as state
from ship
         left join
     (select ship_name, count(item_ship.item_name) shipping
      from item_state
               right join item_ship
                          on item_state.item_name = item_ship.item_name
      where state = 'SHIPPING'
      group by item_ship.ship_name) as a
     on ship.name = a.ship_name;

create or replace view container_info as
select code,
       type,
       case a.packing is not null
           when true then 'USING'
           else 'IDLE'
           end as state
from container
         left join
     (select container_code, item_state.item_name packing
      from item_state
               right join item_container
                          on item_state.item_name = item_container.item_name
      where state in ('PACKING_TO_CONTAINER', 'WAITING_FOR_SHIPPING', 'SHIPPING')) as a
     on container.code = a.container_code;
