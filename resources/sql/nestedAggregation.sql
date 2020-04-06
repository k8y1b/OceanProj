SELECT I.numBodyParts, AVG(A.speed)
FROM Invertebrate I JOIN Animal A on I.species = A.species
GROUP BY I.numBodyParts

--selects the animal with the greatest speed out of each category of "numBodyParts"
