-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 192.168.45.245    Database: davebase
-- ------------------------------------------------------
-- Server version	5.5.5-10.3.38-MariaDB-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aha_api_key`
--

DROP TABLE IF EXISTS `aha_api_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aha_api_key` (
  `id` int(11) NOT NULL,
  `api_key` varchar(512) DEFAULT NULL,
  `expire_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT current_timestamp(),
  `created_at` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aha_api_key`
--

LOCK TABLES `aha_api_key` WRITE;
/*!40000 ALTER TABLE `aha_api_key` DISABLE KEYS */;
/*!40000 ALTER TABLE `aha_api_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aha_role`
--

DROP TABLE IF EXISTS `aha_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aha_role` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `aha_role_UN` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aha_role`
--

LOCK TABLES `aha_role` WRITE;
/*!40000 ALTER TABLE `aha_role` DISABLE KEYS */;
INSERT INTO `aha_role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_MANAGER'),(3,'ROLE_USER');
/*!40000 ALTER TABLE `aha_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aha_user`
--

DROP TABLE IF EXISTS `aha_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aha_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `aha_user_UN` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aha_user`
--

LOCK TABLES `aha_user` WRITE;
/*!40000 ALTER TABLE `aha_user` DISABLE KEYS */;
INSERT INTO `aha_user` VALUES (1,'amuuser','$2a$10$a3YRWTyVDMqCB0hBVpegruNVcZ8lAtbTbrcaRy7/qRybg5BNfnS7S',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 00:47:29'),(2,'amuser','$2a$10$.NmrsY0Koyw0iNa/poOC7.TMcPCPN6drgP/tNcVM27prCspp4Vi96',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 00:47:29'),(3,'auuser','$2a$10$.57pfX.fHZBU0VuNfCyhfuSamzAhE8zGUFAqqwqQlJRGcaosU0T7C',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 02:26:14'),(4,'muuser','$2a$10$/5Yb9DTdtPk0WZqbeBjjbu6ygSk70XO4VjMw6IlJV6ct6flvEepEC',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 00:47:29'),(5,'auser','$2a$10$mLTt.S/rOQ5s6HG1aj0myOAtliNmFwohvd20gYITGipDuTxLdi40i',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 00:47:29'),(6,'muser','$2a$10$XgCCOsaOBmEDZZHksm/Sm..L8/uBW7G4IwtIKoxGIiJQMdhZon/nm',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 00:47:29'),(7,'uuser','$2a$10$l7aWrk1xFoT0Wrh0475LzO3xnAvF31780HtkDDEi4hU8MGxAFC082',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 00:47:29'),(8,'nonuser','$2a$10$ieKR2IiozV2KLfs5bp3JOe2tkjmYmHGDP8jlwXjO2fYrirO.Zd9YW',NULL,NULL,'2023-03-24 00:47:29','2023-03-24 00:47:29');
/*!40000 ALTER TABLE `aha_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aha_user_image`
--

DROP TABLE IF EXISTS `aha_user_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aha_user_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `filename_server` varchar(100) DEFAULT NULL,
  `filename_client` varchar(300) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aha_user_image`
--

LOCK TABLES `aha_user_image` WRITE;
/*!40000 ALTER TABLE `aha_user_image` DISABLE KEYS */;
INSERT INTO `aha_user_image` VALUES (1,6,NULL,'01eddb85-d63f-1eb8-87c9-04529c92ee69','graffiti.jpg','2023-04-15 09:29:49');
/*!40000 ALTER TABLE `aha_user_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aha_user_role`
--

DROP TABLE IF EXISTS `aha_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aha_user_role` (
  `id_user` int(11) DEFAULT NULL,
  `id_role` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aha_user_role`
--

LOCK TABLES `aha_user_role` WRITE;
/*!40000 ALTER TABLE `aha_user_role` DISABLE KEYS */;
INSERT INTO `aha_user_role` VALUES (1,1),(1,2),(1,3),(2,1),(2,2),(3,1),(3,3),(4,2),(4,3),(5,1),(6,2),(7,3);
/*!40000 ALTER TABLE `aha_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (3001,1,9223372036854775806,1,1,1000,0,0);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'davebase'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-15  9:50:47
