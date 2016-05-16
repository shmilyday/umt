CREATE TABLE  `umt_oauth_token` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `access_token` varchar(45) NOT NULL,
  `refresh_token` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `access_expired` timestamp NOT NULL default '0000-00-00 00:00:00',
  `refresh_expired` timestamp NOT NULL default '0000-00-00 00:00:00',
  `client_id` varchar(45) NOT NULL,
  `scope` varchar(45) default NULL,
  `redirect_uri` varchar(255) NOT NULL,
  `uid` varchar(45) NOT NULL,
  `password_type` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER  TABLE  `umt_oauth_token`  ADD  INDEX umt_oauth_token_access_index (`access_token`);
ALTER  TABLE  `umt_oauth_token`  ADD  INDEX umt_oauth_token_refresh_index (`refresh_token`);
-- ----------------------------
-- Table structure for umt_token
-- ----------------------------
CREATE TABLE `umt_token` (
  `id` int(11) NOT NULL auto_increment,
  `token` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `create_time` datetime default NULL,
  `operation` varchar(50) default NULL,
  `status` int(11) default NULL,
  `expire_time` datetime default NULL,
  `content` varchar(500) default NULL,
   `password_type` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;


CREATE TABLE `umt_log_0` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `enable_app_pwd` enum('yes','no') default NULL,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_1` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_2` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_3` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_4` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `umt_log_5` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_6` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_7` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_8` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `umt_log_9` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  `city` varchar(255) default NULL,
  `country` varchar(255) default NULL,
  `province` varchar(255) default NULL,
  `send_warn_email` enum('true','false') default 'false',
  `unit_name` varchar(255) default NULL,
  `is_cstnet_unit` enum('true','false') default 'false',
  `from_dip` enum('true','false') default 'false',
  PRIMARY KEY  (`id`),
  KEY `topTenIndex` (`eventType`,`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
