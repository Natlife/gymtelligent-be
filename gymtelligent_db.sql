-- Gymtelligent AI Fitness App - MySQL Database Schema & Seed Data

CREATE DATABASE IF NOT EXISTS `gym` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `gym`;

-- --------------------------------------------------------
-- Table: roles
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `roles` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255) DEFAULT 'SYSTEM',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255) DEFAULT 'SYSTEM',
  `name` VARCHAR(50) NOT NULL UNIQUE,
  `description` VARCHAR(255)
) ENGINE=InnoDB;

-- Seed default roles
INSERT INTO `roles` (`name`, `description`) 
VALUES 
('ROLE_ADMIN', 'Administrator with full system privileges'),
('ROLE_USER', 'Standard User with fitness tracking access')
ON DUPLICATE KEY UPDATE `description` = VALUES(`description`);

-- --------------------------------------------------------
-- Table: users
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255) DEFAULT 'SYSTEM',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255) DEFAULT 'SYSTEM',
  `user_name` VARCHAR(100) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(150) NOT NULL UNIQUE,
  `full_name` VARCHAR(150),
  `active` TINYINT(1) DEFAULT 1,
  `role_id` INT,
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- Table: user_profiles
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_profiles` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255),
  `user_id` INT NOT NULL UNIQUE,
  `weight_kg` DOUBLE,
  `height_cm` DOUBLE,
  `age` INT,
  `gender` ENUM('MALE', 'FEMALE', 'OTHER'),
  `fitness_goal` ENUM('BUILD_MUSCLE', 'LOSE_FAT', 'IMPROVE_STRENGTH', 'GENERAL_FITNESS'),
  `fitness_level` ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER',
  `avatar_url` VARCHAR(500),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- Table: exercises
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `exercises` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255) DEFAULT 'SYSTEM',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255) DEFAULT 'SYSTEM',
  `name` VARCHAR(100) NOT NULL UNIQUE,
  `category` ENUM('STRENGTH', 'CARDIO', 'FLEXIBILITY', 'BALANCE') NOT NULL,
  `difficulty_level` ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER',
  `met_value` DOUBLE NOT NULL,
  `description` TEXT,
  `muscle_groups` VARCHAR(255),
  `image_url` VARCHAR(500),
  `instructions` TEXT,
  `default_sets` INT DEFAULT 3,
  `default_reps` INT DEFAULT 12
) ENGINE=InnoDB;

-- Seed Exercises
INSERT INTO `exercises` (`name`, `category`, `difficulty_level`, `met_value`, `description`, `muscle_groups`, `instructions`, `default_sets`, `default_reps`) 
VALUES 
('Push Ups', 'STRENGTH', 'BEGINNER', 3.8, 'A fundamental upper body exercise that targets chest, shoulders, and triceps.', 'chest,shoulders,triceps,core', '[\"Start in a plank position with hands shoulder-width apart\",\"Lower your body until chest nearly touches the floor\",\"Keep your core tight and body in a straight line\",\"Push back up to starting position\",\"Repeat for desired reps\"]', 3, 15),
('Squats', 'STRENGTH', 'BEGINNER', 5.0, 'An essential lower body movement focusing on quadriceps, hamstrings, and glutes.', 'quadriceps,hamstrings,glutes,core', '[\"Stand with feet shoulder-width apart, toes slightly outward\",\"Lower hips back and down as if sitting in a chair\",\"Keep your chest high and knees behind your toes\",\"Drive through your heels to return to standing position\",\"Repeat\"]', 4, 12),
('Deadlifts', 'STRENGTH', 'ADVANCED', 6.0, 'A powerhouse compound exercise targeting the entire posterior chain.', 'lower back,glutes,hamstrings,core', '[\"Stand with mid-foot under the barbell, feet hip-width apart\",\"Bend at hips and knees, grab the bar with a shoulder-width grip\",\"Flatten your back, engage lats, and pull chest up\",\"Drive through heels to stand straight up, locking hips\",\"Repeat\"]', 4, 8)
ON DUPLICATE KEY UPDATE `met_value` = VALUES(`met_value`);

-- --------------------------------------------------------
-- Table: workout_sessions
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `workout_sessions` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255),
  `user_id` INT NOT NULL,
  `exercise_id` INT NOT NULL,
  `session_date` DATE NOT NULL,
  `started_at` DATETIME,
  `ended_at` DATETIME,
  `duration_seconds` INT DEFAULT 0,
  `total_reps` INT DEFAULT 0,
  `total_sets` INT DEFAULT 0,
  `calories_burned` DOUBLE DEFAULT 0.0,
  `avg_posture_score` DOUBLE DEFAULT 0.0,
  `ai_feedback` TEXT,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE,
  INDEX `idx_user_session_date` (`user_id`, `session_date`)
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- Table: daily_stats
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `daily_stats` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255),
  `user_id` INT NOT NULL,
  `stat_date` DATE NOT NULL,
  `total_calories` DOUBLE DEFAULT 0.0,
  `total_duration_seconds` INT DEFAULT 0,
  `total_reps` INT DEFAULT 0,
  `workout_count` INT DEFAULT 0,
  `avg_posture_score` DOUBLE DEFAULT 0.0,
  UNIQUE KEY `uk_user_stat_date` (`user_id`, `stat_date`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- Table: user_streaks
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_streaks` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255),
  `user_id` INT NOT NULL UNIQUE,
  `current_streak` INT DEFAULT 0,
  `longest_streak` INT DEFAULT 0,
  `last_workout_date` DATE,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------
-- Table: personal_records
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_records` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_user` VARCHAR(255),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_user` VARCHAR(255),
  `user_id` INT NOT NULL,
  `exercise_id` INT NOT NULL,
  `max_reps` INT DEFAULT 0,
  `max_duration_seconds` INT DEFAULT 0,
  `min_rest_time_seconds` INT,
  `achieved_at` DATETIME,
  UNIQUE KEY `uk_user_exercise` (`user_id`, `exercise_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;
