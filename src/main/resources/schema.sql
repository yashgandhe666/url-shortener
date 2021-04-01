CREATE TABLE IF NOT EXISTS `url_mappings` (
  `uuid` bigint NOT NULL AUTO_INCREMENT,
  `id` varchar(255) NOT NULL,
  `original_url` varchar(500) DEFAULT NULL,
  `short_url` varchar(255) DEFAULT NULL,
  `redirection_count` bigint DEFAULT 0,
  `entry_addition_time` bigint DEFAULT 0,
  `expiry_time` bigint DEFAULT 0,
  PRIMARY KEY(`uuid`)
);