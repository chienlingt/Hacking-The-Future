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
-- Table structure for table `quiz_create`
--

DROP TABLE IF EXISTS `quiz_create`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz_create` (
  `Title` varchar(255) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Theme` varchar(255) NOT NULL,
  `Content` text NOT NULL,
  `Id` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz_create`
--

LOCK TABLES `quiz_create` WRITE;
/*!40000 ALTER TABLE `quiz_create` DISABLE KEYS */;
INSERT INTO `quiz_create` VALUES ('STEM','3rd grade level','Science','https://quizizz.com/admin/quiz/57a65c26d86a80ce22420d54?source=quiz_share',1,'ev'),('Test Your Math','8th grade level, 20 questions','Mathematics','https://quizizz.com/admin/quiz/6059f75f1744e0001b1f7b9f?source=quiz_share',2,'ev'),('Introduction to Technology','Computer','Technology','https://quizizz.com/admin/quiz/599dc93fcdb08b11006bfa22?source=quiz_share',3,'ev'),('Introduction to Engineering','4 questions','Engineering','https://quizizz.com/admin/quiz/576c2db00cc84368502b01bc?source=quiz_share',4,'ev'),('Engineering in Society','10 questions','Engineering','https://quizizz.com/admin/quiz/5d837c9562d492001bb965f6?source=quiz_share',5,'ev'),('STEM & TEKVO 2023','5th-12th grade','Mathematics','https://quizizz.com/admin/quiz/5e54b79dc82619001b3e092e?source=quiz_share',6,'ev'),('Test you tech','Technology vocabulary','Technology','https://quizizz.com/admin/quiz/5d073a1dd7b16a001b7239c8?source=quiz_share',7,'ev'),('Trial1','Trial1','Science','https://quizizz.com/admin/quiz/5d073a1dd7b16a001b7239c8?source=quiz_share',8,'evev123'),('What is Science','Basic Science','Science','https://science/q=com?source=65fdgrba8',9,'evev123'),('SC ','Science related','Science','https://quizizz.com/admin/quiz/57a65c26d86a80ce22420d54?source=quiz_share',10,'evev123'),('Math u crazy','AddMath Quiz','Mathematics','https://quizizz.com/admin/quiz/60b83ba798c041001bcda1b0?source=quiz_share',11,'evev123'),('Tech Saver','Be a technology saver','Technology','https://technologysaver.com',13,'ev'),('OldAndNew','6-8th grade','Science','https://quizizz.com/admin/quiz/5c17b9fbfcd696001aaa495e?source=quiz_share',15,'ls');
/*!40000 ALTER TABLE `quiz_create` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_quiz_create` AFTER INSERT ON `quiz_create` FOR EACH ROW BEGIN
    INSERT INTO quiz_status (`stu_name`, `quiz_id`, `status`, `completeTime`)
    SELECT u.Username, NEW.Id, '-', NULL
    FROM `dsass`.`user` u
    WHERE u.Role = 'student';
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `quiz_create_AFTER_DELETE` AFTER DELETE ON `quiz_create` FOR EACH ROW BEGIN
-- Delete old quiz_status records
    DELETE FROM quiz_status WHERE `quiz_id` = OLD.Id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-12 16:02:50
