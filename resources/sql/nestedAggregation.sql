SELECT MAX(speed), species
FROM Invertebrate JOIN Animal A on Invertebrate.species = A.species
GROUP BY numBodyParts;