alter table consultas add column activo tinyint;
update consultas set activo = 1;
alter table consultas modify activo tinyint not null;