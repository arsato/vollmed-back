create table medicos(
    id bigint not null auto_increment,
    nombre varchar(255) not null,
    email varchar(255) not null unique,
    documento varchar(255) not null unique,
    especialidad varchar(255) not null,
    calle varchar(255) not null,
    distrito varchar(255) not null,
    complemento varchar(255),
    numero varchar(20),
    ciudad varchar(255) not null,

    primary key (id)
)