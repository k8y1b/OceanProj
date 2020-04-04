
DROP TABLE PlantSunlight;
CREATE TABLE PlantSunlight (
color CHAR(20),
sunlight DOUBLE
);
DROP TABLE Animal;
CREATE TABLE Animal (
species CHAR(20) PRIMARY KEY,
speed INTEGER
);
DROP TABLE FishingMethod;
CREATE TABLE FishingMethod(
methodName CHAR(20) PRIMARY KEY,
annualNumberCaught INTEGER,
pricePerCatch DOUBLE,
);
DROP TABLE SingleCellOrganism;
CREATE TABLE SingleCellOrganism(
name CHAR(20) PRIMARY KEY,
reproductionMethod CHAR(20)
);
DROP TABLE Climate;
CREATE TABLE Climate(
temperature DOUBLE,
precipitation DOUBLE,
sunlight DOUBLE,
PRIMARY KEY (temperature,precipitation)
);
DROP TABLE Current;
CREATE TABLE Current(
id INTEGER,
time DOUBLE,
direction DOUBLE,
speed DOUBLE,
PRIMARY KEY (id)
);
DROP TABLE Invertebrate;
CREATE TABLE Invertebrate(
species CHAR(20) PRIMARY KEY,
numBodyParts INTEGER
);
DROP TABLE InvertebrateSoftness;
CREATE TABLE InvertebrateSoftness(
numBodyParts INTEGER PRIMARY KEY,
isSoft BOOLEAN
);
DROP TABLE Fish;
CREATE TABLE Fish(
species CHAR(20) PRIMARY KEY,
numFins INTEGEGER
);
DROP TABLE Mammal;
CREATE TABLE Mammal(
species CHAR(20) PRIMARY KEY,
holdBreathLength DOUBLE
);


DROP TABLE Location;
CREATE TABLE Location(
lat DOUBLE,
lon DOUBLE,
name CHAR(20),
depth DOUBLE,
temperature DOUBLE,
precipitation DOUBLE,
PRIMARY KEY (lat,lon),
FOREIGN KEY (temperature,precipitation) REFERENCES Climate
);
DROP TABLE AnimalEatsAnimal;
CREATE TABLE AnimalEatsAnimal(
species1 CHAR(20),
species2 CHAR(20),
FOREIGN KEY (species1) REFERENCES Animal(species),
FOREIGN KEY (species2) REFERENCES Animal(species),
PRIMARY KEY (species1, species2)
);
DROP TABLE Habitat;
CREATE TABLE Habitat(
sunlight DOUBLE,
depth DOUBLE,
lat DOUBLE,
lon DOUBLE,
FOREIGN KEY (lat,lon) REFERENCES Location
PRIMARY KEY (lat,lon,depth)
);
DROP TABLE Plant;
CREATE TABLE Plant (
name CHAR(20) PRIMARY KEY,
bloom CHAR(20),
color CHAR(20),
depth DOUBLE,
lat DOUBLE,
lon DOUBLE,
FOREIGN KEY (depth,lat,lon) REFERENCES Habitat
);
DROP TABLE AnimalEatsPlant;
CREATE TABLE AnimalEatsPlant(
species CHAR(20),
name CHAR(20),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (name) REFERENCES PLANT,
PRIMARY KEY (species, name)
);
DROP TABLE AnimalEatsSingleCell;
CREATE TABLE AnimalEatsSingleCell (
species CHAR (20),
name CHAR (20),
PRIMARY KEY (species, name),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (name) REFERENCES SingleCellOrganism
);
DROP TABLE AnimalInhabits;
CREATE TABLE AnimalInhabits (
species CHAR(20),
depth DOUBLE,
lat DOUBLE,
lon DOUBLE,
PRIMARY KEY (species, depth, lat, lon),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (depth,lat,lon) REFERENCES Habitat
);
DROP TABLE FishingMethodCatches;
CREATE TABLE FishingMethodCatches (
species CHAR(20),
methodName CHAR(20)
PRIMARY KEY (species, methodName),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (methodName) REFERENCES FishingMethod
);
DROP TABLE MethodUsedAt;
CREATE TABLE MethodUsedAt (
methodName CHAR(20)
lat DOUBLE,
lon DOUBLE,
PRIMARY KEY (methodName, lat, lon)
FOREIGN KEY (lat,lon) REFERENCES Location
FOREIGN KEY (methodName) references FishingMethod
);
DROP TABLE LocationHasCurrent;
CREATE TABLE LocationHasCurrent(
lat DOUBLE,
lon DOUBLE,
id CHAR(20),
PRIMARY KEY(lat, lon, id)
FOREIGN KEY (lat, lon) REFERENCES Location
FOREIGN KEY (id) REFERENCES Current
);


