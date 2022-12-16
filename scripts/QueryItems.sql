select *
from (select name, price, class from item) as item
         left join (select item_name, state from item_state) as item_state on item.name = item_state.item_name
         left join (select item_name, city_id from item_route where stage = 'RETRIEVAL') as retrieval_city
                   on item.name = retrieval_city.item_name
         left join (select item_name, city_id from item_route where stage = 'IMPORT') as import_city
                   on item.name = import_city.item_name
         left join (select item_name, city_id from item_route where stage = 'EXPORT') as export_city
                   on item.name = export_city.item_name
         left join (select item_name, city_id from item_route where stage = 'DELIVERY') as delivery_city
                   on item.name = delivery_city.item_name
         left join (select item_name, staff_id from staff_handle_item where stage = 'RETRIEVAL') as retrieval
                   on item.name = retrieval.item_name
         left join (select id, name retrieval_staff from staff) as retrieval_staff
                   on retrieval.staff_id = retrieval_staff.id
         left join (select item_name, staff_id from staff_handle_item where stage = 'IMPORT') as import
                   on item.name = import.item_name
         left join (select id, name from staff) as import_staff on import.staff_id = import_staff.id
         left join (select item_name, staff_id from staff_handle_item where stage = 'EXPORT') as export
                   on item.name = export.item_name
         left join (select id, name from staff) as export_staff on export.staff_id = export_staff.id
         left join (select item_name, staff_id from staff_handle_item where stage = 'DELIVERY') as delivery
                   on item.name = delivery.item_name
         left join (select id, name from staff) as delivery_staff on delivery.staff_id = delivery_staff.id;

select *
from item
         left join item_route on item.name = item_route.item_name and item_route.stage = 'DELIVERY'
         left join item_state on item.name = item_state.item_name
where name in ((select item_name
                from item_route
                where stage = 'DELIVERY'
                  and city_id in (select city_id from staff_city where staff_id = 11203255))
               intersect
               (select item_name
                from item_state
                where state = 'FROM_IMPORT_TRANSPORTING')
               except
               (select item_name
                from staff_handle_item
                where stage = 'DELIVERY'));

select *
from item
         left join item_state on item.name = item_state.item_name
         left join (select staff_id, item_name
                    from staff_handle_item
                    where stage = 'RETRIEVAL') as retrieval on item.name = retrieval.item_name
         left join (select staff_id, item_name
                    from staff_handle_item
                    where stage = 'IMPORT') as import on item.name = import.item_name
where name in ((select item_name
                from item_state
                where state in ('PICKING_UP', 'TO_EXPORT_TRANSPORTING')
                intersect
                select item_name
                from staff_handle_item
                where stage = 'RETRIEVAL'
                  and staff_id = 11203255)
               union
               (select item_name
                from item_state
                where state in ('FROM_IMPORT_TRANSPORTING', 'DELIVERING')
                intersect
                select item_name
                from staff_handle_item
                where stage = 'DELIVERY'
                  and staff_id = 11203255));

