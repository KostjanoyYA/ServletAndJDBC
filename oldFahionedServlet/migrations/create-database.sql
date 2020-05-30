create user pdbs with
	password '1';

--drop database penaltydb;

CREATE DATABASE penaltydb
    WITH
    OWNER = pdbs
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;