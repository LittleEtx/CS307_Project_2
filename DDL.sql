create table Item(
    name varchar primary key ,
    price int
);
create table Item_Type(
     id int primary key ,
     item_name varchar references Item(name),
  type varchar primary key
);
create table Item_State(
     id int primary key ,
    item_name varchar references Item(name),
    state varchar primary key
);
create table Staff(
  id int primary key ,
  name varchar,
  gender varchar,
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
    area_code varchar unique,
    name varchar unique not null
);

create table Staff_City(
     id int primary key ,
    staff_id int references Staff(id),
    city_id int references City (id)
);

create table Staff_Company
(
     id int primary key ,
  staff_id int references Staff(id),
  company_id int references Company(id)
);

create table Ship
(
    id int primary key,
    name varchar unique not null ,
    company_id int references Company (id)
);

create table Ship_State(
    id int primary key ,
    ship_id int references Ship (id),
  state varchar primary key
);

create table Container(
    code varchar primary key,
    type varchar
);

create table Ship_Container(
     id int primary key ,
    container_code varchar references Container (code),
    ship_id int references Ship (id)
);

create table Container_Item
(
     id int primary key ,
    container_code varchar references Container (code),
    item_name varchar references Item(name)
);

create table Tax_Info
(
    item_type varchar references Item_Type(type),
    city_id int references City (id),
    primary key (item_type,city_id),
    export_tax decimal(30,15),
    import_tax decimal(30,15)
);

create table Transportation_Info(
    id int primary key ,
    staff_id int references Staff(id),
    city_id int references City(id),
    item_name varchar references Item(name),
    stage varchar
);

create table Verification(
    staff_id int primary key references Staff(id),
    authority varchar,
    password varchar
);

