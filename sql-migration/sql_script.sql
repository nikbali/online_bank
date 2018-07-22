CREATE DATABASE  IF NOT EXISTS `onlinebank`
USE `onlinebank`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_EMAIL` (`email`),
  UNIQUE KEY `UK_LOGIN` (`login`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account_balance` decimal(19,2) DEFAULT NULL,
  `account_number` int(11) NOT NULL,
  `user_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_NUMBER` (`account_number`),
  CONSTRAINT `FK_User` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `sender_account_id` int(10) DEFAULT NULL,
  `receiver_account_id` int(10) DEFAULT NULL,  
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_Sender` FOREIGN KEY (`sender_account_id`) REFERENCES `account`(`id`),
  CONSTRAINT `FK_Reciever` FOREIGN KEY (`receiver_account_id`) REFERENCES `account`(`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;