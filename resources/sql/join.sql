Select species, name
FROM AnimalInhabits AI JOIN Location L
ON AI.lat = L.lat AND AI.lon = L.lon;