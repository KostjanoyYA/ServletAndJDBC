create schema if not exists pdbs;
SET search_path TO pdbs;
create table carOwner
(
    id               bigserial     not null,
    firstName        varchar(256)  not null,
    middleName       varchar(256)  not null,
    lastName         varchar(256)  not null
);

alter table carOwner
    add constraint carOwner_pk primary key (id);

create table stateNumber
(
    id              bigserial     not null,
    country         varchar(3)	  DEFAULT 'RUS'    not null,
    regionCode      int		      not null,
    series          varchar(3)    not null,
    number          int           not null
);

alter table stateNumber
    add constraint stateNumber_pk primary key (id);

create table car
(
    id             bigserial     not null,
    make           varchar(256)  not null,
    model          varchar(256)  not null,
    stateNumberID  bigint        not null,
    carOwnerID     bigint        not null
);

alter table car
    add constraint car_pk primary key (id);

alter table car
    add constraint car_stateNumber_id_fk foreign key (stateNumberID) references stateNumber (id);

alter table car
    add constraint car_carOwner_id_fk foreign key (carOwnerID) references carOwner (id);


create table fine
(
    id              bigserial      not null,
    type            varchar        not null,
    charge          numeric(9, 2)
);

alter table fine
    add constraint fine_pk primary key (id);

    create table penaltyEvent
    (
        id              bigserial      not null,
        eventDate       timestamp      not null,
        fineID          bigint         not null,
        carID           bigint         not null
    );

    alter table penaltyEvent
        add constraint penaltyEvent_pk primary key (id);

    alter table penaltyEvent
        add constraint penaltyEvent_fine_id_fk foreign key (fineID) references fine (id);

    alter table penaltyEvent
        add constraint penaltyEvent_car_id_fk foreign key (carID) references car (id);

--Table fulfilment

INSERT INTO fine (type, charge)
VALUES ('Driving under the influence of alcohol', 30000);

INSERT INTO fine (type, charge)
VALUES ('Driving without seat belt on', 1000);

INSERT INTO fine (type, charge)
VALUES ('Speeding', 500);

INSERT INTO fine (type, charge)
VALUES ('Violation of child transportation requirements', 5000);

INSERT INTO fine (type, charge)
VALUES ('Stop line crossing', 2000);

INSERT INTO fine (type, charge)
VALUES ('Double solid crossing', 2500);

INSERT INTO fine (type, charge)
VALUES ('Resistance to a traffic police officer', 150000);

--------------------------------------------------------------------

INSERT INTO carOwner (firstName, middleName,lastName)
VALUES ('JORDAN', 'ROSS', 'BELFORT');

INSERT INTO carOwner (firstName, middleName,lastName)
VALUES ('IPPOLIT', 'GEORGIEVICH', 'YAKOVLEV');

INSERT INTO carOwner (firstName, middleName,lastName)
VALUES ('FLASH', 'BLITZ', 'SLOTHMORE');

INSERT INTO carOwner (firstName, middleName,lastName)
VALUES ('BEN', 'FANTASTIC', 'CASH');

INSERT INTO carOwner (firstName, middleName,lastName)
VALUES ('NIKOLAY', 'EVGRAFOVICH', 'PETROV');

--------------------------------------------------------------------

INSERT INTO stateNumber (country, regionCode, series, number)
VALUES ('RUS', 75, 'MMM', 402);

INSERT INTO stateNumber (country, regionCode, series, number)
VALUES ('RUS', 70, 'OPC', 503);

INSERT INTO stateNumber (country, regionCode, series, number)
VALUES ('RUS', 39, 'PPK', 604);

INSERT INTO stateNumber (country, regionCode, series, number)
VALUES ('RUS', 17, 'CKC', 705);

INSERT INTO stateNumber (country, regionCode, series, number)
VALUES ('RUS', 117, 'XXX', 806);

INSERT INTO stateNumber (country, regionCode, series, number)
VALUES ('RUS', 5, 'OPC', 907);

--------------------------------------------------------------------

INSERT INTO car (make, model, stateNumberID, carOwnerID)
VALUES ('LAMBORGHINI', 'COUNTACH', 1, 1);

INSERT INTO car (make, model, stateNumberID, carOwnerID)
VALUES ('VAZ', '2101', 2, 2);

INSERT INTO car (make, model, stateNumberID, carOwnerID)
VALUES ('FERRARI', '550 MARANELLO', 3, 3);

INSERT INTO car (make, model, stateNumberID, carOwnerID)
VALUES ('THOMAS BUILT', 'SAF-T-LINER', 4, 4);

INSERT INTO car (make, model, stateNumberID, carOwnerID)
VALUES ('KIA', 'RIO', 5, 5);

INSERT INTO car (make, model, stateNumberID, carOwnerID)
VALUES ('LADA', 'PRIORA', 6, 5);

--------------------------------------------------------------------

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 00:00:00', 1, 1);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-07 00:00:00', 6, 2);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2019-12-31 20:20:20', 7, 1);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2019-12-31 23:23:23', 2, 1);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 00:00:15', 3, 1);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 02:11:00', 2, 2);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 11:14:00', 3, 3);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 20:20:20', 4, 4);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 07:00:00', 3, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-15 10:10:10', 5, 4);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-16 08:00:00', 5, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 08:28:00', 4, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 08:29:00', 4, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 08:30:00', 4, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 09:00:00', 4, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 13:12:00', 1, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-02-07 15:00:00', 2, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-10 17:32:15', 3, 6);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-01 09:10:00', 3, 5);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-02 13:12:00', 1, 1);

INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-03 15:15:00', 2, 2);
--
INSERT INTO penaltyEvent (eventDate, fineID, carID)
VALUES ('2020-01-15 12:10:10', 6, 4);

