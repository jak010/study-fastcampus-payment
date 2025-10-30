-- DDL for RetryRequest
DROP TABLE IF EXISTS `retry_request`;
CREATE TABLE `retry_request` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `request_json` TEXT,
    `request_id` VARCHAR(255),
    `retry_count` INT,
    `error_response` TEXT,
    `status` VARCHAR(255),
    `created_at` DATETIME,
    `updated_at` DATETIME,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ENGINE=InnoDB;


-- DDL for Transaction
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT,
    `wallet_id` BIGINT,
    `order_id` VARCHAR(255),
    `transaction_type` VARCHAR(255), -- ENUM: CHARGE, PAYMENT
    `amount` DECIMAL(19, 2),
    `description` VARCHAR(255),
    `created_at` DATETIME,
    `updated_at` DATETIME,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ENGINE=InnoDB;


-- DDL for Wallet
DROP TABLE IF EXISTS `wallet`;
CREATE TABLE `wallet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT,
    `balance` DECIMAL(19, 2),
    `created_at` DATETIME,
    `updated_at` DATETIME,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ENGINE=InnoDB;


-- DDL for Order
DROP TABLE IF EXISTS `temp_order`;
CREATE TABLE `temp_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT,
    `amount` DECIMAL(19, 2),
    `request_id` VARCHAR(255),
    `course_id` BIGINT,
    `course_name` VARCHAR(255),
    `status` VARCHAR(255), -- ENUM: WAIT, REQUESTED, APPROVED
    `created_at` DATETIME,
    `updated_at` DATETIME,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ENGINE=InnoDB;
