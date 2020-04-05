SELECT species FROM AnimalInhabits as ai
WHERE NOT EXISTS (
(SELECT lat, lon FROM Location)
EXCEPT
(SELECT ah.lat, ah.lon FROM  AnimalInhabits as ah WHERE ai.lat = ah.lat AND ai.lon = ah.lon ) );