
alter table `umt_user` change `truename` `true_name` varchar(255) default NULL;
alter table `umt_user` change `username` `cstnet_id` varchar(255) default NULL;
alter table `umt_user` change `authBy` `auth_by` varchar(255) default NULL;

alter table `umt_user` change `createTime` `create_time` datetime;
alter table `umt_user` add column `security_email` varchar(255) default NULL;
alter table `umt_user` add column `secondary_email` varchar(255) default NULL;
alter table `umt_user` add column `umt_id` int(11) default NULL;
alter table `umt_user` drop column `email`;
update `umt_user` set `umt_id`=`id`+10000000;
delete from `umt_user` where auth_by='coreMail';
ALTER TABLE `umt_user` drop index `username`;

alter table `umt_passchangetoken` rename to `_umt_passchangetoken`;


delete from `umt_log`;
alter table `umt_log` change `username` `uid` int(11) default NULL;

CREATE TABLE `umt_login_name` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL ,
  `login_name` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `auth_by` varchar(255) default NULL,
  `tmp_login_name` varchar(255) default NULL,
  `status` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into `umt_login_name`(`uid`,`login_name`,`type`,`auth_by`,`status`) 
select `id`,`cstnet_id`,'primary',`auth_by`,'active' from `umt_user`;

CREATE TABLE `umt_third_party_bind` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `open_id` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `true_name` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `openid` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `umt_third_party_bind`(`uid`,`open_id`,`type`,`true_name`)
select `id`,`openid`,`auth_by`,`true_name` from `umt_user` where openid is not null and `auth_by` in('weibo','qq');

alter table `umt_user` drop column `openid`;

CREATE TABLE `umt_token`(
  `id` int(11) NOT NULL auto_increment,
  `token` varchar(255) default NULL,
  `uid` int(11) default NULL,
  `create_time` datetime default NULL,
  `operation` varchar(50) default NULL,
  `status` int(11) default NULL,
  `expire_time` datetime default NULL,
  `content` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;

CREATE TABLE `umt_app_access` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `app_name` varchar(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `umt_runtime_prop` (
  `id` int(11) NOT NULL auto_increment,
  `prop_name` varchar(255) default NULL,
  `prop_value` text,
  `last_modify_time` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into `umt_runtime_prop` values(1,'umtcert','-----BEGIN CERTIFICATE-----\n7075626C6963/30819F300D06092A864886F70D010101050003818D003081890\n2818100C7E8CA94A1F3B4DCDED86881C610FD8AAE2CC2F567F49A88358359B1B\nC472A23998744F562698BBDB57AE756DEDD1DDF5D65E47CC1EABFB1A6156F42E\n84FFA3D2FC49B50612DF11580233FF1F8813BB636AE600ABCB86B741403DD192\nE0C3928E5F6818178A96A04A1602B7A87DAC852839EBF615EF52EBF58BE8226B\n1BEE5910203010001/70726976617465/30820277020100300D06092A864886F\n70D0101010500048202613082025D02010002818100C7E8CA94A1F3B4DCDED86\n881C610FD8AAE2CC2F567F49A88358359B1BC472A23998744F562698BBDB57AE\n756DEDD1DDF5D65E47CC1EABFB1A6156F42E84FFA3D2FC49B50612DF11580233\nFF1F8813BB636AE600ABCB86B741403DD192E0C3928E5F6818178A96A04A1602\nB7A87DAC852839EBF615EF52EBF58BE8226B1BEE59102030100010281807E6E1\n3F0FC9C0CFEC3514A8708A52634D7032829DC9D2E3E86D993987CE2E51BAEB58\n63F4B064582AD8C1553338FD49CB78D017C8587D9C498AD65B8830451D5D2816\n15986A09E28A341AE81D2BE633036408A20576E0D68FC5814AB1066B3E3AA5D0\nED827AA67A97DEA4274902343212193CFDA7BDFC7B6D1FBE64A85C47CA102410\n0E35EE43AC6571555CA0C79E0C4674300956A527C56BFF195CDBE35D9ED75011\nC0E3BE643B9ADC25176B2D29200731D06B9A10FC1C71691E7A1D654AD6105D57\nF024100E114B4CB24631B87F7721FD36E811B0F1ADCC7EE25D83ADFF1931317B\n1238543F274BB3A3F3D2DBA8B40C9D207E01D1CD1B7CF41D64317D6062558DDE\n4456CEF024100B21CE4B6F226C7CCD3A1A05C261F2A4AABC1856A67DEA7168DF\nCC25CBC7A9AEAEA6F9BFB53DA3D4DE9A93DF127AF057EFE2DA095512B6A12C52\n18C65E18A5B6B024100A74258DB808DB97EBF8DEA4297228FB618D32A2B39AA4\n68DF619C21D6000325EFCC46C016BCF0DB54B17F921FFC236401D44475AF85AE\n152A53C899E52AC98B7024006635B61D53308DB86D96B675F2709CC9013E270F\n9C4B480A3AC4AE5B6090C6F9A1B08CBEC5A5C182D3DAA27FBD1C55022BA7A80F\nBE5938EBCA562EE16AD4B69\n-----END CERTIFICATE-----',now());

CREATE TABLE `umt_access_ips` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) default NULL,
  `ip` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'127.0.0.1');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.137');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.133');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.144');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.217');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.249');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.219');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.51');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.52');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.53');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.58');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.133');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.60');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.64');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.65');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.66');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.50');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.51');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.52');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.53');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.54');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.55');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.27');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.28');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.29');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.72');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.71');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.209');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.138');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.131');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.50');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.51');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.52');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.53');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.54');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.55');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.56');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.93');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.189');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.94');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.230');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.231');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.237');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.238');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.56.239');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'58.252.5.200');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'58.252.5.201');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'58.252.5.202');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.2.213');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.73');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.88');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'10.10.1.151');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.84');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.50');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.86');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'210.72.88.239');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'210.72.88.251');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'119.78.102.49');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.121.2');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'119.78.226.27');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.82.150');

INSERT INTO `umt_access_ips`(`uid`,`ip`) VALUES(1,'159.226.11.86');


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
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `umt_oauth_token` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `access_token` varchar(45) NOT NULL,
  `refresh_token` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `access_expired` timestamp NOT NULL default '0000-00-00 00:00:00',
  `refresh_expired` timestamp NOT NULL default '0000-00-00 00:00:00',
  `client_id` varchar(45) NOT NULL,
  `scope` varchar(45) NOT NULL,
  `redirect_uri` varchar(45) NOT NULL,
  `uid` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
