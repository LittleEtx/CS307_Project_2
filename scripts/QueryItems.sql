select *
from (select name, price, class, state
      from item
               left join item_state on item.name = item_state.item_name) as item_state
         natural join (select retrieval.item_name name,
                              retrieval.city_id   retrieval_city,
                              import.city_id      import_city,
                              export.city_id      export_city,
                              delivery.city_id    delivery_city
                       from (select item_name, city_id from item_route where stage = 'RETRIEVAL') as retrieval
                                left join (select item_name, city_id from item_route where stage = 'IMPORT') as import
                                          on retrieval.item_name = import.item_name
                                left join (select item_name, city_id from item_route where stage = 'EXPORT') as export
                                          on retrieval.item_name = export.item_name
                                left join (select item_name, city_id from item_route where stage = 'DELIVERY') as delivery
                                          on delivery.item_name = retrieval.item_name) as item_city
         left join (select retrieval.item_name name,
                           retrieval.staff_id  retrieval_city,
                           import.staff_id     import_city,
                           export.staff_id     export_city,
                           delivery.staff_id   delivery_city
                    from (select item_name, staff_id from staff_handle_item where stage = 'RETRIEVAL') as retrieval
                             left join (select item_name, staff_id
                                        from staff_handle_item
                                        where stage = 'IMPORT') as import
                                       on retrieval.item_name = import.item_name
                             left join (select item_name, staff_id
                                        from staff_handle_item
                                        where stage = 'EXPORT') as export
                                       on retrieval.item_name = export.item_name
                             left join (select item_name, staff_id
                                        from staff_handle_item
                                        where stage = 'DELIVERY') as delivery
                                       on delivery.item_name = retrieval.item_name) as handle_item
                   on item_city.name = handle_item.name;