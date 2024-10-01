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
-- Table structure for table `quiz_status`
--

DROP TABLE IF EXISTS `quiz_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz_status` (
  `quizstatus_id` int NOT NULL AUTO_INCREMENT,
  `stu_name` varchar(45) NOT NULL,
  `quiz_id` int NOT NULL,
  `status` varchar(60) DEFAULT '-',
  `completeTime` datetime DEFAULT NULL,
  PRIMARY KEY (`quizstatus_id`),
  UNIQUE KEY `quizstatus_id_UNIQUE` (`quizstatus_id`),
  KEY `Username_idx` (`stu_name`),
  CONSTRAINT `QuizS_StuName` FOREIGN KEY (`stu_name`) REFERENCES `user` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=547 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz_status`
--

LOCK TABLES `quiz_status` WRITE;
/*!40000 ALTER TABLE `quiz_status` DISABLE KEYS */;
INSERT INTO `quiz_status` VALUES (316,'Adamtan09',1,'-',NULL),(317,'Adamtan09',2,'-',NULL),(318,'Adamtan09',3,'Completed','2024-06-12 14:43:52'),(319,'Adamtan09',4,'-',NULL),(320,'Adamtan09',5,'-',NULL),(321,'Adamtan09',6,'-',NULL),(322,'Adamtan09',7,'-',NULL),(323,'Adamtan09',8,'-',NULL),(324,'Adamtan09',9,'-',NULL),(325,'Adamtan09',10,'-',NULL),(326,'Adamtan09',11,'-',NULL),(327,'Adamtan09',13,'-',NULL),(331,'Laura_tan',1,'-',NULL),(332,'Laura_tan',2,'-',NULL),(333,'Laura_tan',3,'-',NULL),(334,'Laura_tan',4,'-',NULL),(335,'Laura_tan',5,'-',NULL),(336,'Laura_tan',6,'-',NULL),(337,'Laura_tan',7,'-',NULL),(338,'Laura_tan',8,'-',NULL),(339,'Laura_tan',9,'-',NULL),(340,'Laura_tan',10,'-',NULL),(341,'Laura_tan',11,'-',NULL),(342,'Laura_tan',13,'-',NULL),(346,'jason0319',1,'-',NULL),(347,'jason0319',2,'-',NULL),(348,'jason0319',3,'-',NULL),(349,'jason0319',4,'-',NULL),(350,'jason0319',5,'-',NULL),(351,'jason0319',6,'-',NULL),(352,'jason0319',7,'-',NULL),(353,'jason0319',8,'-',NULL),(354,'jason0319',9,'-',NULL),(355,'jason0319',10,'-',NULL),(356,'jason0319',11,'-',NULL),(357,'jason0319',13,'-',NULL),(379,'Adamtan09',15,'-',NULL),(380,'jason0319',15,'-',NULL),(381,'Laura_tan',15,'-',NULL),(382,'happy',1,'-',NULL),(383,'happy',2,'-',NULL),(384,'happy',3,'-',NULL),(385,'happy',4,'-',NULL),(386,'happy',5,'-',NULL),(387,'happy',6,'-',NULL),(388,'happy',7,'-',NULL),(389,'happy',8,'-',NULL),(390,'happy',9,'-',NULL),(391,'happy',10,'-',NULL),(392,'happy',11,'-',NULL),(393,'happy',13,'-',NULL),(394,'happy',15,'-',NULL),(397,'youyou',1,'-',NULL),(398,'youyou',2,'-',NULL),(399,'youyou',3,'-',NULL),(400,'youyou',4,'-',NULL),(401,'youyou',5,'-',NULL),(402,'youyou',6,'-',NULL),(403,'youyou',7,'-',NULL),(404,'youyou',8,'-',NULL),(405,'youyou',9,'-',NULL),(406,'youyou',10,'-',NULL),(407,'youyou',11,'-',NULL),(408,'youyou',13,'-',NULL),(409,'youyou',15,'-',NULL),(412,'hehe',1,'-',NULL),(413,'hehe',2,'-',NULL),(414,'hehe',3,'-',NULL),(415,'hehe',4,'-',NULL),(416,'hehe',5,'-',NULL),(417,'hehe',6,'-',NULL),(418,'hehe',7,'-',NULL),(419,'hehe',8,'-',NULL),(420,'hehe',9,'-',NULL),(421,'hehe',10,'-',NULL),(422,'hehe',11,'-',NULL),(423,'hehe',13,'-',NULL),(424,'hehe',15,'-',NULL),(427,'ahmadfirdaus07',1,'-',NULL),(428,'ahmadfirdaus07',2,'-',NULL),(429,'ahmadfirdaus07',3,'-',NULL),(430,'ahmadfirdaus07',4,'-',NULL),(431,'ahmadfirdaus07',5,'-',NULL),(432,'ahmadfirdaus07',6,'-',NULL),(433,'ahmadfirdaus07',7,'-',NULL),(434,'ahmadfirdaus07',8,'-',NULL),(435,'ahmadfirdaus07',9,'-',NULL),(436,'ahmadfirdaus07',10,'-',NULL),(437,'ahmadfirdaus07',11,'-',NULL),(438,'ahmadfirdaus07',13,'-',NULL),(439,'ahmadfirdaus07',15,'-',NULL),(442,'reelansantya',1,'-',NULL),(443,'reelansantya',2,'-',NULL),(444,'reelansantya',3,'-',NULL),(445,'reelansantya',4,'-',NULL),(446,'reelansantya',5,'-',NULL),(447,'reelansantya',6,'-',NULL),(448,'reelansantya',7,'-',NULL),(449,'reelansantya',8,'-',NULL),(450,'reelansantya',9,'-',NULL),(451,'reelansantya',10,'-',NULL),(452,'reelansantya',11,'-',NULL),(453,'reelansantya',13,'-',NULL),(454,'reelansantya',15,'-',NULL),(457,'loremipsum96',1,'-',NULL),(458,'loremipsum96',2,'-',NULL),(459,'loremipsum96',3,'-',NULL),(460,'loremipsum96',4,'-',NULL),(461,'loremipsum96',5,'-',NULL),(462,'loremipsum96',6,'-',NULL),(463,'loremipsum96',7,'-',NULL),(464,'loremipsum96',8,'-',NULL),(465,'loremipsum96',9,'-',NULL),(466,'loremipsum96',10,'-',NULL),(467,'loremipsum96',11,'-',NULL),(468,'loremipsum96',13,'-',NULL),(469,'loremipsum96',15,'-',NULL),(487,'yinjiadoe20',1,'-',NULL),(488,'yinjiadoe20',2,'-',NULL),(489,'yinjiadoe20',3,'-',NULL),(490,'yinjiadoe20',4,'-',NULL),(491,'yinjiadoe20',5,'-',NULL),(492,'yinjiadoe20',6,'-',NULL),(493,'yinjiadoe20',7,'-',NULL),(494,'yinjiadoe20',8,'-',NULL),(495,'yinjiadoe20',9,'-',NULL),(496,'yinjiadoe20',10,'-',NULL),(497,'yinjiadoe20',11,'-',NULL),(498,'yinjiadoe20',13,'-',NULL),(499,'yinjiadoe20',15,'-',NULL),(502,'katyln_doe',1,'-',NULL),(503,'katyln_doe',2,'-',NULL),(504,'katyln_doe',3,'-',NULL),(505,'katyln_doe',4,'-',NULL),(506,'katyln_doe',5,'-',NULL),(507,'katyln_doe',6,'-',NULL),(508,'katyln_doe',7,'-',NULL),(509,'katyln_doe',8,'-',NULL),(510,'katyln_doe',9,'-',NULL),(511,'katyln_doe',10,'-',NULL),(512,'katyln_doe',11,'-',NULL),(513,'katyln_doe',13,'-',NULL),(514,'katyln_doe',15,'-',NULL),(517,'Samadabdul',1,'-',NULL),(518,'Samadabdul',2,'-',NULL),(519,'Samadabdul',3,'-',NULL),(520,'Samadabdul',4,'-',NULL),(521,'Samadabdul',5,'-',NULL),(522,'Samadabdul',6,'-',NULL),(523,'Samadabdul',7,'-',NULL),(524,'Samadabdul',8,'-',NULL),(525,'Samadabdul',9,'-',NULL),(526,'Samadabdul',10,'-',NULL),(527,'Samadabdul',11,'-',NULL),(528,'Samadabdul',13,'-',NULL),(529,'Samadabdul',15,'-',NULL),(532,'giveYouup3',1,'-',NULL),(533,'giveYouup3',2,'-',NULL),(534,'giveYouup3',3,'-',NULL),(535,'giveYouup3',4,'-',NULL),(536,'giveYouup3',5,'-',NULL),(537,'giveYouup3',6,'-',NULL),(538,'giveYouup3',7,'-',NULL),(539,'giveYouup3',8,'-',NULL),(540,'giveYouup3',9,'-',NULL),(541,'giveYouup3',10,'-',NULL),(542,'giveYouup3',11,'-',NULL),(543,'giveYouup3',13,'-',NULL),(544,'giveYouup3',15,'-',NULL);
/*!40000 ALTER TABLE `quiz_status` ENABLE KEYS */;
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
