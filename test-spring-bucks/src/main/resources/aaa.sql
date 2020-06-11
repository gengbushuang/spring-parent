create table t_menu (
    id bigint auto_increment,
    create_time timestamp,
    update_time timestamp,
    name varchar(255),
    price decimal(19,2),
    primary key (id)
)

create table t_order (
    id bigint auto_increment,
    create_time timestamp,
    update_time timestamp,
    customer varchar(255),
    state integer not null,
    primary key (id)
)

create table t_order_coffee (
    coffee_order_id bigint not null,
    items_id bigint not null
)

alter table t_order_coffee
add constraint FKj2swxd3y69u2tfvalju7sr07q
foreign key (items_id)
references t_menu

alter table t_order_coffee
add constraint FK33ucji9dx64fyog6g17blpx9v
foreign key (coffee_order_id)
references t_order