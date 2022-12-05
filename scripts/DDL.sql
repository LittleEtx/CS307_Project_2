create table Item_Type(
    type varchar primary key
);

create table Item(
    name varchar primary key,
    price int,
    class varchar references Item_Type(type)
);

create table Item_State(
    item_name varchar primary key references Item(name),
    state varchar
);

create table Staff(
    id int primary key ,
    name varchar not null,
    gender varchar,
    birth date,
    phone_number varchar unique
);

create table Company
(
    id int primary key ,
    name varchar unique not null
);

create table City
(
    id int primary key ,
    name varchar not null
);

create table Staff_City(
    staff_id int references Staff(id),
    city_id int references City (id),
    primary key (staff_id, city_id)
);

create table Staff_Company
(
    staff_id int references Staff(id),
    company_id int references Company(id),
    primary key (staff_id, company_id)
);

create table Ship
(
    id int primary key,
    name varchar not null ,
    company_id int references Company (id)
);

create table Ship_State(
    ship_id int primary key references Ship (id),
    state varchar
);

create table Container(
    code varchar primary key,
    type varchar
);

create table Ship_Container(
    container_code varchar primary key references Container (code),
    ship_id int references Ship (id),
    is_finished boolean
);

create table Container_Item
(
    container_code varchar references Container (code),
    item_name varchar primary key references Item(name),
    is_finished boolean
);

create table Tax_Info
(
    item_type varchar references Item_Type(type),
    city_id int references City (id),
    primary key (item_type,city_id),
    export_tax decimal(30,15),
    import_tax decimal(30,15)
);

create table Item_Route(
    item_name varchar  references Item(name),
    city_id int references City(id),
    stage varchar,
    primary key (item_name,stage)
);

create table Item_Courier(
    item_name varchar primary key references Item(name),
    staff_id int references Staff(id),
    is_finished boolean
);


create table Verification(
    staff_id int primary key references Staff(id),
    authority varchar,
    password varchar
);

