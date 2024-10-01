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
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `Role` varchar(45) NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `Email` varchar(45) DEFAULT NULL,
  `Username` varchar(45) NOT NULL,
  `Friends` varchar(255) DEFAULT NULL,
  `Friend_Requests` varchar(255) DEFAULT NULL,
  `sent_friends` varchar(255) DEFAULT NULL,
  `X` varchar(45) DEFAULT NULL,
  `Y` varchar(45) DEFAULT NULL,
  `Parent1` varchar(45) DEFAULT NULL,
  `Parent2` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('Student','Adam Tan','adam@gmail.com','Adamtan09','jason0319,hehe,','','','251','-175','TanChinPeng',NULL),('Student','ahmadfirdaus07','ahmad@gmail.com','ahmadfirdaus07',NULL,NULL,NULL,'-68','257','firdaus_an',NULL),('Student','giveYouup3','give@gmail.com','giveYouup3',NULL,NULL,NULL,'259','88','Nevergonna19',NULL),('Student','happy','happy@gmail.com','happy',NULL,NULL,NULL,'-448','-214','Lily',NULL),('Student','hehe','hehe@gmail.com','hehe','jason0319,Adamtan09,',NULL,'Laura_tan,','-8','467','TanChinPeng',NULL),('Student','Jason','jason0319@live.com','jason0319','Laura_tan,Adamtan09,hehe,','',NULL,'-96','131','TanChinPeng',NULL),('Student','katyln_doe','katyln@gmail.com','katyln_doe',NULL,NULL,NULL,'-165','-296','johndoe3698',NULL),('Student','Laura Tan','laura@gmail.com','Laura_tan','jason0319,','hehe,','','157','456','TanChinPeng',NULL),('Student','loremipsum96','lore@gmail.com','loremipsum96',NULL,NULL,NULL,'-102','294','noobmaster68',NULL),('Student','reelansantya','reel@gmail.com','reelansantya',NULL,NULL,NULL,'-99','-444','Santya24',NULL),('Student','Samadabdul','sama@gmail.com','Samadabdul',NULL,NULL,NULL,'-320','67','aliabdul10',NULL),('Student','yinjiadoe20','yin@gmail.com','yinjiadoe20',NULL,NULL,NULL,'-85','-193','johndoe3698',NULL),('Student','youyou','youyou@gmail.com','youyou',NULL,NULL,NULL,'128','126','Lily',NULL);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-12 16:02:50
