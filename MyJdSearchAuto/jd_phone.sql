# Host: 218.240.49.29  (Version: 5.5.41-cll-lve)
# Date: 2015-03-12 19:50:05
# Generator: MySQL-Front 5.3  (Build 4.136)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "jd_comments_counts"
#

DROP TABLE IF EXISTS `jd_comments_counts`;
CREATE TABLE `jd_comments_counts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gid` varchar(255) DEFAULT '',
  `counts` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Structure for table "jd_goods"
#

DROP TABLE IF EXISTS `jd_goods`;
CREATE TABLE `jd_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gid` varchar(15) DEFAULT NULL,
  `gurl` varchar(255) DEFAULT NULL,
  `gname` varchar(255) DEFAULT NULL,
  `gtitle` varchar(255) DEFAULT NULL,
  `gprice` float DEFAULT NULL,
  `gkind` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Structure for table "jd_goods_comment"
#

DROP TABLE IF EXISTS `jd_goods_comment`;
CREATE TABLE `jd_goods_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(15) DEFAULT NULL,
  `uname` varchar(255) DEFAULT NULL,
  `ugrade` varchar(10) DEFAULT NULL,
  `uarea` varchar(10) DEFAULT NULL,
  `comment_star` tinyint(3) DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `ucomment` text,
  `ucomment_time` datetime DEFAULT NULL,
  `usatisfied` varchar(255) DEFAULT NULL,
  `ufrom` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=114696 DEFAULT CHARSET=utf8;
