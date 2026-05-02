/*
 Navicat Premium Dump SQL

 Source Server         : aijiapms
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : aijia-pms

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 02/05/2026 18:42:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bus_bill
-- ----------------------------
DROP TABLE IF EXISTS `bus_bill`;
CREATE TABLE `bus_bill`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `type` tinyint NULL DEFAULT 1 COMMENT '费用类型 (1:物业费, 2:水费, 3:电费, 4:车位费)',
  `year` int NOT NULL COMMENT '年份 (如: 2024)',
  `bill_date` date NOT NULL COMMENT '账单月份(取该月1号)',
  `start_date` date NULL DEFAULT NULL COMMENT '计费开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '计费结束日期',
  `month` int NULL DEFAULT 0 COMMENT '月份 (1-12，若按年计费可设为0)',
  `unit_price` decimal(10, 4) NOT NULL COMMENT '单价',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '应缴总额 (面积 * 单价)',
  `pay_status` tinyint NULL DEFAULT 0 COMMENT '支付状态 (0:待缴费, 1:已缴费)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '费用备注',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '实际缴费时间',
  `deadline` datetime NULL DEFAULT NULL COMMENT '缴费截止日期',
  `is_deleted` tinyint NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_room_type_date`(`room_id` ASC, `type` ASC, `bill_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物业费账单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_bill
-- ----------------------------
INSERT INTO `bus_bill` VALUES (1, 7, 1, 2024, '2024-05-01', '2024-05-01', '2024-05-31', 5, 2.5000, 301.25, 1, NULL, '2026-04-30 23:52:07', '2024-06-15 23:59:59', 0, '2026-04-30 23:41:30', '2026-04-30 23:41:30');
INSERT INTO `bus_bill` VALUES (2, 9, 1, 2024, '2024-05-01', '2024-05-01', '2024-05-31', 5, 2.5000, 301.25, 0, NULL, NULL, '2024-06-15 23:59:59', 0, '2026-04-30 23:41:30', '2026-04-30 23:41:30');
INSERT INTO `bus_bill` VALUES (3, 12, 1, 2024, '2024-05-01', '2024-05-20', '2024-05-31', 5, 2.5000, 116.61, 0, NULL, NULL, '2024-06-15 23:59:59', 0, '2026-04-30 23:41:30', '2026-04-30 23:41:30');
INSERT INTO `bus_bill` VALUES (4, 7, 1, 2026, '2026-05-01', '2026-05-01', '2026-05-31', 5, 2.6000, 313.30, 0, NULL, NULL, '2026-06-15 23:59:59', 0, '2026-04-30 23:51:37', '2026-04-30 23:51:37');
INSERT INTO `bus_bill` VALUES (5, 9, 1, 2026, '2026-05-01', '2026-05-01', '2026-05-31', 5, 2.6000, 313.30, 0, NULL, NULL, '2026-06-15 23:59:59', 0, '2026-04-30 23:51:37', '2026-04-30 23:51:37');
INSERT INTO `bus_bill` VALUES (6, 12, 1, 2026, '2026-05-01', '2026-05-01', '2026-05-31', 5, 2.6000, 313.30, 0, NULL, NULL, '2026-06-15 23:59:59', 0, '2026-04-30 23:51:37', '2026-04-30 23:51:37');
INSERT INTO `bus_bill` VALUES (7, 13, 1, 2026, '2026-05-01', '2026-05-01', '2026-05-31', 5, 2.6000, 313.30, 1, NULL, '2026-04-30 23:53:26', '2026-06-15 23:59:59', 0, '2026-04-30 23:51:37', '2026-04-30 23:51:37');
INSERT INTO `bus_bill` VALUES (8, 7, 1, 2026, '2026-03-01', '2026-03-01', '2026-03-31', 3, 2.8000, 337.40, 1, NULL, '2026-05-01 00:02:03', '2026-04-15 23:59:59', 0, '2026-04-30 23:53:11', '2026-04-30 23:53:11');
INSERT INTO `bus_bill` VALUES (9, 9, 1, 2026, '2026-03-01', '2026-03-01', '2026-03-31', 3, 2.8000, 337.40, 1, NULL, '2026-04-30 23:57:47', '2026-04-15 23:59:59', 0, '2026-04-30 23:53:11', '2026-04-30 23:53:11');
INSERT INTO `bus_bill` VALUES (10, 12, 1, 2026, '2026-03-01', '2026-03-01', '2026-03-31', 3, 2.8000, 337.40, 0, NULL, NULL, '2026-04-15 23:59:59', 0, '2026-04-30 23:53:11', '2026-04-30 23:53:11');
INSERT INTO `bus_bill` VALUES (11, 13, 1, 2026, '2026-03-01', '2026-03-01', '2026-03-31', 3, 2.8000, 337.40, 0, NULL, NULL, '2026-04-15 23:59:59', 0, '2026-04-30 23:53:11', '2026-04-30 23:53:11');

-- ----------------------------
-- Table structure for bus_building
-- ----------------------------
DROP TABLE IF EXISTS `bus_building`;
CREATE TABLE `bus_building`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼栋名称 (如: 1号楼)',
  `unit_count` int NULL DEFAULT 0 COMMENT '单元数量',
  `floor_count` int NULL DEFAULT 0 COMMENT '总层数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除 (0-正常, 1-已删)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE COMMENT '楼栋名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '楼栋信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_building
-- ----------------------------
INSERT INTO `bus_building` VALUES (1, '1号楼', 2, 18, '靠近社区北门，高层住宅', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (2, '2号楼', 3, 18, '一层为物业服务中心', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (3, '3号楼', 2, 26, '楼王位置，视野开阔', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (4, '5号楼', 4, 11, '多层洋房，带电梯', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (5, '6号楼', 1, 32, '单单元高层，景观房', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (6, '7号楼', 2, 18, '靠近中心花园', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (7, '8号楼', 3, 6, '步梯房，老旧改造区', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (8, '9号楼', 2, 18, '临街楼栋，底商已售罄', 0, '2026-04-26 01:26:29', '2026-04-26 01:58:33');
INSERT INTO `bus_building` VALUES (9, '10号楼', 2, 26, '紧邻儿童游乐场', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (10, '11号楼A座', 1, 30, '写字楼/公寓性质', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (11, '11号楼B座', 1, 30, '写字楼/公寓性质', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (12, '12号楼', 3, 11, '电梯小高层', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (13, '15号楼', 2, 18, '靠近地下车库入口', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (14, '16号楼', 4, 6, '低密度洋房', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (15, '17号楼', 2, 26, '高层，采光优越', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (16, '18号楼', 1, 18, '靠近社区东门', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (17, '19号楼', 2, 11, '员工宿舍区备选', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (18, '20号楼', 3, 18, '社区西侧，安静区域', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (19, '21号楼', 2, 6, '别墅区旁，低容积率', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_building` VALUES (20, '22号楼', 2, 18, '待验收新楼栋', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');

-- ----------------------------
-- Table structure for bus_notice
-- ----------------------------
DROP TABLE IF EXISTS `bus_notice`;
CREATE TABLE `bus_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `type` tinyint NULL DEFAULT 1 COMMENT '类型 (1:全体公告, 2:楼栋公告)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0:草稿, 1:发布, 2:撤回)',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '阅读状态(0:未读, 1:已读)',
  `author_id` bigint NULL DEFAULT NULL COMMENT '发布人ID',
  `target_id` bigint NULL DEFAULT NULL COMMENT '接收目标ID (全体:空, 楼栋:BuildingID, 个人:OwnerID)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社区公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_notice
-- ----------------------------
INSERT INTO `bus_notice` VALUES (1, '2024年度物业费缴纳提醒', '亲爱的业主，2024年度物业费账单已生成，请及时在系统中查询并缴费。', 1, 2, 0, NULL, NULL, '2026-04-27 22:54:30', '2026-04-29 23:53:01', 0);
INSERT INTO `bus_notice` VALUES (2, '五一假期安全提醒', '亲爱的业主，五一假期将至，请离家前关好门窗、断开电源，祝您节日愉快！', 1, 1, 0, NULL, NULL, '2026-04-28 22:20:28', '2026-04-28 22:20:28', 0);
INSERT INTO `bus_notice` VALUES (3, '111', '111', 2, 1, 0, NULL, NULL, '2026-04-28 22:25:14', '2026-04-28 22:25:14', 0);
INSERT INTO `bus_notice` VALUES (4, '2025年度物业费缴纳提醒', '亲爱的业主，2025年度物业费账单已生成，请及时在系统中查询并缴费。', 1, 1, 0, NULL, NULL, '2026-04-30 22:56:45', '2026-04-30 22:56:45', 0);

-- ----------------------------
-- Table structure for bus_owner
-- ----------------------------
DROP TABLE IF EXISTS `bus_owner`;
CREATE TABLE `bus_owner`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业主姓名',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `sex` tinyint NULL DEFAULT 0 COMMENT '性别 (0:未知, 1:男, 2:女)',
  `is_deleted` tinyint NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint NULL DEFAULT NULL COMMENT '关联系统用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '业主基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_owner
-- ----------------------------
INSERT INTO `bus_owner` VALUES (1, '张大三', '13800138001', '110101199001011234', 0, 0, '2026-04-26 21:28:48', 3);
INSERT INTO `bus_owner` VALUES (2, '李四', '13999929292', '224233333333333333', 0, 0, '2026-04-26 21:46:13', 11);
INSERT INTO `bus_owner` VALUES (4, 'aaa', '12333333333', '222333222222222222', 0, 0, '2026-04-26 22:26:51', NULL);
INSERT INTO `bus_owner` VALUES (5, '李四', '13999999999', NULL, 0, 1, '2026-05-02 12:07:57', 9);

-- ----------------------------
-- Table structure for bus_repair
-- ----------------------------
DROP TABLE IF EXISTS `bus_repair`;
CREATE TABLE `bus_repair`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL COMMENT '报修房间ID',
  `owner_id` bigint NOT NULL COMMENT '报修人ID',
  `contact_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报修人联系电话',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报修内容',
  `photos` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址(逗号隔开)',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 (0:待处理, 1:处理中, 2:已完成, 3:已评价)',
  `handler_id` bigint NULL DEFAULT NULL COMMENT '维修工用户ID',
  `handler_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维修人员姓名',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '维修完成时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_room_id`(`room_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '报修管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_repair
-- ----------------------------
INSERT INTO `bus_repair` VALUES (1, 1, 1, '13888888888', '卫生间水管漏水，请尽快派人修理', NULL, 2, NULL, '师傅小王', NULL, '2026-04-28 22:06:24', '2026-04-28 22:06:24', 0);
INSERT INTO `bus_repair` VALUES (2, 12, 3, '13322232323', '1111', 'asdf', 0, NULL, NULL, NULL, '2026-05-01 01:24:49', '2026-05-01 01:24:49', 0);
INSERT INTO `bus_repair` VALUES (3, 1, 3, '13800138000', '客厅主灯闪烁，怀疑是驱动电源坏了，请师傅来看看。', '/uploads/repair/20240502/abc.jpg,/uploads/repair/20240502/def.jpg', 0, NULL, NULL, NULL, '2026-05-02 09:52:07', '2026-05-02 09:52:07', 0);
INSERT INTO `bus_repair` VALUES (4, 7, 3, '13800138000', '客厅主灯闪烁，怀疑是驱动电源坏了，请师傅来看看。', '/uploads/repair/20240502/abc.jpg,/uploads/repair/20240502/def.jpg', 0, NULL, NULL, NULL, '2026-05-02 09:59:43', '2026-05-02 09:59:43', 0);
INSERT INTO `bus_repair` VALUES (5, 7, 1, '111', '客厅主灯闪烁，怀疑是驱动电源坏了，请师傅来看看。', '/uploads/repair/20240502/abc.jpg,/uploads/repair/20240502/def.jpg', 2, 7, '张师傅', '2026-05-02 11:55:00', '2026-05-02 10:05:03', '2026-05-02 11:54:59', 0);
INSERT INTO `bus_repair` VALUES (6, 12, 1, '111', '123', '', 1, 4, '维修人员1', NULL, '2026-05-02 10:06:08', '2026-05-02 11:02:01', 0);
INSERT INTO `bus_repair` VALUES (7, 12, 1, '123', '123', '', 2, 4, '维修人员1', '2026-05-02 11:03:28', '2026-05-02 10:06:56', '2026-05-02 11:03:27', 0);
INSERT INTO `bus_repair` VALUES (8, 12, 1, '13800138001', '123', '', 2, 4, '维修人员1', '2026-05-02 10:56:41', '2026-05-02 10:27:09', '2026-05-02 10:56:40', 0);
INSERT INTO `bus_repair` VALUES (9, 12, 1, '13800138001', '停电了', '22', 2, 4, '维修人员1', '2026-05-02 11:05:05', '2026-05-02 11:04:30', '2026-05-02 11:05:05', 0);

-- ----------------------------
-- Table structure for bus_room
-- ----------------------------
DROP TABLE IF EXISTS `bus_room`;
CREATE TABLE `bus_room`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID',
  `unit_id` bigint NOT NULL COMMENT '所属单元ID',
  `room_num` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号 (如: 101)',
  `floor` int NULL DEFAULT NULL COMMENT '楼层',
  `area` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '建筑面积',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 (0:闲置, 1:已售, 2:入住)',
  `delivery_date` date NULL DEFAULT NULL COMMENT '交房/起收日期',
  `room_type` tinyint NULL DEFAULT 1 COMMENT '房屋类型(1:住宅, 2:商铺, 3:其他)',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_unit_room`(`unit_id` ASC, `room_num` ASC) USING BTREE,
  INDEX `idx_unit_id`(`unit_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_room
-- ----------------------------
INSERT INTO `bus_room` VALUES (1, 2, 5, '101', 1, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (2, 2, 5, '102', 1, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (3, 2, 5, '201', 2, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (4, 2, 5, '202', 2, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (5, 2, 5, '301', 3, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (6, 2, 5, '302', 3, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (7, 2, 5, '401', 4, 120.50, 2, '2024-01-01', 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:35:42');
INSERT INTO `bus_room` VALUES (8, 2, 5, '402', 4, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (9, 2, 5, '501', 5, 120.50, 2, '2024-01-01', 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:35:42');
INSERT INTO `bus_room` VALUES (10, 2, 5, '502', 5, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (11, 2, 5, '601', 6, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (12, 2, 5, '602', 6, 120.50, 2, '2024-05-20', 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:35:51');
INSERT INTO `bus_room` VALUES (13, 2, 5, '701', 7, 120.50, 2, '2024-06-01', 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:36:00');
INSERT INTO `bus_room` VALUES (14, 2, 5, '702', 7, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (15, 2, 5, '801', 8, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (16, 2, 5, '802', 8, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (17, 2, 5, '901', 9, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (18, 2, 5, '902', 9, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (19, 2, 5, '1001', 10, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (20, 2, 5, '1002', 10, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (21, 2, 5, '1101', 11, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (22, 2, 5, '1102', 11, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (23, 2, 5, '1201', 12, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (24, 2, 5, '1202', 12, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (25, 2, 5, '1301', 13, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (26, 2, 5, '1302', 13, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (27, 2, 5, '1401', 14, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (28, 2, 5, '1402', 14, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (29, 2, 5, '1501', 15, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (30, 2, 5, '1502', 15, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (31, 2, 5, '1601', 16, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (32, 2, 5, '1602', 16, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (33, 2, 5, '1701', 17, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (34, 2, 5, '1702', 17, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (35, 2, 5, '1801', 18, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');
INSERT INTO `bus_room` VALUES (36, 2, 5, '1802', 18, 120.50, 0, NULL, 1, 0, '2026-04-30 23:27:00', '2026-04-30 23:27:00');

-- ----------------------------
-- Table structure for bus_room_owner
-- ----------------------------
DROP TABLE IF EXISTS `bus_room_owner`;
CREATE TABLE `bus_room_owner`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `owner_id` bigint NOT NULL COMMENT '业主ID',
  `relation_type` tinyint NULL DEFAULT 1 COMMENT '关系 (1:业主, 2:家属, 3:租客)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_room_id`(`room_id` ASC) USING BTREE,
  INDEX `idx_owner_id`(`owner_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间与住户关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_room_owner
-- ----------------------------
INSERT INTO `bus_room_owner` VALUES (1, 7, 1, 1, '2026-04-30 23:46:20', 0, '2026-04-30 23:46:20');
INSERT INTO `bus_room_owner` VALUES (2, 9, 2, 1, '2026-04-30 23:46:20', 0, '2026-04-30 23:46:20');
INSERT INTO `bus_room_owner` VALUES (3, 12, 3, 1, '2026-04-30 23:46:20', 0, '2026-04-30 23:46:20');
INSERT INTO `bus_room_owner` VALUES (4, 13, 4, 1, '2026-04-30 23:47:10', 0, '2026-04-30 23:47:10');

-- ----------------------------
-- Table structure for bus_unit
-- ----------------------------
DROP TABLE IF EXISTS `bus_unit`;
CREATE TABLE `bus_unit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '单元名称 (如: 1单元)',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_building_unit`(`building_id` ASC, `name` ASC) USING BTREE,
  INDEX `idx_building_id`(`building_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '单元表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bus_unit
-- ----------------------------
INSERT INTO `bus_unit` VALUES (1, 1, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (2, 1, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (3, 2, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (4, 2, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (5, 2, '3单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (6, 3, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (7, 3, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (8, 4, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (9, 4, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (10, 4, '3单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (11, 4, '4单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (12, 5, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (13, 6, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (14, 6, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (15, 7, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (16, 7, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (17, 7, '3单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (18, 8, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (19, 8, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (20, 9, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (21, 9, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (22, 10, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (23, 11, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (24, 12, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (25, 12, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (26, 12, '3单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (27, 13, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (28, 13, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (29, 14, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (30, 14, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (31, 14, '3单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (32, 14, '4单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (33, 15, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (34, 15, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (35, 16, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (36, 17, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (37, 17, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (38, 18, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (39, 18, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (40, 18, '3单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (41, 19, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (42, 19, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (43, 20, '1单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (44, 20, '2单元', 0, '2026-04-26 01:26:29', '2026-04-26 01:26:29');
INSERT INTO `bus_unit` VALUES (45, 8, '3单元', 1, '2026-04-26 01:56:34', '2026-04-26 01:58:33');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由路径 (如: bill)',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径 (如: business/bill/index)',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识 (如: bus:bill:pay)',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型 (M:目录, C:菜单, F:按钮)',
  `sort_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '仪表盘', 'dashboard', NULL, NULL, 'Odometer', 'C', 1, '2026-04-29 22:38:06');
INSERT INTO `sys_menu` VALUES (2, 0, '楼栋管理', 'building', NULL, NULL, 'OfficeBuilding', 'C', 2, '2026-04-29 22:38:06');
INSERT INTO `sys_menu` VALUES (3, 0, '单元管理', 'unit', NULL, NULL, 'Coordinate', 'C', 3, '2026-04-29 22:38:06');
INSERT INTO `sys_menu` VALUES (4, 0, '房屋管理', 'room', NULL, NULL, 'House', 'C', 4, '2026-04-29 22:38:06');
INSERT INTO `sys_menu` VALUES (5, 0, '业主管理', 'owner', NULL, NULL, 'User', 'C', 5, '2026-04-29 22:38:06');
INSERT INTO `sys_menu` VALUES (6, 0, '账单管理', 'bill', NULL, NULL, 'Money', 'C', 6, '2026-04-29 22:38:06');
INSERT INTO `sys_menu` VALUES (7, 0, '维修管理', 'repair', NULL, NULL, 'Tools', 'C', 7, '2026-04-29 22:38:06');
INSERT INTO `sys_menu` VALUES (8, 0, '公告管理', 'notice', NULL, NULL, 'Notification', 'C', 8, '2026-04-29 22:38:06');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称 (如: 超级管理员)',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限标识 (如: ADMIN, REPAIR_MAN)',
  `sort_order` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 (1:正常, 0:禁用)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ADMIN', 1, 1, NULL, '2026-04-29 22:37:29');
INSERT INTO `sys_role` VALUES (2, '维修员', 'REPAIR_MAN', 2, 1, NULL, '2026-04-29 23:31:34');
INSERT INTO `sys_role` VALUES (3, '业主', 'OWNER', 3, 1, NULL, '2026-04-29 23:31:34');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (1, 6);
INSERT INTO `sys_role_menu` VALUES (1, 7);
INSERT INTO `sys_role_menu` VALUES (1, 8);
INSERT INTO `sys_role_menu` VALUES (2, 7);
INSERT INTO `sys_role_menu` VALUES (2, 8);
INSERT INTO `sys_role_menu` VALUES (3, 6);
INSERT INTO `sys_role_menu` VALUES (3, 7);
INSERT INTO `sys_role_menu` VALUES (3, 8);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密后的密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 (1:正常, 0:禁用)',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$sjUFmQIarUF5gbnImIm3DefAhmopuMCuGTo2XS6vwZqCGwxw7OCCm', '超级管理员', 'https://elemecdn.com', NULL, NULL, 1, 0, '2026-04-28 22:47:48', '2026-04-29 23:25:35');
INSERT INTO `sys_user` VALUES (3, 'owner01', '$2a$10$BKqXoHU0BPIQiQtnr4u6M.bAHNiVDd.aHyxOKJsRcCfjR1fWGCWhO', '业主张大三', 'https://elemecdn.com', NULL, NULL, 1, 0, '2026-04-29 23:46:22', '2026-04-29 23:52:11');
INSERT INTO `sys_user` VALUES (4, 'repair1', '$2a$10$uRUQb1Pnt18emc/s2HzSOe9dARLF7VzsbgptZP6OgbLAjOHEp6Z16', '维修人员1', NULL, '13323332323', NULL, 1, 0, '2026-05-02 10:36:43', '2026-05-02 10:36:43');
INSERT INTO `sys_user` VALUES (7, 'worker_test01', '$2a$10$oNexKIJ1I/lEjjlnOGOip.tqJMmznp4eXOAyyuYwew5T4V4rQ6aRi', '张师傅', NULL, NULL, NULL, 1, 0, '2026-05-02 11:52:55', '2026-05-02 11:54:03');
INSERT INTO `sys_user` VALUES (8, '123', '$2a$10$ofY1vo9t7ZDBNhItJH7kSeMDOY1SGNMMPaGbFeTXxr6YYPwRIeuQC', 'abc', NULL, '13026555656', NULL, 1, 0, '2026-05-02 12:07:17', '2026-05-02 12:07:17');
INSERT INTO `sys_user` VALUES (9, 'lisi', '$2a$10$wlTIi8QFCA0j5JkH5FMw8OfI9Au69UMLAOQzde9kcwGbKSMcSzZ8q', '李四', NULL, '13999999999', NULL, 1, 0, '2026-05-02 12:07:57', '2026-05-02 12:07:57');
INSERT INTO `sys_user` VALUES (11, 'lisi1', '$2a$10$ZwMNrUkWhdZkzCWQUit08OHNpNC54Wv5hV.1pFZINruCJtz0X1Qmm', '李四', NULL, '13999929292', NULL, 1, 0, '2026-05-02 12:10:02', '2026-05-02 12:10:02');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (3, 3);
INSERT INTO `sys_user_role` VALUES (4, 2);
INSERT INTO `sys_user_role` VALUES (7, 2);
INSERT INTO `sys_user_role` VALUES (8, 2);
INSERT INTO `sys_user_role` VALUES (9, 3);
INSERT INTO `sys_user_role` VALUES (11, 3);

SET FOREIGN_KEY_CHECKS = 1;
