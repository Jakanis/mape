-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: localhost    Database: mape_conferences
-- ------------------------------------------------------
-- Server version	5.7.26-0ubuntu0.19.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `conferences`
--

DROP TABLE IF EXISTS `conferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conferences` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(120) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `visitors_capacity` int(11) unsigned NOT NULL,
  `visitors_registered` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conferences`
--

LOCK TABLES `conferences` WRITE;
/*!40000 ALTER TABLE `conferences` DISABLE KEYS */;
INSERT INTO `conferences` VALUES (1,'Changed another conference','Kyiv, Ukraine','2019-06-15 16:41:00',0,50,0),(3,'Yet another conference','Kyiv, Ukraine','2020-06-17 16:38:00',0,50,2),(4,'Full another conference','Kyiv, Ukraine','2020-06-06 16:44:32',0,1,0),(5,'Changed another conference','Kyiv, Ukraine','2019-06-06 16:44:39',1,50,0),(7,'Changed another conference','Kyiv, Ukraine','2019-06-06 16:44:53',0,50,0),(14,'yaay111','22221','2322-02-22 20:22:00',0,222,2),(15,'Конференція українською','It works','2222-02-22 20:22:00',0,22,0),(16,'Це працює','21214','2123-03-23 19:14:00',0,22,0),(17,'22','2','2222-02-02 20:22:00',0,22,0);
/*!40000 ALTER TABLE `conferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lectures`
--

DROP TABLE IF EXISTS `lectures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lectures` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `topic` varchar(120) NOT NULL,
  `speaker_id` int(11) unsigned NOT NULL,
  `conference_id` int(11) unsigned NOT NULL,
  `moderator_approved` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `speaker_approved` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_lecture_1_idx` (`speaker_id`),
  KEY `fk_lecture_2_idx` (`conference_id`),
  CONSTRAINT `fk_lecture_1` FOREIGN KEY (`speaker_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lecture_2` FOREIGN KEY (`conference_id`) REFERENCES `conferences` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lectures`
--

LOCK TABLES `lectures` WRITE;
/*!40000 ALTER TABLE `lectures` DISABLE KEYS */;
INSERT INTO `lectures` VALUES (5,'Java GC',18,1,1,1,0),(6,'C++20',17,3,0,1,1),(7,'C#8',18,3,1,1,0),(8,'C#9',17,3,1,1,1),(53,'1',17,1,1,1,0),(54,'2',17,1,1,1,0),(55,'3',17,1,1,1,0),(56,'311',17,3,1,1,0),(57,'321',18,3,1,1,0),(58,'123334',17,4,0,1,0),(60,'12',17,14,1,1,0),(62,'11',18,16,1,1,0),(63,'22',17,16,1,1,0),(64,'22',17,17,1,1,0),(65,'22',18,17,1,1,0);
/*!40000 ALTER TABLE `lectures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rates`
--

DROP TABLE IF EXISTS `rates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rates` (
  `user_id` int(11) unsigned DEFAULT NULL,
  `speaker_id` int(11) unsigned DEFAULT NULL,
  `rate` varchar(45) DEFAULT NULL,
  UNIQUE KEY `user_id` (`user_id`,`speaker_id`),
  KEY `fk_rates_1_idx` (`user_id`),
  KEY `fk_rates_2_idx` (`speaker_id`),
  CONSTRAINT `fk_rates_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rates_2` FOREIGN KEY (`speaker_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rates`
--

LOCK TABLES `rates` WRITE;
/*!40000 ALTER TABLE `rates` DISABLE KEYS */;
INSERT INTO `rates` VALUES (17,6,'5'),(1,17,'4'),(17,20,'3'),(1,20,'4'),(1,18,'4'),(17,18,'3'),(6,17,'4'),(6,20,'4'),(6,18,'4');
/*!40000 ALTER TABLE `rates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registrations`
--

DROP TABLE IF EXISTS `registrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registrations` (
  `conference_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  UNIQUE KEY `conference_id` (`conference_id`,`user_id`),
  KEY `fk_registrations_1_idx` (`conference_id`),
  KEY `fk_registrations_2_idx` (`user_id`),
  CONSTRAINT `fk_registrations_1` FOREIGN KEY (`conference_id`) REFERENCES `conferences` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_registrations_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registrations`
--

LOCK TABLES `registrations` WRITE;
/*!40000 ALTER TABLE `registrations` DISABLE KEYS */;
INSERT INTO `registrations` VALUES (1,6),(1,7),(3,1),(3,2),(3,6),(3,7),(4,1),(5,6),(7,7),(14,1),(14,6);
/*!40000 ALTER TABLE `registrations` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER registation_insert_trigger AFTER INSERT ON registrations
FOR EACH ROW 
BEGIN
    UPDATE conferences SET visitors_registered=visitors_registered+1 WHERE id=NEW.conference_id;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER registation_delete_trigger AFTER DELETE ON registrations
FOR EACH ROW
BEGIN
    UPDATE conferences SET visitors_registered=visitors_registered-1 WHERE id=OLD.conference_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `role_UNIQUE` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (4,'Admin'),(3,'Moderator'),(2,'Speaker'),(1,'User');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `speakers`
--

DROP TABLE IF EXISTS `speakers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `speakers` (
  `user_id` int(10) unsigned NOT NULL,
  `average_rate` double unsigned NOT NULL,
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  CONSTRAINT `fk_speakers_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `speakers`
--

LOCK TABLES `speakers` WRITE;
/*!40000 ALTER TABLE `speakers` DISABLE KEYS */;
/*!40000 ALTER TABLE `speakers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(80) NOT NULL,
  `password` varchar(80) NOT NULL,
  `first_name` varchar(80) DEFAULT NULL,
  `last_name` varchar(80) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idusers_UNIQUE` (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_users_1_idx` (`role_id`),
  CONSTRAINT `fk_users_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin1','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','admin1name','admin1_surname','admin@gmail.com',4,0),(2,'admin2','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','admin2name','admin2_surname','admin2@gmail.com',4,0),(3,'moder1','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','moder1name','moder1_surname','moder1@gmail.com',3,0),(4,'moder2','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','moder2name','moder2_surname','moder2@gmail.com',3,0),(6,'user1','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','user1name','user1_surname','user1@gmail.com',1,0),(7,'user2','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','user2_ім\'я','user2_прізвище','user2@gmail.com',1,1),(17,'Speaker1','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','speaker1_name','speaker1_surname','speaker1@g.co',2,0),(18,'Speaker2','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','speaker2_name','speaker2_surname','speaker2@g.co',2,0),(20,'Speaker3','F39DAAA7C3ECF40C7701688327700A7FD2C53C2EDD745AD83FA6B1E83704A03B','speaker3_name','speaker3_surname','speaker3@g.co',2,0),(21,'Jakanis','76CF58F6BE237267B2C86094DEEE26E7B006ADAAA8B4239DB2CBB56B1CE00F5E','Kozii','Evgeniy','varmid@gmail.com',1,0),(22,'Jakanis1','76CF58F6BE237267B2C86094DEEE26E7B006ADAAA8B4239DB2CBB56B1CE00F5E','Evgeniy','Kozii','varmid1@gmail.com',1,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-13 14:34:04
