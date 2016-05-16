/*
MySQL Data Transfer
Source Host: localhost
Source Database: duckling_new
Target Host: localhost
Target Database: duckling_new
Date: 2013/3/19 14:11:35
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for umt_access_ips
-- ----------------------------
CREATE TABLE `umt_access_ips` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `ip` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for umt_app_access
-- ----------------------------
CREATE TABLE `umt_app_access` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `app_name` varchar(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for umt_application
-- ----------------------------
CREATE TABLE `umt_application` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `url` varchar(255) default NULL,
  `keyid` int(11) default NULL,
  `serverType` varchar(255) default NULL,
  `description` text,
  `allowOperate` tinyint(1) default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
-- Table structure for umt_associate
-- ----------------------------
CREATE TABLE `umt_associate` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `associate_uid` int(11) default NULL,
  `app_list` varchar(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for umt_log
-- ----------------------------
CREATE TABLE `umt_log` (
  `id` int(11) NOT NULL auto_increment,
  `appname` varchar(255) default NULL,
  `eventType` varchar(255) default NULL,
  `appurl` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `ipaddress` varchar(128) default NULL,
  `occurTime` datetime default NULL,
  `browserType` varchar(255) default NULL,
  `remark` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table `umt_log` add index topTenIndex (`eventType`,`uid`);




-- ----------------------------
-- Table structure for umt_login_name
-- ----------------------------
CREATE TABLE `umt_login_name` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL ,
  `login_name` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `tmp_login_name` varchar(255) default NULL,
  `status` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for umt_pubkeys
-- ----------------------------
CREATE TABLE `umt_pubkeys` (
  `id` int(11) NOT NULL auto_increment,
  `pubkey` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;

-- ----------------------------
-- Table structure for umt_requests
-- ----------------------------
CREATE TABLE `umt_requests` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(255) default NULL,
  `trueName` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `createTime` datetime default NULL,
  `phoneNumber` varchar(255) default NULL,
  `orgnization` varchar(255) default NULL,
  `password` varchar(255) default NULL,
  `state` int(11) default NULL,
  `operator` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;

-- ----------------------------
-- Table structure for umt_role
-- ----------------------------
CREATE TABLE `umt_role` (
  `id` int(11) NOT NULL auto_increment,
  `rolename` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `rolename` (`rolename`(1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;

-- ----------------------------
-- Table structure for umt_role_member
-- ----------------------------
CREATE TABLE `umt_role_member` (
  `roleid` int(11) NOT NULL default '0',
  `userid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`roleid`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;

-- ----------------------------
-- Table structure for umt_runtime_prop
-- ----------------------------
CREATE TABLE `umt_runtime_prop` (
  `id` int(11) NOT NULL auto_increment,
  `prop_name` varchar(255) default NULL,
  `prop_value` text,
  `last_modify_time` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for umt_session
-- ----------------------------
CREATE TABLE `umt_session` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(255) NOT NULL default '',
  `appname` varchar(255) NOT NULL default '',
  `sessionid` varchar(64) NOT NULL default '',
  `logouturl` varchar(255) default NULL,
  `logintime` datetime default NULL,
  `lastupdate` datetime default NULL,
  `appType` varchar(64) default NULL,
  `userip` varchar(255) default NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
-- Table structure for umt_third_party_bind
-- ----------------------------
CREATE TABLE `umt_third_party_bind` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `open_id` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `true_name` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `openid` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for umt_ticket
-- ----------------------------
CREATE TABLE `umt_ticket` (
  `id` int(11) NOT NULL auto_increment,
  `type` int(11) default NULL,
  `createTime` datetime default NULL,
  `random` varchar(255) default NULL,
  `sessionid` varchar(255) default NULL,
  `extra` text,
  `userip` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
-- Table structure for umt_user
-- ----------------------------
CREATE TABLE `umt_user` (
  `id` int(11) NOT NULL auto_increment,
  `umt_id` varchar(255) default NULL,
  `true_name` varchar(255) default NULL,
  `password` varchar(255) default NULL,
  `create_time` datetime default NULL,
  `security_email` varchar(255) default NULL,
  `cstnet_id` varchar(255) default NULL,
  `secondary_email` varchar(255) default NULL,
  `account_status` enum('locked','normal') default 'normal',
  `type` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`umt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
CREATE TABLE  `umt_oauth_client` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `client_id` varchar(45) NOT NULL,
  `client_secret` varchar(45) NOT NULL,
  `description` text,
  `scope` varchar(45) default NULL,
  `redirect_uri` varchar(255) NOT NULL,
  `status` varchar(45) NOT NULL,
  `client_name` varchar(45) default NULL,
  `applicant` varchar(45) default NULL,
  `application_time` varchar(45) default NULL,
  `applicant_phone` varchar(45) default NULL,
  `contact_info` varchar(45) default NULL,
  `third_party` varchar(45) default NULL,
  `url` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `company` varchar(255) default NULL,
  `client_website` VARCHAR(255) default null,
  `app_type` varchar(255) default 'webapp',
  `pwd_type` varchar(10) DEFAULT 'none',
  `need_org_info` int DEFAULT '0',
  `logo_100` int , 
  `logo_64` int ,
  `logo_32` int ,
  `logo_16` int,
  `logo_custom` varchar(255),
  `default_logo` int,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `umt_certificate` (
  `id` int(11) NOT NULL auto_increment,
  `dn` varchar(255)  NOT NULL default '',
  `cstnetId` varchar(255)  NOT NULL default '',
  `regist_time` datetime default NULL,
  `pub_cert` text ,
  `full_cert` text ,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `cstnetId` (`cstnetId`),
  UNIQUE KEY `dn` (`dn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `umt_org` (
  `id` int(11) NOT NULL auto_increment,
  `org_symbol` varchar(255) NOT NULL,
  `org_name` varchar(255) default NULL,
  `is_cas` int(11) default '0',
  `is_coremail` int(11) default '0',
  `type` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `umt_org_domain` (
  `id` int(11) NOT NULL auto_increment,
  `org_id` varchar(255) default NULL,
  `org_domain` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `org_domain_UNIQUE` (`org_domain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `umt_ca` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `dn` varchar(100) default NULL,
  `cn` varchar(45) default NULL,
  `type` tinyint(1) default NULL,
  `valiFrom` timestamp NULL default NULL,
  `expirationOn` timestamp NULL default NULL,
  `password` varchar(45) default NULL,
  `status` tinyint(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `umt_role` VALUES ('1', 'admin', '管理员');
INSERT INTO `umt_role_member` VALUES ('1', '1');
INSERT INTO `umt_access_ips` VALUES(1,1,'127.0.0.1');
INSERT INTO `umt_user`(`id`,`umt_id`,`cstnet_id`,`true_name`,`password`,`create_time`,`type`) VALUES (1,'10000001','admin@root.umt','admin','IQEN5D81apj+t3dUwdjsPmfxrms=',now(),'umt');
-- password is admin!
INSERT INTO `umt_login_name` VALUES ('1', '1', 'admin@root.umt', 'primary',null,'active');