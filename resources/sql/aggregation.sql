SELECT AVG(Loc.temperature)
FROM Location Loc
WHERE Loc.depth < 100

--gets the average temperature of locations where the depth is less than 100 meters

Select species
FROM Animal
WHERE speed = MAX(speed);

--selects the name of the animal with the greatest speed
