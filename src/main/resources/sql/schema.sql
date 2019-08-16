# 4BIT Scheme v 4.1
# Written by Hwang
# Updated by Lee
# Updated by Hong,Cho
# Date : 2019/08/08 (목)


DROP DATABASE IF EXISTS `project_4bit`;

CREATE DATABASE IF NOT EXISTS `project_4bit`
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;


DROP USER IF EXISTS `4bit`;
CREATE USER IF NOT EXISTS 'master'@'%' IDENTIFIED BY 'Test1234';
GRANT ALL PRIVILEGES ON project_4bit.* To 'master'@'%';

USE `project_4bit`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `admin`;
DROP TABLE IF EXISTS `article`;
DROP TABLE IF EXISTS `article_file`;
DROP TABLE IF EXISTS `attend_log`;
DROP TABLE IF EXISTS `board_type_list`;
DROP TABLE IF EXISTS `branch`;
DROP TABLE IF EXISTS `class_group`;
DROP TABLE IF EXISTS `class_teacher_log`;
DROP TABLE IF EXISTS `constraint_define`;
DROP TABLE IF EXISTS `homework`;
DROP TABLE IF EXISTS `hw_article`;
DROP TABLE IF EXISTS `hw_file`;
DROP TABLE IF EXISTS `hw_reply`;
DROP TABLE IF EXISTS `oauth_access_token`;
DROP TABLE IF EXISTS `oauth_client_details`;
DROP TABLE IF EXISTS `oauth_refresh_token`;
DROP TABLE IF EXISTS `point_log`;
DROP TABLE IF EXISTS `privilege`;
DROP TABLE IF EXISTS `quiz`;
DROP TABLE IF EXISTS `reply`;
DROP TABLE IF EXISTS `roadmap`;
DROP TABLE IF EXISTS `roadmap_exercise`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `role_privilege`;
DROP TABLE IF EXISTS `student`;
DROP TABLE IF EXISTS `student_test`;
DROP TABLE IF EXISTS `teacher`;
DROP TABLE IF EXISTS `test_group`;
DROP TABLE IF EXISTS `test_quiz`;
DROP TABLE IF EXISTS `user`;

-- 신분 테이블
CREATE TABLE IF NOT EXISTS `role` (
   `role_code`   		VARCHAR(36)   		NOT NULL,
   `role_name`   		VARCHAR(16)   		NOT NULL,

	PRIMARY KEY (`role_code`)

)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 권한 테이블
CREATE TABLE IF NOT EXISTS `privilege` (
   `privilege_code`   	VARCHAR(36)   		NOT NULL,
   `privilege_name`   	VARCHAR(16)   		NOT NULL,

   	PRIMARY KEY (`privilege_code`)

)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 신분_권한 테이블
CREATE TABLE IF NOT EXISTS `role_privilege` (
   `role_code`   		VARCHAR(36)   		NOT NULL,
   `privilege_code`   	VARCHAR(36)   		NOT NULL,

	UNIQUE (`role_code`, `privilege_code`),

	FOREIGN KEY (`role_code`) REFERENCES `role` (`role_code`),
	FOREIGN KEY (`privilege_code`) REFERENCES `privilege` (`privilege_code`)
) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 지점 테이블
CREATE TABLE IF NOT EXISTS  `branch` (
   `branch_code`   		VARCHAR(36)   	NOT NULL ,
   `branch_name`   		VARCHAR(36)   		NULL ,
   `branch_address`   	VARCHAR(36)   		NULL ,
   `branch_phone`   	VARCHAR(15)   		NULL,

	PRIMARY KEY (`branch_code`)

)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 반 테이블
CREATE TABLE IF NOT EXISTS  `class_group` (
   `class_id`   		BIGINT 	  		NOT NULL	  AUTO_INCREMENT,
   `class_name`   		VARCHAR(36)			NULL,
   `class_start_date`   DATETIME   	  		NULL,
   `class_end_date`   	DATETIME   	  		NULL,
   `subject`			VARCHAR(50)			NULL,
   `branch_code`   		VARCHAR(36)   	NOT NULL ,

    PRIMARY KEY (`class_id`),

    FOREIGN KEY (`branch_code`) REFERENCES `branch` (`branch_code`)
    ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 포털유저 테이블
CREATE TABLE IF NOT EXISTS `user` (
   `user_id` 	  		BIGINT   		NOT NULL 	AUTO_INCREMENT,
   `username`  			VARCHAR(36)  		NULL,
   `password`   		VARCHAR(36)   		NULL,
   `name`   			VARCHAR(10)   		NULL,
   `email`   			VARCHAR(30)   		NULL,
   `phone`   			VARCHAR(15)   		NULL,
   `point_sum`			INT					NULL,
   `user_level`			INT					NULL,
   `role_code`   		VARCHAR(36)   	NOT NULL,

   	PRIMARY KEY (`user_id`),

	FOREIGN KEY (`role_code`) REFERENCES `role` (`role_code`)
    ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 관리자 테이블
CREATE TABLE IF NOT EXISTS `admin` (
   `admin_id` 	  		BIGINT   		NOT NULL 	AUTO_INCREMENT,
   `user_id`  			BIGINT  		NOT NULL,
   `branch_code`   		VARCHAR(36)   		NULL,


   	PRIMARY KEY (`admin_id`),

	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`branch_code`) REFERENCES `branch` (`branch_code`)
    ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 강사 테이블
CREATE TABLE IF NOT EXISTS `teacher` (
   `teacher_id` 	  	BIGINT   		NOT NULL 	AUTO_INCREMENT,
   `user_id`   			BIGINT   		NOT NULL,

   	PRIMARY KEY (`teacher_id`),

	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 학생 테이블
CREATE TABLE IF NOT EXISTS `student` (
   `student_id` 		  	BIGINT   		NOT NULL 	AUTO_INCREMENT,
   `student_birth`			VARCHAR(8)  		NULL,
   `roadmap_last`  			INT 		  		NULL,
   `attend_count`   		INT 		 		NULL,
   `absent_count`   		INT			   		NULL,
   `late_count`   			INT		 	  		NULL,
   `out_count`				INT					NULL,
   `early_leave_count`		INT					NULL,
   `counsel`   				TEXT		   	NOT NULL,
   `user_id`				BIGINT			NOT NULL,
   `class_id`				BIGINT			NOT NULL,


   	PRIMARY KEY (`student_id`),

	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
   	FOREIGN KEY (`class_id`) REFERENCES `class_group` (`class_id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 포인트로그 테이블
CREATE TABLE IF NOT EXISTS `point_log` (
   `point_log_id`		BIGINT			NOT NULL AUTO_INCREMENT,
   `point_added`   		INT 		  		NULL,
   `point_from`   		VARCHAR(36)			NULL,
   `point_event_time` 	DATETIME   		NOT NULL,
   `user_id`  			BIGINT		  	NOT NULL,

    PRIMARY KEY (`point_log_id`),

	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 출석 로그 테이블
 CREATE TABLE IF NOT EXISTS `attend_log` (
   `attend_log_id`	    BIGINT				NOT NULL AUTO_INCREMENT,
   `event_name`  	 	ENUM('IN','OUT')	NOT NULL,
   `event_time`   		DATETIME  			NOT NULL,
   `student_id`   		BIGINT 	  			NOT NULL,

	PRIMARY KEY (`attend_log_id`),

    FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`)
	ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 반_강사_로그 테이블
 CREATE TABLE IF NOT EXISTS `class_teacher_log` (
   `class_teacher_log_id`	    BIGINT				NOT NULL AUTO_INCREMENT,
   `teacher_id`  	 			BIGINT				NOT NULL,
   `class_id`   				BIGINT  			NOT NULL,
   `class_teacher_event_time`  	DATETIME   			NOT NULL,
   `class_teacher_description`	TEXT				NULL,

	PRIMARY KEY (`class_teacher_log_id`),

    FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`)
	ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`class_id`) REFERENCES `class_group` (`class_id`)
	ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 제약정의 테이블
CREATE TABLE IF NOT EXISTS `constraint_define` (
   `constraint_name`   		VARCHAR(50)   	NOT NULL,
   `title_length`			INT				NULL,
   `content_length`			INT				NULL,
   `each_file_size_limit`	INT				NULL,
   `file_count`				INT				NULL,
   `total_file_size_limit`	INT				NULL,
   `reply_length`			INT				NULL,
   `article_depth_limit`	INT				NULL,
   `reply_depth_limit`		INT				NULL,

	PRIMARY KEY (`constraint_name`)

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 게시판종류목록 테이블
CREATE TABLE IF NOT EXISTS `board_type_list` (
   `board_id`   		VARCHAR(50)   		NOT NULL,
   `board_name`   		VARCHAR(50)  	 		NULL,
   `isnotice`			ENUM('Y','N')  		NOT NULL DEFAULT ('N'),
   `constraint_name`	VARCHAR(50)			NOT NULL,
   `class_id`			BIGINT				NOT NULL,

   	PRIMARY KEY (`board_id`),

   	FOREIGN KEY (`constraint_name`) REFERENCES `constraint_define` (`constraint_name`)
	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`class_id`) REFERENCES `class_group` (`class_id`)
	ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 게시물 테이블
CREATE TABLE IF NOT EXISTS `article` (
   `article_id` 	  		BIGINT 	  		NOT NULL  AUTO_INCREMENT,
   `article_number`   		INT   				NULL,
   `article_create_date`   	DATETIME   			NULL,
   `article_update_date`   	DATETIME   			NULL,
   `article_hits`   		INT   				NULL,
   `article_like`   		INT   				NULL,
   `group_id`   			INT		   			NULL,
   `depth`   				INT   				NULL,
   `sequence`   			INT   				NULL,
   `article_title`   		VARCHAR(50)   		NULL,
   `article_type`   		VARCHAR(36)   		NULL,
   `article_contents`   	TEXT		   		NULL,
   `isfile`   				ENUM('Y','N')   	NULL DEFAULT ('N'),
   `board_id`   			VARCHAR(50)		NOT NULL,
   `user_id`   				BIGINT			NOT NULL,

	PRIMARY KEY (`article_id`),

	FOREIGN KEY (`board_id`) REFERENCES `board_type_list` (`board_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 댓글 테이블
CREATE TABLE IF NOT EXISTS `reply` (
   `reply_id`   		BIGINT 	  		NOT NULL	 AUTO_INCREMENT,
   `reply_contents`   	VARCHAR(255)   		NULL,
   `reply_create_date`  DATETIME   			NULL,
   `reply_update_date`  DATETIME   			NULL,
   `reply_group_id`   	INT 	  			NULL,
   `reply_depth`   		INT   				NULL,
   `reply_sequence`   	INT   				NULL,
   `article_id`  	 	BIGINT 	  		NOT NULL,
   `user_id`  	 		BIGINT 	  		NOT NULL,

	PRIMARY KEY (`reply_id`),

	FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
   	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 게시물파일 테이블
CREATE TABLE IF NOT EXISTS `article_file` (
   `file_id`   			BIGINT 	  		NOT NULL AUTO_INCREMENT,
   `file_url`   		VARCHAR(255)   		NULL,
   `article_id`   		BIGINT 	  		NOT NULL,
   `file_name`   		VARCHAR(100) 	 	NULL,
   `file_size`   		INT 	  		NOT NULL,

	PRIMARY KEY (`file_id`),

	FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 시험 테이블
CREATE TABLE IF NOT EXISTS `test_group` (
   `test_id` 	  		BIGINT		   		NOT NULL 	AUTO_INCREMENT,
   `test_name`  	 	VARCHAR(50)   			NULL,
   `test_start_time`   	DATETIME   				NULL,
   `test_end_time`   	DATETIME   				NULL,
   `test_description`   TEXT		   			NULL,
   `sum`				INT						NULL,
   `avg`				DOUBLE					NULL,
   `max`				INT						NULL,
   `min`				INT						NULL,
   `user_id`			BIGINT				NOT NULL,
   `constraint_name`	VARCHAR(50)			NOT NULL,

	PRIMARY KEY (`test_id`),
																						-- 수정할 수 도 있다..
	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`constraint_name`) REFERENCES `constraint_define` (`constraint_name`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 문제 테이블
CREATE TABLE IF NOT EXISTS `quiz` (
   `quiz_id`   			BIGINT 	  			NOT NULL AUTO_INCREMENT,
   `quiz_contents`   	TEXT		   			NULL,
   `quiz_answer`   		VARCHAR(255)   			NULL,
   `quiz_each_score`   	INT   					NULL,
   `quiz_subject`		VARCHAR(36)				NULL,
   `quiz_chapter`		VARCHAR(36)				NULL,
   `quiz_level`			ENUM('상','중','하')	NULL DEFAULT('하'),
   `quiz_answertype`	INT						NULL,
   `quiz_explain`		TEXT					NULL,
   `user_id`			BIGINT				NOT NULL,
   `constraint_name`	VARCHAR(50)			NOT NULL,

	PRIMARY KEY (`quiz_id`),

  	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  	FOREIGN KEY (`constraint_name`) REFERENCES `constraint_define` (`constraint_name`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 시험_문제 테이블
CREATE TABLE IF NOT EXISTS `test_quiz` (
   `test_quiz_id`  		BIGINT   NOT NULL AUTO_INCREMENT,
   `test_id`			BIGINT	 NOT NULL,
   `quiz_id`   			BIGINT   NOT NULL,
   `test_quiz_order`	INT			 NULL,

	PRIMARY KEY (`test_quiz_id`),

  	FOREIGN KEY (`test_id`) REFERENCES `test_group` (`test_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
   	FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`quiz_id`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 학생_시험 테이블
CREATE TABLE IF NOT EXISTS `student_test` (
   `student_test_id`  		BIGINT			NOT NULL AUTO_INCREMENT,
   `st_answer`   			JSON				NULL,
   `st_test_score`			INT					NULL,
   `test_id`				BIGINT			NOT NULL,
   `user_id` 				BIGINT			NOT NULL,

   PRIMARY KEY (`student_test_id`),

    FOREIGN KEY (`test_id`) REFERENCES `test_group` (`test_id`)
	ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
	ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 과제 테이블
CREATE TABLE IF NOT EXISTS `homework` (
   `hw_id`   			BIGINT		   		NOT NULL 	AUTO_INCREMENT,
   `hw_name`   			VARCHAR(50)   		NULL,
   `hw_deadline`   		DATETIME   			NULL,
   `hw_create_date`   	DATETIME   			NULL,
   `hw_update_date`   	DATETIME   			NULL,
   `hw_subject`			VARCHAR(50)			NULL,
   `hw_description`   	TEXT   				NULL,
   `hw_teach_isfile`   	ENUM('Y','N') 		NULL DEFAULT('N'),
   `class_id`   		BIGINT 	  		NOT NULL,
   `user_id`			BIGINT			NOT NULL,
   `constraint_name`	VARCHAR(50)		NOT NULL,

	PRIMARY KEY (`hw_id`),

	FOREIGN KEY (`class_id`) REFERENCES `class_group` (`class_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`constraint_name`) REFERENCES `constraint_define` (`constraint_name`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 과제게시물 테이블
CREATE TABLE IF NOT EXISTS `hw_article` (
   `hw_article_id`   	BIGINT 	  	NOT NULL AUTO_INCREMENT,
   `hw_submit_date`   	DATETIME   		NULL,
   `hw_update_date`   	DATETIME   		NULL,
   `hw_contents`  	 	TEXT 		  	NULL,
   `hw_isfile`   		ENUM('Y','N')	NULL DEFAULT('N'),
   `hw_id`   			BIGINT		   	NOT NULL,
   `user_id`   			BIGINT		   	NOT NULL,

	PRIMARY KEY (`hw_article_id`),

	FOREIGN KEY (`hw_id`) REFERENCES `homework` (`hw_id`)
	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 과제제출파일 테이블
CREATE TABLE IF NOT EXISTS `hw_file` (
   `hw_file_id`   			BIGINT		   	NOT NULL AUTO_INCREMENT,
   `hw_file_url`   			VARCHAR(255)   		NULL,
   `hw_article_id`   		BIGINT 	  		NOT NULL,
   `hw_file_name`   		VARCHAR(100) 		NULL,
   `hw_file_size`   		INT 	  			NULL,


	PRIMARY KEY (`hw_file_id`),

	FOREIGN KEY (`hw_article_id`) REFERENCES `hw_article` (`hw_article_id`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 과제물댓글 테이블
CREATE TABLE IF NOT EXISTS `hw_reply` (
   `hw_reply_id`   			BIGINT		   	NOT NULL AUTO_INCREMENT,
   `hw_reply_contents`   	VARCHAR(255)   		NULL,
   `hw_reply_create_date`   DATETIME   			NULL,
   `hw_reply_update_date`   DATETIME   			NULL,
   `hw_article_id`   		BIGINT		   	NOT NULL,
   `user_id`   				BIGINT		   	NOT NULL,

	PRIMARY KEY (`hw_reply_id`),

	FOREIGN KEY (`hw_article_id`) REFERENCES `hw_article` (`hw_article_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 로드맵 테이블
CREATE TABLE IF NOT EXISTS `roadmap` (
   `roadmap_stage_no`   		INT   			NOT NULL,
   `roadmap_subject`   			VARCHAR(36)   		NULL,
   `roadmap_chapter`			VARCHAR(36)			NULL,

	PRIMARY KEY (`roadmap_stage_no`)

)ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

-- 로드맵연습문제 테이블
CREATE TABLE IF NOT EXISTS `roadmap_exercise` (
	`exercise_sequence`   		BIGINT   		NOT NULL AUTO_INCREMENT,
    `exercise_contents`   		TEXT  		 		NULL,
    `exercise_answer`			INT					NULL,
    `roadmap_stage_no`			INT				NOT NULL,

	PRIMARY KEY (`exercise_sequence`),

	FOREIGN KEY (`roadmap_stage_no`) REFERENCES `roadmap` (`roadmap_stage_no`)
    ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE = `InnoDB` DEFAULT CHARACTER SET = `utf8`;

--
-- Table structure for table `oauth_client_details`	사용
--
CREATE TABLE IF NOT EXISTS `oauth_client_details` (
  `client_id`               VARCHAR(255) PRIMARY KEY,
  `resource_ids`            VARCHAR(255),
  `client_secret`           VARCHAR(255),
  `scope`                   VARCHAR(255),
  `authorized_grant_types`  VARCHAR(255),
  `web_server_redirect_uri` VARCHAR(255),
  `authorities`             VARCHAR(255),
  `access_token_validity`   INTEGER,
  `refresh_token_validity`  INTEGER,
  `additional_information`  VARCHAR(4096),
  `autoapprove`             VARCHAR(255)
) ENGINE = `MyISAM` DEFAULT CHARACTER SET = `utf8`;

--

--
-- Table structure for table `oauth_access_token`		사용
--
CREATE TABLE IF NOT EXISTS `oauth_access_token` (
  `token_id`                VARCHAR(255),
  `token`                   BLOB,
  `authentication_id`       VARCHAR(255) PRIMARY KEY,
  `user_name`               VARCHAR(255),
  `client_id`               VARCHAR(255),
  `authentication`          BLOB,
  `refresh_token`           VARCHAR(255)
) ENGINE = `MyISAM` DEFAULT CHARACTER SET = `utf8`;

--
-- Table structure for table `oauth_refresh_token`	사용
--
CREATE TABLE IF NOT EXISTS `oauth_refresh_token` (
  `token_id`                VARCHAR(255),
  `token`                   BLOB,
  `authentication`          BLOB
) ENGINE = `MyISAM` DEFAULT CHARACTER SET = `utf8`;



-- INSERT문 -------------------------------------------------------------------------------------------------------------
-- 이쪽 수정해야됨.
SET @client_id = '762f6bbb-a257-11e9-9b39-0242ac120002';
SET @client_secret = 'c16b2a8b36678a7440caeda356534ef2fa75699098bb7d58d499541024e53a51';
SET @resource_ids = 'todo-server-rest-api';
SET @authorized_grant_types = 'password,authorization_code,refresh_token,client_credentials';
SET @scope = 'read,write';
SET @authorities = 'ROLE_GUEST';
SET @access_token_validity = 3600;
SET @refresh_token_validity = 86400;

INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `authorities`, `access_token_validity`, `refresh_token_validity`, `autoapprove`)
  VALUES (@client_id, @resource_ids, @client_secret, @scope, @authorized_grant_types, @authorities, @access_token_validity, @refresh_token_validity, true);

INSERT INTO `role` VALUE
('role_guest', 'ROLE_GUEST'),
('role_admin','ROLE_ADMIN'),
('role_teacher','ROLE_TEACHER'),
('role_student','ROLE_STUDENT');


-- * 이쪽 더 세분화 해야됨. ex) read_member, read_me, read_freeboard, read_job, read_project,... etc
-- membermanage == All user에 관한 권한
-- me == 나의 정보에 관한 권한
-- notice == 전체공지에 대한 권한
-- job == 취업게시판에 대한 권한
-- project == 영상 게시판에 대한 권한
-- classnotice == 반별 공지에 대한 권한
-- testteacher == 시험출제에 대한 권한
-- teststudent == 시험응시에 대한 권한
-- test == 시험 리스트에 대한 권한
-- assignmentteacher == 과제 출제에 대한 권한
-- assignmentstudent == 과제 제출에 대한 권한
-- assignment == 과제 리스트에 대한 권한
-- classboard == 반별 자유게시판에 대한 권한
-- library == 반별 자료게시판에 대한 권한
-- roadmap == 로드맵에 대한 권한 (일단은 관리자가 등록하는 것이 아니다.)

INSERT INTO `privilege` VALUE
-- ('read_member','READ_MEMBER'),
-- ('write_member','WRITE_MEMBER'),
-- ('read_class','READ_CLASS'),
-- ('write_class','WRITE_CLASS'),
-- ('read_board','READ_BOARD'),
-- ('write_board','WRITE_BOARD'),
-- ('read_test','READ_TEST'),
-- ('write_test','WRITE_TEST'),
-- ('read_assignment','READ_ASSIGNMENT'),
-- ('write_assignment','WRITE_ASSIGNMENT');
('read_member','READ_MEMBER'),
('write_member','WRITE_MEMBER'),

('read_me','READ_ME'),
('write_me','WRITE_ME'),

('read_notice','READ_NOTICE'),
('write_notice','WRITE_NOTICE'),

('read_job','READ_JOB'),
('write_job','WRITE_JOB'),

('read_pro','READ_PRO'),
('write_pro','WRITE_PRO'),

('read_cnotice','READ_CNOTICE'),
('write_cnotice','WRITE_CNOTICE'),

('read_ttest','READ_TTEST'),
('write_ttest','WRITE_TTEST'),

('read_stest','READ_STEST'),
('write_stest','WRITE_STEST'),

('read_test','READ_TEST'),
('write_test','WRITE_TEST'),

('read_thw','READ_THW'),
('write_thw','WRITE_THW'),

('read_shw','READ_SHW'),
('write_shw','WRITE_SHW'),

('read_hw','READ_HW'),
('write_hw','WRITE_HW'),

('read_cboard','READ_CBOARD'),
('write_cboard','WRITE_CBOARD'),

('read_library','READ_LIBRARY'),
('write_library','WRITE_LIBRARY'),

('read_roadmap','READ_ROADMAP'),
('write_roadmap','WRITE_ROADMAP');


-- * 이쪽도 더 세분화 해야됨
INSERT INTO `role_privilege` VALUE
('role_admin','read_member'),
('role_teacher','read_member'),
('role_student','read_member'),
('role_admin','write_member'),
('role_teacher','write_member'),
('role_student','write_member'),
-- --------------------------------------
('role_admin','read_me'),
('role_teacher','read_me'),
('role_student','read_me'),
('role_admin','write_me'),
('role_teacher','write_me'),
('role_student','write_me'),
-- --------------------------------------
('role_admin','read_notice'),
('role_teacher','read_notice'),
('role_student','read_notice'),
('role_admin','write_notice'),
('role_teacher','write_notice'),
('role_student','write_notice'),
-- --------------------------------------
('role_admin','read_job'),
('role_teacher','read_job'),
('role_student','read_job'),
('role_admin','write_job'),
('role_teacher','write_job'),
('role_student','write_job'),
-- --------------------------------------
('role_admin','read_pro'),
('role_teacher','read_pro'),
('role_student','read_pro'),
('role_admin','write_pro'),
('role_teacher','write_pro'),
('role_student','write_pro'),
-- ----------------------------------------
('role_admin','read_cnotice'),
('role_teacher','read_cnotice'),
('role_student','read_cnotice'),
('role_admin','write_cnotice'),
('role_teacher','write_cnotice'),
('role_student','write_cnotice'),
-- ----------------------------------------
('role_admin','read_ttest'),
('role_teacher','read_ttest'),
('role_student','read_ttest'),
('role_admin','write_ttest'),
('role_teacher','write_ttest'),
('role_student','write_ttest'),
-- -------------------------------------
('role_admin','read_stest'),
('role_teacher','read_stest'),
('role_student','read_stest'),
('role_admin','write_stest'),
('role_teacher','write_stest'),
('role_student','write_stest'),
-- ----------------------------------
('role_student','read_test'),
('role_teacher','read_test'),
('role_admin','read_test'),
('role_student','write_test'),
('role_teacher','write_test'),
('role_admin','write_test'),
-- ------------------------------------
('role_student','read_thw'),
('role_teacher','read_thw'),
('role_admin','read_thw'),
('role_student','write_thw'),
('role_teacher','write_thw'),
('role_admin','write_thw'),
-- -----------------------------------
('role_admin','read_shw'),
('role_teacher','read_shw'),
('role_student','read_shw'),
('role_admin','write_shw'),
('role_teacher','write_shw'),
('role_student','write_shw'),
-- -----------------------------------
('role_admin','read_hw'),
('role_teacher','read_hw'),
('role_student','read_hw'),
('role_admin','write_hw'),
('role_teacher','write_hw'),
('role_student','write_hw'),
-- -----------------------------------
('role_admin','read_cboard'),
('role_teacher','read_cboard'),
('role_student','read_cboard'),
('role_admin','write_cboard'),
('role_teacher','write_cboard'),
('role_student','write_cboard'),
-- -----------------------------------
('role_admin','read_library'),
('role_teacher','read_library'),
('role_student','read_library'),
('role_admin','write_library'),
('role_teacher','write_library'),
('role_student','write_library'),
-- -----------------------------------
('role_admin','read_roadmap'),
('role_teacher','read_roadmap'),
('role_student','read_roadmap'),
('role_admin','write_roadmap'),
('role_teacher','write_roadmap'),
('role_student','write_roadmap');
-- --------------------------------------

INSERT INTO `branch` VALUE
('sinchon',	'신촌','마포구','02-111-1111'),
('gangnam',	'강남','강남구','02-222-2222'),
('seocho',	'서초','서초구','02-333-3333');

INSERT INTO `class_group` (`class_name`,`class_start_date`,`class_end_date`,`subject`,`branch_code`) VALUE
('관리자용더미데이터',			 	 null,null,null,'sinchon'),
('java 웹 개발자 양성과정',			 '2019-02-26 00:00:00','2019-08-26 00:00:00','java-web',		'sinchon'),
('java 어플리케이션 개발자 양성과정','2019-02-26 00:00:00','2019-08-26 00:00:00','java-application','gangnam');




-- (PK 유저고유번호) 로그인이름, 비번, 이름, 메일, 연락처, 포인트합계, 유저레벨, 신분코드
INSERT INTO `user` (`username` ,`password`,`name`,`email`,`phone`, `point_sum`, `user_level`, `role_code`)
VALUE
('2019022601','1234','황OO','kim@bitcamp.com','010-1111-1111' ,    50,   2,'role_student'),
('2019022602','1234','이OO','lee@bitcamp.com','010-2222-2222' ,    10,   1,'role_student'),
('2019022603','1234','박OO','park@bitcamp.com','010-3333-3333',     5,   1,'role_student'),
('2019022604','1234','조OO','cho@bitcamp.com','010-4444-4444' ,     0,   1, 'role_student'),
('2019022605','1234','홍OO','hong@bitcamp.com','010-5555-5555',   100,   3,'role_student'),
('2018001','1234','김강사','kim@bitcamp.com','010-1234-1234'  ,100000,1000,'role_teacher'),
('2018002','1234','이강사','lee@bitcamp.com','010-2345-2345'  ,100000,1000,'role_teacher'),
('2019001','1234','박강사','park@bitcamp.com','010-3456-3456' ,100000,1000,'role_teacher'),
('1000','1234','신촌관리자','seocho@bitcamp.com','02-111-1111'	  ,100000,1000,'role_admin'),
('2000','1234','서초관리자','sinchon@bitcamp.com','02-222-2222'	  ,100000,1000,'role_admin'),
('test_s','1234','학생테스트','test@bitcamp.com','010-1111-2223' ,    50,   2,'role_student'),
('test_t','1234','강사테스트','park@bitcamp.com','010-3456-3456' ,100000,1000,'role_teacher'),
('test_a','1234','신촌관리','sinchon@bitcamp.com','02-222-2222'	  ,100000,1000,'role_admin');

-- 학생 테이블
-- 생년월일, 로드맵단계, 유저_고유번호, 반_고유번호, 출석, 결석, 지각, 외출, 조퇴, 상담내용
-- 로드맵 단계는 5-5 == 505  식으로 표현
INSERT INTO `student` (`student_birth`,`roadmap_last`,`user_id`,`class_id`,`attend_count`,`absent_count`,`late_count`,`out_count`,`early_leave_count`,`counsel`)
VALUE
('19890512','505','1','1',  98, 2, 1, 0, 1, '이쁘다'),
('19920615','505','2','1', 100, 0, 1, 0, 1, '열심히한다'),
('19990909','101','3','2', 90, 10, 0, 0, 1, '못생겼다'),
('19930720','505','4','2', 100, 0, 0, 0, 1, '잘생겼다'),
('19950102','505','5','1', 100, 0, 0, 0, 1, '바보다'),
('19950102','101','11','1', 100, 0, 0, 0, 1, '테스트다');

-- 강사 테이블
-- 강사_고유번호(PK) , 유저_고유번호(FK)
INSERT INTO `teacher` (`user_id`)
VALUE
('6'),
('7'),
('8'),
('12');

-- 관리자 테이블
-- 관리자_고유번호(PK), 유저고유번호(FK), 지점코드(FK)
INSERT INTO `admin`(`user_id`, `branch_code`)
value
('9' ,'sinchon'),
('10','seocho'),
('13','sinchon');


-- 제약정의 insert
-- 제약이름, 제목글자수, 내용글자수, 개당첨부파일크기, 파일첨부개수, 총파일크기, 댓글글자수, 게시물뎁쓰제한, 댓글뎁쓰제한
INSERT INTO `constraint_define` VALUE
('notice_constraint',20,500,300,5,1024,100,1,3),
('job_constraint',20,500,300,5,1024,100,1,3),
('project_constraint',20,500,300,5,1024,100,1,3),
('class_notice_constraint',20,500,300,5,1024,100,1,3),
('class_board_constraint',20,500,300,5,1024,100,1,3),
('class_library_constraint',20,500,300,5,1024,100,1,3),
('homework_constraint',20,500,300,5,1024,100,1,3),
('test_constraint',20,500,300,5,1024,100,1,3),
('quiz_constraint',20,500,300,5,1024,100,1,3);



-- PK : 게시판 영문명,   게시판한글명 공지표시유무, 제약이름, 반_고유번호
INSERT INTO `board_type_list` VALUE
('notice',			'전체공지게시판',			'Y','notice_constraint',	  '1'),
('job',				'취업공고게시판',			'Y','job_constraint' ,	      '1'),
('project',			'프로젝트 발표영상 게시판',	'N','project_constraint',	  '1'),
('class_1_notice',	'1반 공지게시판',			'N','class_notice_constraint','2'),
('class_1_board',	'1반 자유게시판',			'N','class_board_constraint' ,'2'),
('class_1_library',	'1반 자료게시판',			'N','class_library_constraint','2'),
('class_2_notice',	'2반 공지게시판',			'N','class_notice_constraint', '3'),
('class_2_board',	'2반 자유게시판',			'N','class_board_constraint' , '3'),
('class_2_library',	'2반 자료게시판',			'N','class_library_constraint','3');

-- user_id == 1 ~ 5 : student
--             6 ~ 8 : teacher
--            9 ~ 10 : admin
INSERT INTO `article`
(`article_number` ,`article_create_date`,`article_update_date`,`article_hits`,`article_like`,`group_id` ,`depth`,`sequence`,
`article_title`, `article_type`, `article_contents`, `isfile`, `board_id`, `user_id`)
VALUE
(1, '2019-02-26 00:00:00', '2019-02-26 00:00:00', 30, 5, 1, 1, 1, '사무실은 3층입니다','전체공지게시판', '문앞에 바로 있습니다', 'N', 'notice', 9),
(2, '2019-03-15 15:12:20', '2019-03-15 15:12:20', 40, 7, 1, 1, 1, '흡연장소 안내','반 자유게시판', '1층 뒤쪽 정원', 'N', 'class_1_board', 2),
(3, '2019-04-11 12:58:10', '2019-04-11 12:58:10', 38, 6, 1, 1, 1, '반복문 과제해', '전체공지게시판', '4월 20일까지', 'N', 'notice', 8),
(1, '2019-04-28 15:05:12', '2019-04-28 15:06:20', 60, 8, 1, 1, 1, '노동부 취업', '취업공고게시판', NULL, 'Y', 'job', 9 ),
(1, '2019-05-19 20:12:08', '2019-05-19 20:12:08', 45, 5, 1, 1, 1, '스티브짱', '반 공지게시판', '고맙습니다.', 'Y', 'class_1_notice', 7),
(1, '2019-06-04 12:45:10', '2019-06-04 13:10:10', 49, 6, 1, 1, 1, '옆강의실 없어서 너무 좋다', '반 자유게시판', 'ㅈㄱㄴ', 'N', 'class_1_board', 1);

INSERT INTO `article_file` (`file_url` ,`article_id`,`file_name`,`file_size`) VALUE
('파일주소1', 4, '파일명1', 50),
('파일주소2', 4, '파일명2', 100),
('파일주소3', 5, '파일명3', 300);

-- user_id == 1 ~ 5 : student
--             6 ~ 8 : teacher
--            9 ~ 10 : admin
INSERT INTO `reply` (`reply_contents`,`reply_create_date`,`reply_update_date`,
`reply_group_id`, `reply_depth` ,`reply_sequence`,`article_id`,`user_id`)
VALUE
('넵', '2019-02-26 10:10:00', '2019-02-26 10:10:00', 1, 1, 1, 1,1),
('화장실 완전 쾌적', '2019-04-11 12:59:10','2019-04-11 13:00:10', 1, 1, 1, 3,3),
('옆반 끝까지 안왔으면..', '2019-06-04 13:45:12', '2019-06-04 13:45:12', 1, 1, 1, 6,7);

-- 선생님 출제
INSERT INTO `homework` (`hw_name`, `hw_deadline`, `hw_create_date`, `hw_update_date`,
 `hw_subject`, `hw_description`, `hw_teach_isfile`,`class_id`,`user_id`,`constraint_name`)
VALUE
('JAVA 조건문',       '2019-03-13 00:00:00', '2019-03-11 00:00:00','2019-03-11 00:00:00', 'JAVA',   '과제내용', 'N',1,'6','homework_constraint'),
('JAVA 반복문',     '2019-03-19 00:00:00', '2019-03-16 00:00:00','2019-03-16 00:00:00', 'JAVA',   '과제내용', 'N',1,'7','homework_constraint'),
('JAVA 알고리즘',    '2019-06-27 00:00:00', '2019-06-16 00:00:00','2019-06-16 00:00:00', 'JAVA',   '과제내용', 'N',2,'6','homework_constraint'),
('JAVA 알고리즘',    '2019-07-23 00:00:00', '2019-07-20 00:00:00','2019-07-20 00:00:00', 'JAVA',   '과제내용', 'N',2,'7','homework_constraint'),
('DB 기초',       '2019-07-22 00:00:00', '2019-07-16 00:00:00','2019-07-16 00:00:00', 'DB',     '과제내용', 'N',2,'6','homework_constraint'),
('TODO 만들기',    '2019-04-25 00:00:00', '2019-04-16 00:00:00','2019-04-16 00:00:00', 'Spring', '과제내용', 'N',2,'7','homework_constraint');

-- 학생 제출
INSERT INTO `hw_article` (`hw_submit_date`, `hw_update_date`, `hw_contents`, `hw_isfile`,
 `hw_id`, `user_id`)
VALUE
('2019-04-20 15:23:18', '2019-04-20 00:00:00', '과제내용1', 'N', 1, 1),
('2019-03-20 20:03:00', '2019-03-20 00:00:00', '과제내용1', 'N', 2, 3),
('2019-06-26 15:10:00', '2019-06-26 00:00:00', '과제내용2', 'Y', 3, 2),
('2019-03-18 13:38:00', '2019-03-19 08:12:00', '과제내용3', 'Y', 4, 4),
('2019-07-21 19:08:00', '2019-07-21 00:00:00', '과제내용4', 'N', 5, 5),
('2019-03-12 21:12:00', '2019-03-12 00:00:00', '과제내용5', 'N', 6, 1);

INSERT INTO `hw_reply` (`hw_reply_contents`,`hw_reply_create_date`,
 `hw_reply_update_date`, `hw_article_id`,`user_id`)
VALUE
('왜이거야',       '2019-04-20 20:21:18', '2019-04-20 20:21:18', 1, 5),
('선생님이지롱',    '2019-03-23 10:45:31', '2019-03-23 10:45:31', 2, 6),
('나도몰라',       '2019-06-26 18:21:03', '2019-06-26 19:32:43', 3, 3),
('선생님이야',       '2019-03-18 14:21:12', '2019-03-18 14:21:12', 4, 7),
('알려주세요',       '2019-07-21 19:08:00', '2019-07-21 19:08:00', 5, 4);

INSERT INTO `hw_file` (`hw_file_url` ,`hw_article_id`,`hw_file_name`, `hw_file_size`)
VALUE
('www.naver.com', 3, '숙제파일1', 40),
('www.naver.com', 4, '숙제파일2', 40);

INSERT INTO `test_group` (`test_name`,`test_start_time`, `test_end_time`,  `test_description`, `sum`,`avg`,
`max`,`min`,`user_id`,`constraint_name` )
VALUE
('자바클래스',   '2019-07-29 21:00:00', '2019-07-29 22:00:59', '자바 클래스에 대해 알아보자', '1000','60','100','30','6','test_constraint'),
('메서드',      '2019-07-28 19:23:05', '2019-07-28 20:24:59', '메서드에 대해 알아보자',       '1000','70','100','0','7','test_constraint' ),
('스레드',      '2019-07-29 17:00:25', '2019-07-27 20:22:59', '스레드에 대해 알아보자',       '1000','40','100','10','6','test_constraint');

-- 학생_시험_고유번호 , 학생답(json), 개인시험점수, 시험_고유번호, 유저_고유번호
INSERT INTO `student_test` VALUE
(1,'{"1":1, "2":2,"3":3,"4":2,"5":"자바","6":3,"7":1,"8":3,"9":"해피","10":"초콜릿"}',100,1,11);

INSERT INTO `student_test` VALUE
(2,'{"1":2, "2":4,"3":1,"4":3,"5":1,"6":"새드","7":"과자","8":3,"9":1,"10":"풍선껌"}',50,2,11);

INSERT INTO `quiz` (`quiz_contents`,`quiz_answer`,`quiz_each_score`,`quiz_subject`,
`quiz_chapter`,`quiz_level`,`quiz_answertype`,`quiz_explain`,`user_id`,`constraint_name`) VALUE
('Linear Search의 평균 검색 회수는? ① n-1   ② (n＋1)/2   ③ n   ④ n/2','2', 5, '데이터 베이스', '자료구조','상',1,'-',6,'quiz_constraint'),
('데이터베이스 설계 단계 중 저장 레코드 양식 설계, 레코드 집중의 분석 및 설계, 접근 경로 설계와 관계되는 것은? ① 논리적 설계  ② 요구 조건 분석  ③ 물리적 설계  ④ 개념적 설계','3', 5, '데이터 베이스', 'DB설계','하',1,'-',6,'quiz_constraint'),
('3NF에서 BCNF가 되기 위한 조건은? ① 이행적 함수 종속 제거 ② 부분적 함수 종속 제거 ③ 다치 종속 제거 ④ 결정자이면서 후보키가 아닌 것 제거','4', 5, '데이터 베이스', '정규화','하',1, '-',6,'quiz_constraint');

INSERT INTO `roadmap` VALUE
(1,'JAVA','반복문'),
(2,'JAVA','조건문'),
(3,'JAVA','MVC'),
(4,'DB','기초'),
(5,'JAVA','Spring'),
(6,'DB','초급');

INSERT INTO `roadmap_exercise` VALUE
(1,'문제본문', 1, 1),
(2,'문제본문', 2, 2),
(3,'문제본문', 3, 3),
(4,'문제본문', 4, 4),
(5,'문제본문', 1, 5),
(6,'문제본문', 2, 6);

INSERT INTO `test_quiz` (`test_id`,`quiz_id`,`test_quiz_order`)
VALUE
(1,1,5);


-- ----------------------------------------- SELECT 문 ---------------------------------------------------------
-- 전체 유저 검색
SELECT * FROM project_4bit.user;

-- 전체 학생 검색
SELECT * FROM project_4bit.student;

-- 전체 강사 검색
SELECT * FROM project_4bit.teacher;

-- 전체 관리자 검색
SELECT * FROM project_4bit.admin;

-- 유저 중에서 name = 황OO 검색
SELECT * FROM project_4bit.user
WHERE name = '황OO';

-- 유저 중에서 학생 ID 가 1 인 학생 검색
SELECT * FROM project_4bit.user u
JOIN project_4bit.student s
ON u.user_id = s.user_id
WHERE s.student_id = 1;

-- 유저 중에서 강사 ID 가 1 인 강사 검색
SELECT * FROM project_4bit.user u
JOIN project_4bit.teacher t
ON u.user_id = t.user_id
WHERE t.teacher_id = 1;

-- 유저 중에서 관리자 ID 가 1 인 관리자 검색
SELECT * FROM project_4bit.user u
JOIN project_4bit.admin a
ON u.user_id = a.user_id
WHERE a.admin_id = 1;










