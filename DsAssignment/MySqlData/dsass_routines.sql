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
-- Dumping events for database 'dsass'
--

--
-- Dumping routines for database 'dsass'
--
/*!50003 DROP PROCEDURE IF EXISTS `update_leaderboard_proc` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_leaderboard_proc`(
    IN p_student_username VARCHAR(255),
    IN p_event_id INT,
    IN p_status VARCHAR(255),
    IN p_completeTime DATETIME
)
BEGIN
    DECLARE event_date DATE;
    DECLARE event_count INT;
    DECLARE clash_event_name VARCHAR(255);

    -- Handling event registration
    IF p_event_id IS NOT NULL THEN
        -- Get the date of the event being registered
        SELECT Date INTO event_date FROM event_create WHERE Id = p_event_id;

        -- Check if the student has already registered for an event on the same day
        SELECT COUNT(*), MAX(ec.Name) INTO event_count, clash_event_name
        FROM event_registration er
        INNER JOIN event_create ec ON er.event_id = ec.Id
        WHERE er.student_username = p_student_username
        AND ec.Date = event_date
        AND er.event_id != p_event_id; -- Make sure not to include the current registration

        -- If no clashing event, add points and update leaderboard
        IF event_count = 0 THEN
            UPDATE leaderboard
            SET points = points + 5,
                PointLastUpdated = CURDATE()
            WHERE stu_name = p_student_username;
        ELSE
            SET @msg = CONCAT('Cannot register for events on the same day. Clashing event: ', clash_event_name, '. Date: ', DATE_FORMAT(event_date, '%Y-%m-%d'));
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = @msg;
        END IF;
    END IF;

    -- Handling quiz completion
    IF p_status = 'complete' THEN
        UPDATE leaderboard
        SET points = points + 2,
            PointLastUpdated = p_completeTime
        WHERE stu_name = p_student_username;
    END IF;
END ;;
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

-- Dump completed on 2024-06-12 16:02:51
