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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `Role` varchar(45) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Email` varchar(45) NOT NULL,
  `Username` varchar(45) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Confirm` varchar(100) NOT NULL,
  `X` int NOT NULL,
  `Y` int NOT NULL,
  `Parent1` varchar(45) DEFAULT NULL,
  `Parent2` varchar(45) DEFAULT NULL,
  `Child1` varchar(45) DEFAULT NULL,
  `Child2` varchar(45) DEFAULT NULL,
  `Child3` varchar(45) DEFAULT NULL,
  `Child4` varchar(45) DEFAULT NULL,
  `Child5` varchar(45) DEFAULT NULL,
  `Child6` varchar(45) DEFAULT NULL,
  `Child7` varchar(45) DEFAULT NULL,
  `Child8` varchar(45) DEFAULT NULL,
  `Child9` varchar(45) DEFAULT NULL,
  `Child10` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Email`,`Username`),
  UNIQUE KEY `Email_UNIQUE` (`Email`),
  UNIQUE KEY `Username_UNIQUE` (`Username`),
  KEY `idx_Username` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('Student','Adam Tan','adam@gmail.com','Adamtan09','$2a$10$JCS0/z952.t6nplPJ/5n5eAmHntOaCc9sH10O7WLOV2qIcDoq7UXy','Adamtan09',251,-175,'TanChinPeng',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','ahmadfirdaus07','ahmad@gmail.com','ahmadfirdaus07','$2a$10$3YTV4bA0u26T3mLnPrTZ6e0jQnvwSeVRhLiIf1gm8vcflniEB5fGS','123',-68,257,'firdaus_an',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','aliabdul10','ali@gmail.com','aliabdul10','$2a$10$eqW10.tqszbNiNFf7JMVNONGfuxHAtV2D31gKWlp81Lb0zftb7iQe','234',-79,-84,NULL,NULL,'Samadabdul',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Educator','Boo Ee Vone','booee@gmail.com','ev','$2a$10$CmSImftuGVZtrBg.e7QKb..JnrrPq5Q.SRcsQ3STxBMaJN3DzE2Ry','evev123',339,-396,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Educator','Tan Ching Wen','chingwen@hotmail.com','Ching Wen','$2a$10$59rlFNP33zgMa6mdPs8WO.SASCY0sBGCZgPlQfeBstoIzQfGtOUO.','123',257,-440,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','firdaus_an','firdaus@gmail.com','firdaus_an','$2a$10$aWunT1Z8ecdKfm3hllEfOeL5RwXdepmEuhB2CNEKcpU3XI7NwpBdW','123',176,381,NULL,NULL,'ahmadfirdaus07',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','giveYouup3','give@gmail.com','giveYouup3','$2a$10$SLifjs85PriQoFFxV8xy1uCXFLppWQG12K31cH/eWohrbDjO6u5O6','234',259,88,'Nevergonna19',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','happy','happy@gmail.com','happy','$2a$10$xVNxBUjb7evzloLXKOdRIOMemsDMMcexnE8IOVSoxX.saJg4IyFMy','happy',-448,-214,'Lily',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','hehe','hehe@gmail.com','hehe','$2a$10$veaOuygHIWLmERUvl4u4jOFVF1rjSXp11IYVhdWFfFflRffggJ6Yy','hehe',-8,467,'TanChinPeng',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','Jason','jason0319@live.com','jason0319','$2a$10$4ffMlmDiLyx.E.Bx5HYrHOO.hMuoz7e8GPOYWGBkFuyNcFWuBx042','jason0319',-96,131,'TanChinPeng',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','johndoe3698','john@gmail.com','johndoe3698','$2a$10$R1zYHMKo3X1aaxJ34sFDg.KNCpfD1gykGSCqXgeOINpxZ4VxZfLBe','234',-62,18,NULL,NULL,'yinjiadoe20',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','katyln_doe','katyln@gmail.com','katyln_doe','$2a$10$77rtgSvDFjWP6Uj3nCzv5OUSjyZQaN4n276NSMgTy5RL1QYso/jAu','234',-165,-296,'johndoe3698',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','Laura Tan','laura@gmail.com','Laura_tan','$2a$10$rffJbor6YU2RsVa0bQ7IkOFse3MXotPMLMCVuRVLgeaKnudk636yW','Laura_tan',157,456,'TanChinPeng',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','Lily','lily@gmail.com','Lily','$2a$10$9t4MdfguxaPM3qXQCmhW0OGoOamaGQ/aRH2gHfZsUu0YtULTxzSCu','Lily',89,-274,NULL,NULL,'happy',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','loremipsum96','lore@gmail.com','loremipsum96','$2a$10$s7zJv63QRX5sYQw7liJWi.6qrmg/7pkZrJ3/3as7lIDyuSDou5w5i','123',-102,294,'noobmaster68',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Educator','Lao Shi','lsls190@gmail.com','ls','$2a$10$CdaJxG7DksuL3vKtiwhrqeAKjjfLPKF0pBaVgXCKFQBPTh7acO7iK','ls',-22,66,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','Nevergonna19','never@gmail.com','Nevergonna19','$2a$10$KzzXaQSJ8iMCRt7kNrKrq.kzuPKP7zpSFvrYpkyexe9tFqC59ZA5.','234',230,202,NULL,NULL,'giveYouup3',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','noobmaster68','noob@gmail.com','noobmaster68','$2a$10$joUjRweGFysRD24m/UNY7u09STHPxSYWoV1mQ76.GjY28vUc8VEGu','123',448,147,NULL,NULL,'loremipsum96',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','reelansantya','reel@gmail.com','reelansantya','$2a$10$4wlux5UwBKzcSSZPATCOuOaCPk0N64lGE3ZWlpxI6ejL3wIYcX4re','123',-99,-444,'Santya24',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','Samadabdul','sama@gmail.com','Samadabdul','$2a$10$ECC8GWHL1A46l3aGkaXpT.cat11oEBEA/wkeWRCs44B5LDdwZoG4C','234',-320,67,'aliabdul10',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','Santya24','santya@gmail.com','Santya24','$2a$10$hxJ2kjLHxEjvJZ7eb2cFZ.5wYhVaJjuwxf7BDJy1uct3MnyjnxPZS','123',68,-358,NULL,NULL,'reelansantya',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Parent','Tan Chin Peng','tcp@live.com','TanChinPeng','$2a$10$4wFtvQBpL4W5AcyC862DjOBCKWgha7olU4O9dd2GvyofflHiVeefS','TanChinPeng',2,5,NULL,NULL,'Adamtan09','Laura_tan','jason0319',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','yinjiadoe20','yin@gmail.com','yinjiadoe20','$2a$10$8/fEpsUqES3kvJEOxwlsyeHBUfI1tr9fvy6UDqJ0wQO99At58vdZq','234',-85,-193,'johndoe3698',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Student','youyou','youyou@gmail.com','youyou','$2a$10$0yf/BY4kOq00tOuOFyz12ut3bAVaaRIOW3vX46h5N2QcIg.KNCGg.','youyou',128,126,'Lily',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `student_insert_trigger` AFTER INSERT ON `user` FOR EACH ROW BEGIN
    IF NEW.Role = 'Student' THEN
        -- Insert the student into the 'student' table
        INSERT INTO student (`Role`, `Name`, `Email`, `Username`, `X`, `Y`, `Parent1`, `Parent2`)
        VALUES (NEW.Role, NEW.Name, NEW.Email, NEW.Username, NEW.X, NEW.Y, NEW.`Parent1`, NEW.`Parent2`);

        -- Insert the student's username into the 'leaderboard' table with initial points and null pointLastUpdated
        INSERT INTO `dsass`.`leaderboard` (`stu_name`, `points`, `pointLastUpdated`)
        VALUES (NEW.Username, 0, NULL);

        -- Automatically add quiz_status records for new student (set status as '-' and completeTime as NULL)
        INSERT INTO `dsass`.`quiz_status` (`stu_name`, `quiz_id`, `status`, `completeTime`)
        SELECT NEW.Username, qc.Id, '-', NULL
        FROM `dsass`.`quiz_create` qc;

    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_update_user` AFTER UPDATE ON `user` FOR EACH ROW BEGIN
    IF NEW.Role = 'Student' THEN
        UPDATE student
        SET Name = NEW.Name,
            Email = NEW.Email,
            Username = NEW.Username,
            X = NEW.X,
            Y = NEW.Y,
            Parent1 = NEW.Parent1,
            Parent2 = NEW.Parent2
        WHERE Username = NEW.Username;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_delete_user` AFTER DELETE ON `user` FOR EACH ROW BEGIN
    DELETE FROM student WHERE Username = OLD.Username;
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
