CREATE DATABASE  IF NOT EXISTS `onlinebank`
USE `onlinebank`;

-- порядок важен
DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;

CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `document_number` int(20) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `login` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_DOCUMENT` (`document_number`),
  UNIQUE KEY `UK_EMAIL` (`email`),
  UNIQUE KEY `UK_LOGIN` (`login`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `account` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account_balance` decimal(19,2) DEFAULT NULL,
  `account_number` int(11) NOT NULL,
  `currency` VARCHAR(255) NOT NULL DEFAULT 'RUB';
  `user_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_NUMBER` (`account_number`),
  CONSTRAINT `FK_User` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) DEFAULT CHARSET=utf8;


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

 CREATE TABLE `role` (
   `id` int(10) NOT NULL AUTO_INCREMENT,
   `name` varchar(255) DEFAULT 'CLIENT',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- добавим по умолчанию две роли
 INSERT INTO `role` VALUES ('1','CLIENT'),('2','ADMIN');
commit;

 CREATE TABLE `user_role` (
   `id` int(20) NOT NULL AUTO_INCREMENT,
   `role_id` int(10) DEFAULT NULL,
   `user_id` int(10) DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
   KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
   CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
   CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

