SELECT I.numBodyParts, AVG(A.speed)
FROM Invertebrate I JOIN Animal A on I.species = A.species
GROUP BY I.numBodyParts