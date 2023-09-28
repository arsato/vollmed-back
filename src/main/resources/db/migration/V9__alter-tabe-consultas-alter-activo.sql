ALTER TABLE consultas CHANGE COLUMN activo motivo_cancelamiento VARCHAR(30) NULL;
update consultas set motivo_cancelamiento = NULL;