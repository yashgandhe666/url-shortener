CREATE TABLE IF NOT EXISTS `url_mappings` (
    `id` varchar(255) NOT NULL,
    `original_url` varchar(500) DEFAULT NULL,
    `short_url` varchar(255) DEFAULT NULL,
    PRIMARY KEY(`id`)
);