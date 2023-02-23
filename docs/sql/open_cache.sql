/*
 Navicat Premium Data Transfer

 Source Server         : fresh
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : open_cache

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 23/02/2023 17:01:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for open_cache_app
-- ----------------------------
DROP TABLE IF EXISTS `open_cache_app`;
CREATE TABLE `open_cache_app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称（英文） ',
  `app_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用描述',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of open_cache_app
-- ----------------------------
INSERT INTO `open_cache_app` VALUES (1, 'open-cache-app', '测试应用1', 1, NULL, '2022-06-11 08:27:35', NULL);
INSERT INTO `open_cache_app` VALUES (2, 'open-cache-app2', '测试应用2', 1, NULL, '2022-06-11 08:28:31', NULL);
INSERT INTO `open_cache_app` VALUES (3, 'open-cache-app3', '测试应用3', 1, NULL, '2023-02-08 22:54:15', NULL);
INSERT INTO `open_cache_app` VALUES (4, 'job-client-services3', '测试应用3', 1, NULL, '2023-02-14 22:01:26', NULL);

-- ----------------------------
-- Table structure for open_cache_log
-- ----------------------------
DROP TABLE IF EXISTS `open_cache_log`;
CREATE TABLE `open_cache_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL COMMENT '应用 id',
  `instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存实例 id',
  `cache_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存名称',
  `command` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '命令类型',
  `cache_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存 key',
  `cache_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存 value',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '任务状态',
  `cause` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '失败原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of open_cache_log
-- ----------------------------
INSERT INTO `open_cache_log` VALUES (1, 1, NULL, 'test16', 'update', 'loadUserById16:1', '{\"id\":1,\"name\":\"lijunping & pengguifang\"}', 1, NULL, '2023-02-23 16:58:47');

-- ----------------------------
-- Table structure for open_cache_report
-- ----------------------------
DROP TABLE IF EXISTS `open_cache_report`;
CREATE TABLE `open_cache_report`  (
  `id` bigint(20) NOT NULL,
  `app_id` bigint(20) NULL DEFAULT NULL COMMENT '应用 id',
  `instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存实例 id',
  `cache_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存名称',
  `request_count` bigint(20) NULL DEFAULT NULL COMMENT '总请求总数',
  `hit_count` bigint(20) NULL DEFAULT NULL COMMENT '总命中总数',
  `miss_count` bigint(20) NULL DEFAULT NULL COMMENT '总未命中总数',
  `hit_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '命中率',
  `miss_rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '未命中率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of open_cache_report
-- ----------------------------
INSERT INTO `open_cache_report` VALUES (1, 1000, '980', NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-10 12:54:15');
INSERT INTO `open_cache_report` VALUES (2, 1200, '1100', NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-11 12:55:14');
INSERT INTO `open_cache_report` VALUES (3, 1300, '1200', NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-12 12:55:55');
INSERT INTO `open_cache_report` VALUES (4, 13500, '13000', NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-13 14:02:39');

-- ----------------------------
-- Table structure for open_cache_user
-- ----------------------------
DROP TABLE IF EXISTS `open_cache_user`;
CREATE TABLE `open_cache_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '用户状态（0：正常，1锁定）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '爬虫系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of open_cache_user
-- ----------------------------
INSERT INTO `open_cache_user` VALUES (1, 'lijunping', '$2a$10$3oNlO/vvXV3FPsmimv0x3ePTcwpe/E1xl86TDC0iLKwukWkJoRIyK', '18242076871', 0, '2021-06-24 15:53:16', '2021-06-24 15:53:19');
INSERT INTO `open_cache_user` VALUES (2, 'pengguifang', '$2a$10$3oNlO/vvXV3FPsmimv0x3ePTcwpe/E1xl86TDC0iLKwukWkJoRIyK', '18322520810', 0, '2021-11-30 21:53:37', '2021-11-30 21:53:40');

SET FOREIGN_KEY_CHECKS = 1;
