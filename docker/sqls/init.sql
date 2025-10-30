-- Drop tables in correct dependency order
DROP TABLE IF EXISTS `Transaction`;
DROP TABLE IF EXISTS `temp_order`;
DROP TABLE IF EXISTS `wallet`;
DROP TABLE IF EXISTS `RetryRequest`;

-- RetryRequest
CREATE TABLE `RetryRequest` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `requestJson` TEXT,
    `requestId` VARCHAR(255),
    `retryCount` INT,
    `errorResponse` TEXT,
    `status` ENUM ('FAILURE', 'IN_PROGRESS', 'SUCCESS'),
    `createdAt` DATETIME(6),
    `updatedAt` DATETIME(6),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- temp_order
CREATE TABLE `temp_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `userId` BIGINT,
    `courseId` BIGINT,
    `courseName` VARCHAR(255),
    `requestId` VARCHAR(255),
    `amount` DECIMAL(38, 2),
    `status` ENUM ('APPROVED', 'REQUESTED', 'WAIT'),
    `createdAt` DATETIME(6),
    `updatedAt` DATETIME(6),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Transaction
CREATE TABLE `Transaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `walletId` BIGINT,
    `userId` BIGINT,
    `orderId` BIGINT,
    `transactionType` ENUM ('CHARGE', 'PAYMENT'),
    `amount` DECIMAL(38, 2),
    `description` VARCHAR(255),
    `createdAt` DATETIME(6),
    `updatedAt` DATETIME(6),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- wallet
CREATE TABLE `wallet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT,
    `balance` DECIMAL(38, 2),
    `createdAt` DATETIME(6),
    `updatedAt` DATETIME(6),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
