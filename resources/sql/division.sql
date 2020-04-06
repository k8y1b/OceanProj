SELECT DISTINCT ai.species
FROM AnimalInhabits ai
WHERE NOT EXISTS (
(SELECT lat, lon FROM Location)
MINUS
(SELECT ah.lat, ah.lon FROM AnimalInhabits ah WHERE ai.species = ah.species ) )