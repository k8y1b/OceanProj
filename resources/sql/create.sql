CREATE TABLE PlantSunlight (
color CHAR(31),
sunlight NUMBER(10,2)
);
CREATE TABLE Animal (
species CHAR(31) PRIMARY KEY,
speed INTEGER
);
CREATE TABLE FishingMethod(
methodName CHAR(31) PRIMARY KEY,
annualNumberCaught INTEGER,
pricePerCatch NUMBER(10,2)
);
CREATE TABLE SingleCellOrganism(
name CHAR(31) PRIMARY KEY,
reproductionMethod CHAR(31)
);
CREATE TABLE Climate(
temperature NUMBER(10,2),
precipitation NUMBER(10,2),
sunlight NUMBER(10,2),
PRIMARY KEY (temperature,precipitation)
);
CREATE TABLE "CURRENT"(
id INTEGER,
time TIMESTAMP,
direction CHAR(15),
speed NUMBER(10,2),
PRIMARY KEY (id)
);
CREATE TABLE Invertebrate(
species CHAR(31) PRIMARY KEY,
numBodyParts INTEGER
);
CREATE TABLE InvertebrateSoftness(
numBodyParts INTEGER PRIMARY KEY,
isSoft NUMBER(1) DEFAULT 0 NOT NULL
);
CREATE TABLE Fish(
species CHAR(31) PRIMARY KEY,
numFins INTEGER
);
CREATE TABLE Mammal(
species CHAR(31) PRIMARY KEY,
holdBreathLength NUMBER(10,2)
);
CREATE TABLE Location(
lat NUMBER(10,2),
lon NUMBER(10,2),
name CHAR(31),
depth NUMBER(10,2),
temperature NUMBER(10,2),
precipitation NUMBER(10,2),
PRIMARY KEY (lat,lon),
FOREIGN KEY (temperature,precipitation) REFERENCES Climate
);
CREATE TABLE AnimalEatsAnimal(
species1 CHAR(31),
species2 CHAR(31),
FOREIGN KEY (species1) REFERENCES Animal(species),
FOREIGN KEY (species2) REFERENCES Animal(species),
PRIMARY KEY (species1, species2)
);
CREATE TABLE Habitat(
sunlight NUMBER(10,2),
depth NUMBER(10,2),
lat NUMBER(10,2),
lon NUMBER(10,2),
FOREIGN KEY (lat,lon) REFERENCES Location,
PRIMARY KEY (lat,lon,depth)
);
CREATE TABLE Plant (
name CHAR(31) PRIMARY KEY,
bloom CHAR(31),
color CHAR(31),
depth NUMBER(10,2),
lat NUMBER(10,2),
lon NUMBER(10,2),
FOREIGN KEY (lat,lon,depth) REFERENCES Habitat
);
CREATE TABLE AnimalEatsPlant(
species CHAR(31),
name CHAR(31),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (name) REFERENCES PLANT,
PRIMARY KEY (species, name)
);
CREATE TABLE AnimalEatsSingleCell (
species CHAR(31),
name CHAR(31),
PRIMARY KEY (species, name),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (name) REFERENCES SingleCellOrganism
);
CREATE TABLE AnimalInhabits (
species CHAR(31),
depth NUMBER(10,2),
lat NUMBER(10,2),
lon NUMBER(10,2),
PRIMARY KEY (species, depth, lat, lon),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (lat,lon,depth) REFERENCES Habitat
);
CREATE TABLE FishingMethodCatches (
species CHAR(31),
methodName CHAR(31),
PRIMARY KEY (species, methodName),
FOREIGN KEY (species) REFERENCES Animal,
FOREIGN KEY (methodName) REFERENCES FishingMethod
);
CREATE TABLE MethodUsedAt (
methodName CHAR(31),
lat NUMBER(10,2),
lon NUMBER(10,2),
PRIMARY KEY (methodName, lat, lon),
FOREIGN KEY (lat,lon) REFERENCES Location,
FOREIGN KEY (methodName) references FishingMethod
);
CREATE TABLE LocationHasCurrent(
lat NUMBER(10,2),
lon NUMBER(10,2),
id INTEGER,
PRIMARY KEY(lat, lon, id),
FOREIGN KEY (lat, lon) REFERENCES Location,
FOREIGN KEY (id) REFERENCES "CURRENT"
);

INSERT INTO Climate VALUES (10.5, 1757, 0.24);
INSERT INTO Climate VALUES (11, 1649, 0.27);
INSERT INTO Climate VALUES (11.5, 1804, 0.18);
INSERT INTO Climate VALUES (10.5, 1745, 0.30);
INSERT INTO Climate VALUES (10.2, 1131, 0.25);

INSERT INTO Location VALUES (-123.2498, 49.4593, 'Lions Bay', 350, 10.5, 1757);
INSERT INTO Location VALUES (-124.1748, 49.7796, 'Saltery Bay', 80, 11, 1649);
INSERT INTO Location VALUES (-124.4217, 48.5644, 'Port San Juan', 35, 11.5, 1804);
INSERT INTO Location VALUES (-123.5909, 49.0133, 'Porlier Pass', 40, 10.5, 1745);
INSERT INTO Location VALUES (-123.8930, 49.7367, 'Skookumchuck Narrows', 125, 10.2, 1131);


INSERT INTO "CURRENT" VALUES (1, (TO_TIMESTAMP('2000-01-01 04:00:00.00', 'YYYY-MM-DD HH24:MI:SS.FF')), 'NW', 5.1);
INSERT INTO "CURRENT" VALUES (2, (TO_TIMESTAMP('2000-01-01 09:15:00.00', 'YYYY-MM-DD HH24:MI:SS.FF')), 'SE', 10);
INSERT INTO "CURRENT" VALUES (3, (TO_TIMESTAMP('2000-01-01 16:06:00.00', 'YYYY-MM-DD HH24:MI:SS.FF')), 'NW', 8.6);
INSERT INTO "CURRENT" VALUES (4, (TO_TIMESTAMP('2000-01-01 23:16:00.00', 'YYYY-MM-DD HH24:MI:SS.FF')), 'SE', 6.1);
INSERT INTO "CURRENT" VALUES (5, (TO_TIMESTAMP('2000-01-01 04:11:00.00', 'YYYY-MM-DD HH24:MI:SS.FF')), 'NW', 3.3);

INSERT INTO LocationHasCurrent VALUES (-123.2498, 49.4593, 1);
INSERT INTO LocationHasCurrent VALUES (-124.1748, 49.7796, 1);
INSERT INTO LocationHasCurrent VALUES (-124.4217, 48.5644, 3);
INSERT INTO LocationHasCurrent VALUES (-123.5909, 49.0133, 4);
INSERT INTO LocationHasCurrent VALUES (-123.8930, 49.7367, 5);

INSERT INTO Habitat VALUES (5000, 350, -123.2498, 49.4593);
INSERT INTO Habitat VALUES (40000, 80, -124.1748, 49.7796);
INSERT INTO Habitat VALUES (64000, 0, -124.4217, 48.5644);
INSERT INTO Habitat VALUES (17500, 15, -123.5909, 49.0133);
INSERT INTO Habitat VALUES (20000, 125, -123.8930, 49.7367);

INSERT INTO PlantSunlight VALUES ('yellow', 98000);
INSERT INTO PlantSunlight VALUES ('green', 70000);
INSERT INTO PlantSunlight VALUES ('dark-green', 64000);
INSERT INTO PlantSunlight VALUES ('green-brown', 40000);
INSERT INTO PlantSunlight VALUES ('brown', 20000);

INSERT INTO Plant VALUES ('Water Plantain', '3 white/pink petals', 'green', 0, -124.4217, 48.5644);
INSERT INTO Plant VALUES ('Skunk Cabbage', 'Dense yellow spadix', 'green', 0, -124.4217, 48.5644);
INSERT INTO Plant (name, color, depth, lat, lon) VALUES ('Serrulated Surfgrass', 'green-brown', 15, -123.5909, 49.0133);
INSERT INTO Plant (name, color, depth, lat, lon) VALUES ('Eelgrass', 'dark-green', 15, -123.5909, 49.0133);
INSERT INTO Plant (name, color, depth, lat, lon) VALUES ('Giant Kelp', 'brown', 80, -124.1748, 49.7796);

INSERT INTO Animal VALUES ('Pacific Oyster', 0);
INSERT INTO Animal VALUES ('Geoduck', 0);
INSERT INTO Animal VALUES ('King Crab', 11.1);
INSERT INTO Animal VALUES ('California Spiny Lobster', 9);
INSERT INTO Animal VALUES ('Pacific Giant Octopus', 40);
INSERT INTO Animal VALUES ('Chinook Salmon', 1.8);
INSERT INTO Animal VALUES ('Coho Salmon', 2.1);
INSERT INTO Animal VALUES ('Rockfish', 2.5);
INSERT INTO Animal VALUES ('Lingcod', 1.9);
INSERT INTO Animal VALUES ('Herring', 3.9);
INSERT INTO Animal VALUES ('Harbour Seal', 11);
INSERT INTO Animal VALUES ('California Sea Lion', 9.6);
INSERT INTO Animal VALUES ('Pacific Dolphin', 15);
INSERT INTO Animal VALUES ('Grey Whale', 7);
INSERT INTO Animal VALUES ('Orca', 7.2);
INSERT INTO Animal VALUES ('Zooplankton', 3.5);

INSERT INTO FishingMethod VALUES ('Mooching', 600, 75);
INSERT INTO FishingMethod VALUES ('Gillnetting', 10000, 20);
INSERT INTO FishingMethod VALUES ('Bottom Fishing', 500, 180);
INSERT INTO FishingMethod VALUES ('Trawling', 8500, 10);
INSERT INTO FishingMethod VALUES ('Downrigging', 1000, 70);

INSERT INTO SingleCellOrganism VALUES ('Cyanobacteria', 'Binary Fission');
INSERT INTO SingleCellOrganism VALUES ('Hyalosphenia', 'Asexual or Sexual');
INSERT INTO SingleCellOrganism VALUES ('Red Algae', 'Alternation of Generations');
INSERT INTO SingleCellOrganism VALUES ('Brown Algae', 'Alternation of Generations');
INSERT INTO SingleCellOrganism VALUES ('Green Algae', 'Alternation of Generations');

INSERT INTO Invertebrate VALUES ('Pacific Oyster', 2);
INSERT INTO Invertebrate VALUES ('Geoduck', 2);
INSERT INTO Invertebrate VALUES ('King Crab', 3);
INSERT INTO Invertebrate VALUES ('California Spiny Lobster', 14);
INSERt INTO Invertebrate VALUES ('Pacific Giant Octopus', 1);

INSERT INTO InvertebrateSoftness VALUES (2, 0);
INSERT INTO InvertebrateSoftness VALUES (4, 0);
INSERT INTO InvertebrateSoftness VALUES (3, 0);
INSERT INTO InvertebrateSoftness VALUES (14, 0);
INSERT INTO InvertebrateSoftness VALUES (1, 1);

INSERT INTO Fish VALUES ('Chinook Salmon', 8);
INSERT INTO Fish VALUES ('Coho Salmon', 8);
INSERT INTO Fish VALUES ('Rockfish', 9);
INSERT INTO Fish VALUES ('Lingcod', 6);
INSERT INTO Fish VALUES ('Herring', 8);

INSERT INTO Mammal VALUES ('Harbour Seal', 3);
INSERT INTO Mammal VALUES ('California Sea Lion', 10);
INSERT INTO Mammal VALUES ('Pacific Dolphin', 8);
INSERT INTO Mammal VALUES ('Grey Whale', 4);
INSERT INTO Mammal VALUES ('Orca', 12);

INSERT INTO AnimalEatsAnimal VALUES ('Orca', 'California Sea Lion');
INSERT INTO AnimalEatsAnimal VALUES ('California Sea Lion', 'Coho Salmon');
INSERT INTO AnimalEatsAnimal VALUES ('Orca', 'Chinook Salmon');
INSERT INTO AnimalEatsAnimal VALUES ('Chinook Salmon', 'Herring');
INSERT INTO AnimalEatsAnimal VALUES ('Pacific Giant Octopus', 'King Crab');

INSERT INTO AnimalEatsPlant VALUES ('Chinook Salmon', 'Serrulated Surfgrass');
INSERT INTO AnimalEatsPlant VALUES ('Chinook Salmon', 'Eelgrass');
INSERT INTO AnimalEatsPlant VALUES ('Chinook Salmon', 'Giant Kelp');
INSERT INTO AnimalEatsPlant VALUES ('King Crab', 'Giant Kelp');
INSERT INTO AnimalEatsPlant VALUES ('Coho Salmon', 'Eelgrass');

INSERT INTO AnimalEatsSingleCell VALUES ('Geoduck', 'Cyanobacteria');
INSERT INTO AnimalEatsSingleCell VALUES ('Coho Salmon', 'Brown Algae');
INSERT INTO AnimalEatsSingleCell VALUES ('Geoduck', 'Red Algae');
INSERT INTO AnimalEatsSingleCell VALUES ('Herring', 'Green Algae');
INSERT INTO AnimalEatsSingleCell VALUES ('Zooplankton', 'Cyanobacteria');

INSERT INTO AnimalInhabits VALUES ('King Crab', 350, -123.2498, 49.4593);
INSERT INTO AnimalInhabits VALUES ('California Spiny Lobster', 125, -123.8930, 49.7367);
INSERT INTO AnimalInhabits VALUES ('Chinook Salmon', 80, -124.1748, 49.7796);
INSERT INTO AnimalInhabits VALUES ('Coho Salmon', 80, -124.1748, 49.7796);
INSERT INTO AnimalInhabits VALUES ('Harbour Seal', 0, -124.4217, 48.5644);

INSERT INTO FishingMethodCatches VALUES ('Coho Salmon', 'Mooching');
INSERT INTO FishingMethodCatches VALUES ('Chinook Salmon', 'Mooching');
INSERT INTO FishingMethodCatches VALUES ('Chinook Salmon', 'Gillnetting');
INSERT INTO FishingMethodCatches VALUES ('Herring', 'Trawling');
INSERT INTO FishingMethodCatches VALUES ('Herring', 'Downrigging');

INSERT INTO MethodUsedAt VALUES ('Mooching', -123.2498, 49.4593);
INSERT INTO MethodUsedAt VALUES ('Gillnetting', -124.1748, 49.7796);
INSERT INTO MethodUsedAt VALUES ('Bottom Fishing', -124.4217, 48.5644);
INSERT INTO MethodUsedAt VALUES ('Trawling', -123.5909, 49.0133);
INSERT INTO MethodUsedAt VALUES ('Downrigging', -123.8930, 49.7367);
