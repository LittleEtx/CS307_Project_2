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