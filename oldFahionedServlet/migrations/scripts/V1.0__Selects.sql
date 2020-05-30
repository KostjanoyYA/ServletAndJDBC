--Checking queries--------------------------------------------------------------------

--Cars with fullStateNumbers, carOwners with fine
DROP TABLE IF EXISTS temp_table;
CREATE TEMP TABLE temp_table AS
	SELECT stateNumber.id, substring(stateNumber.series, 1, 1) || lpad(stateNumber.number::varchar, 3, '0') || substring(stateNumber.series, 2) ||
    lpad(stateNumber.regionCode::varchar, 3, '0') || stateNumber.country AS fullNumber
	FROM stateNumber;

SELECT carOwner.lastName, carOwner.firstName, carOwner.middleName, car.make, car.model, temp_table.fullNumber, penaltyEvent.eventDate, fine.type, fine.charge
FROM penaltyEvent
JOIN car ON car.id = penaltyEvent.carID
JOIN fine ON fine.id = penaltyEvent.fineID
JOIN temp_table ON temp_table.id = car.stateNumberID
JOIN carOwner ON carOwner.id = car.carOwnerID
ORDER BY carOwner.lastName;

--Result queries-----------------------------------------------------
--Cars with stateNumbers, carOwners, fine
SELECT carOwner.lastName, carOwner.firstName, carOwner.middleName, car.make, car.model,
stateNumber.number, stateNumber.series, stateNumber.regionCode, stateNumber.country, penaltyEvent.eventDate, fine.type, fine.charge
FROM penaltyEvent
JOIN car ON car.id = penaltyEvent.carID
JOIN stateNumber ON stateNumber.id = car.stateNumberID
JOIN carOwner ON carOwner.id = car.carOwnerID
JOIN fine ON fine.id = penaltyEvent.fineID
ORDER BY carOwner.lastName;

--Getting top 5--
DROP TABLE IF EXISTS temp_table;
DROP TABLE IF EXISTS temp_count_table;
DROP TABLE IF EXISTS temp_rang_table;

CREATE TEMP TABLE temp_table AS
SELECT carOwner.lastName, carOwner.firstName, carOwner.middleName, car.make, car.model,
       stateNumber.number, stateNumber.series, stateNumber.regionCode, stateNumber.country,
       penaltyEvent.eventDate, fine.id as fine_id, fine.type, fine.charge
FROM penaltyEvent
         JOIN car ON car.id = penaltyEvent.carID
         JOIN stateNumber ON stateNumber.id = car.stateNumberID
         JOIN carOwner ON carOwner.id = car.carOwnerID
         JOIN fine ON fine.id = penaltyEvent.fineID
ORDER BY carOwner.lastName;

CREATE TEMP TABLE temp_count_table AS
SELECT temp_table.fine_id, COUNT(*) cnt
FROM temp_table
GROUP BY fine_id
ORDER BY cnt DESC;

CREATE TEMP TABLE temp_rang_table AS
select * from
    (select *, dense_rank() over (order by cnt desc) as fine_rank
     from temp_count_table) subquery
where fine_rank <= 5;

SELECT fine.id as fine_id, fine.type as fine_type,
       fine.charge as fine_charge, temp_rang_table.fine_rank as fine_top_place, temp_rang_table.cnt as fine_occurrences
FROM fine, temp_rang_table
WHERE fine.id = temp_rang_table.fine_id
ORDER BY temp_rang_table.fine_rank ASC;