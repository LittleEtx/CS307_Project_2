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

select *
from item_fullInfo;