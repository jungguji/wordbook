--drop table `users`
CREATE TABLE `users` (
    `id` INT(11) NOT NULL AUTO_INCREMENT
    , `username` VARCHAR(50) NOT NULL
    , `password` VARCHAR(500) NOT NULL
    , PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8 
COLLATE=UTF8_DANISH_CI
;