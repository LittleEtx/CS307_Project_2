SELECT *
FROM staff
         join verification on staff.id = verification.staff_id
         left join staff_company on staff.id = staff_company.staff_id
         left join staff_city on staff.id = staff_city.staff_id
WHERE name = 'Zang Cong'
  AND password = '582433470701600771';

SELECT *
FROM staff
         join verification on staff.id = verification.staff_id;



SELECT *
FROM tax_info;

select *
from item_state s
         left join item_container c on s.item_name = c.item_name
         left join item_ship i on s.item_name = i.item_name
where ship_name = 'f1d76a3a'
  and container_code = '632b8f09';

select *
from item_state s
         left join item_container c on s.item_name = c.item_name
         left join item_ship i on s.item_name = i.item_name
where container_code = '632b8f09';

select *
from item_state s
         left join item_ship i on s.item_name = i.item_name
         left join item_container c on s.item_name = c.item_name
where ship_name = 'f1d76a3a';

select company_id
from staff_handle_item
         left join staff_company sc on staff_handle_item.staff_id = sc.staff_id
where stage = 'RETRIEVAL'
  and item_name = 'coconut-39c7a';

SELECT id, name, max(export_rate) rate
FROM city
         left join tax_info on city.id = tax_info.city_id
group by id;
