-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (x86_64)
--
-- Host: localhost    Database: dsass
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `event_create`
--

DROP TABLE IF EXISTS `event_create`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_create` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Venue` varchar(255) NOT NULL,
  `Date` date NOT NULL,
  `Start_Time` time NOT NULL,
  `End_Time` time NOT NULL,
  `Duration_Hour` int NOT NULL,
  `Username` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_create`
--

LOCK TABLES `event_create` WRITE;
/*!40000 ALTER TABLE `event_create` DISABLE KEYS */;
INSERT INTO `event_create` VALUES (1,'Robot','Robotic','JayaOne','2024-06-16','09:00:00','15:00:00',6,'Ching Wen'),(4,'MedMath','Math Competition','DTC, UM','2024-05-21','09:00:00','13:00:00',4,'Ching Wen'),(6,'MedMath2','Mathematic Competition','KLCC','2024-05-28','13:00:00','20:00:00',7,'Ching Wen'),(7,'TechTalk','Technology','Auditorium Hall','2024-06-12','17:00:00','23:00:00',6,'ev'),(8,'Hackathon','Coding Marathon','FSKTM, UM','2024-06-30','08:00:00','22:00:00',14,'Ching Wen'),(9,'Science Fair','Showcase scientific project','Midvalley','2024-06-29','11:00:00','22:00:00',11,'Ching Wen'),(10,'Summer Project','Experiment, team project','UM','2024-06-09','10:00:00','19:00:00',9,'ev'),(11,'STEM Expo','Annual exhibition','Sunway Velocity','2024-06-23','10:00:00','22:00:00',12,'Ching Wen'),(13,'Stem Festival','50 booths','DTC','2024-05-25','10:00:00','22:00:00',12,'Ching Wen'),(14,'We','STEM Camp','DTC','2024-07-09','08:00:00','13:00:00',5,'Ching Wen'),(15,'Camp 5','STEM','UM','2024-05-13','08:00:00','16:00:00',8,'Ching Wen'),(16,'2024 Mag','STEM','UM','2024-07-28','08:00:00','10:00:00',2,'Ching Wen'),(21,'try','try','try','2024-06-28','10:00:00','13:00:00',3,'ev'),(22,'FutureEngineer ','Learn from Prof','DEWAN FPA','2024-06-12','11:00:00','15:00:00',4,'ev'),(23,'Festar','STEM Festival','KPS','2024-06-12','10:00:00','22:00:00',12,'ev'),(25,'FastTrend','Learn Technology','DTC','2024-06-13','11:00:00','15:00:00',4,'ev'),(26,'ScientistLife','Science','KK10','2024-06-16','08:00:00','19:00:00',11,'ev');
/*!40000 ALTER TABLE `event_create` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-12 16:02:51
