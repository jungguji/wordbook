CREATE TABLE `word` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `level` INT(11) NULL DEFAULT 0,
    `meaning` VARCHAR(255) NULL DEFAULT NULL,
    `next_date` DATE NULL DEFAULT NULL,
    `word` VARCHAR(255) NULL DEFAULT NULL,
    `users_id` INT(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `fk_word_user` (`users_id`),
    CONSTRAINT `fk_word_user` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;