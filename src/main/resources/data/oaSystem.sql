drop database if exists oaSystem;
create database if not exists oaSystem
    default character set utf8
    default collate utf8_general_ci;   
    
use oaSystem;

drop table if exists oa_user;
create table oa_user(
   id bigint not null auto_increment primary key comment '主键',
   user_id varchar(11) not null comment '用户的工号',
   user_name varchar(20) not null comment '用户名',
   password varchar(100) not null comment '用户密码',
   answer1 varchar(50) default null comment '问题1答案,默认为空',
   answer2 varchar(50) default null comment '问题2答案,默认为空',
   email varchar(20) comment '用户邮箱',
   phone_num varchar(11) comment '用户手机',
   dept_id varchar(10) not null comment '部门Id',
   flag int default 1 comment '标志符 0 离职，1在职',
   create_time dateTime not null  comment '创建时间',
   update_time dateTime not null  comment '修改时间',
   key depyIdIndex(dept_Id),
   unique key useridUnique (user_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment '用户表';

insert into oa_user(id,user_id,user_name,password,answer1,answer2,email,phone_num,dept_id,flag,create_time,update_time)
values(1,"11","shujia","11","1","1","22@qq.com","111111","20170101","1",now(),now());

insert into oa_user(id,user_id,user_name,password,answer1,answer2,email,phone_num,dept_id,flag,create_time,update_time)
values(2,"12","shujia","11","1","1","22@qq.com","111111","20170101","1",now(),now());

drop table if exists oa_role;
create table oa_role(
  id bigint not null primary key auto_increment comment '角色Id',
  role_id bigint not null comment '物理主键',
  role_zh_name varchar(10) not null comment '角色中文名称',
  role_name varchar(30) not null comment '角色名称',
   unique key roleidUnique (role_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment '角色表';

insert into oa_role(id,role_id,role_name,role_zh_name) values(1,1,"admin","超级管理员");
insert into oa_role(id,role_id,role_name,role_zh_name) values(2,2,"boss","董事长");
insert into oa_role(id,role_id,role_name,role_zh_name) values(3,3,"generalManager","总经理");
insert into oa_role(id,role_id,role_name,role_zh_name) values(4,4,"financeManager","财务部门经理");
insert into oa_role(id,role_id,role_name,role_zh_name) values(5,5,"financeAssistant","财务部门助理");
insert into oa_role(id,role_id,role_name,role_zh_name) values(6,6,"hrManager","人事行政部门经理");
insert into oa_role(id,role_id,role_name,role_zh_name) values(7,7,"hrAssistant","人事行政部门助理");
insert into oa_role(id,role_id,role_name,role_zh_name) values(8,8,"lifeManager","后勤部门经理");
insert into oa_role(id,role_id,role_name,role_zh_name) values(9,9,"lifeAssistant","后勤部门助理");
insert into oa_role(id,role_id,role_name,role_zh_name) values(10,10,"worker","员工");
insert into oa_role(id,role_id,role_name,role_zh_name) values(11,11,"doorkeeper","保安");





drop table if exists user_role;
create table user_role(
  user_id bigint not null,
  role_id bigint not null,
  primary key(user_id,role_id)
);

drop table if exists oa_overtime;
create table oa_overtime(
  id bigint not null auto_increment primary key comment '主键',
  overtime_id varchar(11) not null  comment '逻辑主键 用与用户多对多',
  dept_id varchar(10) not null comment '部门Id',
  from_time varchar(20) not null comment '开始时间',
  to_time varchar(20) not null comment '计划结束时间',
  descs varchar(50) not null comment'加班描述',
  dept_opinion varchar(50)  comment '部门领导意见',
  hr_opinion varchar(50)  comment '人事行政领导意见',
  real_time dateTime comment'实际结束时间',
  flag int  default 0 comment '标志符 0 不通过，1通过 默认不通过',
  process_instance_id varchar(10)  comment '流程实例Id',
  create_time datetime not null ,
  update_time dateTime not null,
  unique key useridIndexUnique (overtime_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment '加班表';

drop table if exists oa_leave;
create table oa_leave(
  id bigint not null primary key auto_increment comment '主键',
  leave_id varchar(11) not null comment '逻辑主键 与 用户多对多',
  dept_id varchar(10) not null comment '部门Id',
  descs varchar(50) not null comment '请假描述',
  from_time varchar(20) not null comment '请假开始时间',
  to_time varchar(20) not null comment '请假结束时间',
  flag int  default 0 comment '标志符 0 不通过，1通过 默认不通过',
  dept_opinion varchar(50)  comment '部门领导意见',
  hr_opinion varchar(50)  comment '人事行政领导意见',
  process_instance_id varchar(10) comment '流程实例Id',
  create_time dateTime not null,
  update_time datetime not null,
  unique key useridIndexUnique (leave_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment'请假表';

drop table if exists oa_business_trip;
create table  oa_business_trip(
  id bigint not null primary key auto_increment comment'物理主键',
  business_id varchar(100) not null comment '逻辑主键 与 用户多对多',
  dept_id varchar(10) not null comment '部门Id',
  descs varchar(50) not null comment '外出描述',
  from_time varchar(20) not null comment '外出开始时间',
  to_time varchar(20) not null comment '外出结束时间',
  from_location varchar(20) not null comment '外出出发地点',
  to_location varchar(20) not null comment '外出目标地点',
  phone_num varchar(11) comment '联系电话',
  flag int  default 0 comment '标志符 0 不通过，1通过 默认不通过',
  dept_opinion varchar(50)  comment '部门领导意见',
  hr_opinion varchar(50) comment '人事行政领导意见',
  car_type varchar(10) comment '分配汽车类型',
  process_instance_id varchar(10) comment '流程实例Id',
  create_time dateTime not null,
  update_time datetime not null,
  unique key useridIndexUnique (business_id) using Btree -- 使用B树建立索引
)engine=InnODB comment '外出表';

drop table if exists oa_eating;
create table oa_eating(
  id bigint not null primary key auto_increment comment'主键',
  eating_id varchar(11) not null comment '逻辑主键 与 用户多对多',
  dept_id varchar(10) not null comment '部门Id',
  lunch int comment '午餐',
  dinner int comment'晚餐',
  night_snack int comment '夜宵',
  breakfast int comment '早餐',
  hr_opinion varchar(10) comment '人事意见',
  flag int  default 0 comment '标志符 0 不通过，1通过 默认不通过',
  process_instance_id varchar(10)  comment '流程实例Id',
  create_time dateTime not null,
  update_time datetime not null,
  unique key useridIndexUnique (eating_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment'报餐表';

drop table if exists oa_department;
create table oa_department(
 id bigint not null auto_increment primary key comment'物理主键',
 dept_id varchar(10) not null unique comment '部门id',
 dept_name varchar(10) not null comment '部门名字',
 parent_id varchar(6) not null comment '分总部id',
 create_time dateTime not null,
 update_time datetime not null,
 unique key useridIndexUnique (dept_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment '部门表';

-- 珠海分部部门表数据
insert into oa_department(id,dept_id,dept_name,parent_id,create_time,update_time)
       values(1,"20170101","人事行政部","201701",now(),now());
insert into oa_department(id,dept_id,dept_name,parent_id,create_time,update_time)
       values(2,"20170102","财务部","201701",now(),now());
insert into oa_department(id,dept_id,dept_name,parent_id,create_time,update_time)
       values(3,"20170103","后勤部","201701",now(),now());
-- 广州分部部门表数据
insert into oa_department(id,dept_id,dept_name,parent_id,create_time,update_time)
       values(4,"20170201","人事行政部","201702",now(),now());
insert into oa_department(id,dept_id,dept_name,parent_id,create_time,update_time)
       values(5,"20170202","财务部","201702",now(),now());
insert into oa_department(id,dept_id,dept_name,parent_id,create_time,update_time)
       values(6,"20170203","后勤部","201702",now(),now());


drop table if exists oa_head_department;
create table oa_head_department(
 id bigint not null primary key auto_increment comment '物理主键',
 head_dept_id varchar(10) not null  comment '总部门id',
 head_dept_name varchar(10) not null comment '总部门名字',
 create_time dateTime not null,
 update_time datetime not null,
 unique key useridIndexUnique (head_dept_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment '分总部表';

-- 分总部表 01 珠海总部 02 广州总部
insert into  oa_head_department(id,head_dept_id,head_dept_name,create_time,update_time)
             values (1,"201701","珠海分总部",now(),now());
insert into  oa_head_department(id,head_dept_id,head_dept_name,create_time,update_time)
             values (2,"201702","广州分总部",now(),now());


drop table if exists oa_menu;
create table oa_menu(
  id bigint not null primary key auto_increment,
   menu_id bigint not null ,
   role_Id bigint  comment '逻辑主键 与 角色多对多',
   menu_name varchar(30) not null comment '菜单名字',
   url varchar(50) not null,
   unique key useridIndexUnique (menu_id) using Btree -- 使用B树建立索引
)engine=InnoDB comment'菜单表';


-- activiti 表
drop table if exists  ACT_ID_GROUP;
create table ACT_ID_GROUP (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    TYPE_ varchar(255),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists  ACT_ID_MEMBERSHIP;
create table ACT_ID_MEMBERSHIP (
    USER_ID_ varchar(64),
    GROUP_ID_ varchar(64),
    primary key (USER_ID_, GROUP_ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


drop table if exists   ACT_ID_USER;
create table ACT_ID_USER (
    ID_ varchar(64),
    REV_ integer,
    FIRST_ varchar(255),
    LAST_ varchar(255),
    EMAIL_ varchar(255),
    PWD_ varchar(255),
    PICTURE_ID_ varchar(64),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists   ACT_ID_INFO;
create table ACT_ID_INFO (
    ID_ varchar(64),
    REV_ integer,
    USER_ID_ varchar(64),
    TYPE_ varchar(64),
    KEY_ varchar(255),
    VALUE_ varchar(255),
    PASSWORD_ LONGBLOB,
    PARENT_ID_ varchar(255),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


alter table ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_GROUP
    foreign key (GROUP_ID_)
    references ACT_ID_GROUP (ID_);

alter table ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_USER
    foreign key (USER_ID_)
    references ACT_ID_USER (ID_);

drop table if exists   ACT_HI_PROCINST;
create table ACT_HI_PROCINST (
    ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    BUSINESS_KEY_ varchar(255),
    PROC_DEF_ID_ varchar(64) not null,
    START_TIME_ datetime(3) not null,
    END_TIME_ datetime(3),
    DURATION_ bigint,
    START_USER_ID_ varchar(255),
    START_ACT_ID_ varchar(255),
    END_ACT_ID_ varchar(255),
    SUPER_PROCESS_INSTANCE_ID_ varchar(64),
    DELETE_REASON_ varchar(4000),
    TENANT_ID_ varchar(255) default '',
    NAME_ varchar(255),
    primary key (ID_),
    unique (PROC_INST_ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists  ACT_HI_ACTINST;
create table ACT_HI_ACTINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    EXECUTION_ID_ varchar(64) not null,
    ACT_ID_ varchar(255) not null,
    TASK_ID_ varchar(64),
    CALL_PROC_INST_ID_ varchar(64),
    ACT_NAME_ varchar(255),
    ACT_TYPE_ varchar(255) not null,
    ASSIGNEE_ varchar(255),
    START_TIME_ datetime(3) not null,
    END_TIME_ datetime(3),
    DURATION_ bigint,
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists ACT_HI_TASKINST;
create table ACT_HI_TASKINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64),
    TASK_DEF_KEY_ varchar(255),
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    NAME_ varchar(255),
    PARENT_TASK_ID_ varchar(64),
    DESCRIPTION_ varchar(4000),
    OWNER_ varchar(255),
    ASSIGNEE_ varchar(255),
    START_TIME_ datetime(3) not null,
    CLAIM_TIME_ datetime(3),
    END_TIME_ datetime(3),
    DURATION_ bigint,
    DELETE_REASON_ varchar(4000),
    PRIORITY_ integer,
    DUE_DATE_ datetime(3),
    FORM_KEY_ varchar(255),
    CATEGORY_ varchar(255),
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists ACT_HI_VARINST;
create table ACT_HI_VARINST (
    ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    TASK_ID_ varchar(64),
    NAME_ varchar(255) not null,
    VAR_TYPE_ varchar(100),
    REV_ integer,
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    CREATE_TIME_ datetime(3),
    LAST_UPDATED_TIME_ datetime(3),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


drop table if exists ACT_HI_DETAIL;
create table ACT_HI_DETAIL (
    ID_ varchar(64) not null,
    TYPE_ varchar(255) not null,
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    TASK_ID_ varchar(64),
    ACT_INST_ID_ varchar(64),
    NAME_ varchar(255) not null,
    VAR_TYPE_ varchar(255),
    REV_ integer,
    TIME_ datetime(3) not null,
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists ACT_HI_COMMENT ;
create table ACT_HI_COMMENT (
    ID_ varchar(64) not null,
    TYPE_ varchar(255),
    TIME_ datetime(3) not null,
    USER_ID_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    ACTION_ varchar(255),
    MESSAGE_ varchar(4000),
    FULL_MSG_ LONGBLOB,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists  ACT_HI_ATTACHMENT ;
create table ACT_HI_ATTACHMENT (
    ID_ varchar(64) not null,
    REV_ integer,
    USER_ID_ varchar(255),
    NAME_ varchar(255),
    DESCRIPTION_ varchar(4000),
    TYPE_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    URL_ varchar(4000),
    CONTENT_ID_ varchar(64),
    TIME_ datetime(3),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists  ACT_HI_IDENTITYLINK  ;
create table ACT_HI_IDENTITYLINK (
    ID_ varchar(64),
    GROUP_ID_ varchar(255),
    TYPE_ varchar(255),
    USER_ID_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


create index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST(END_TIME_);
create index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST(BUSINESS_KEY_);
create index ACT_IDX_HI_ACT_INST_START on ACT_HI_ACTINST(START_TIME_);
create index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST(END_TIME_);
create index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL(PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL(ACT_INST_ID_);
create index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL(TIME_);
create index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL(NAME_);
create index ACT_IDX_HI_DETAIL_TASK_ID on ACT_HI_DETAIL(TASK_ID_);
create index ACT_IDX_HI_PROCVAR_PROC_INST on ACT_HI_VARINST(PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_NAME_TYPE on ACT_HI_VARINST(NAME_, VAR_TYPE_);
create index ACT_IDX_HI_PROCVAR_TASK_ID on ACT_HI_VARINST(TASK_ID_);
create index ACT_IDX_HI_ACT_INST_PROCINST on ACT_HI_ACTINST(PROC_INST_ID_, ACT_ID_);
create index ACT_IDX_HI_ACT_INST_EXEC on ACT_HI_ACTINST(EXECUTION_ID_, ACT_ID_);
create index ACT_IDX_HI_IDENT_LNK_USER on ACT_HI_IDENTITYLINK(USER_ID_);
create index ACT_IDX_HI_IDENT_LNK_TASK on ACT_HI_IDENTITYLINK(TASK_ID_);
create index ACT_IDX_HI_IDENT_LNK_PROCINST on ACT_HI_IDENTITYLINK(PROC_INST_ID_);
create index ACT_IDX_HI_TASK_INST_PROCINST on ACT_HI_TASKINST(PROC_INST_ID_);

drop table if exists  ACT_GE_PROPERTY ;
create table ACT_GE_PROPERTY (
    NAME_ varchar(64),
    VALUE_ varchar(300),
    REV_ integer,
    primary key (NAME_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

insert into ACT_GE_PROPERTY
values ('schema.version', '5.22.0.0', 1);

insert into ACT_GE_PROPERTY
values ('schema.history', 'create(5.22.0.0)', 1);

insert into ACT_GE_PROPERTY
values ('next.dbid', '1', 1);

drop table if exists  ACT_GE_BYTEARRAY;
create table ACT_GE_BYTEARRAY (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    DEPLOYMENT_ID_ varchar(64),
    BYTES_ LONGBLOB,
    GENERATED_ TINYINT,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


drop table if exists  ACT_RE_DEPLOYMENT;
create table ACT_RE_DEPLOYMENT (
    ID_ varchar(64),
    NAME_ varchar(255),
    CATEGORY_ varchar(255),
    TENANT_ID_ varchar(255) default '',
    DEPLOY_TIME_ timestamp(3) NULL,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists  ACT_RE_MODEL;
create table ACT_RE_MODEL (
    ID_ varchar(64) not null,
    REV_ integer,
    NAME_ varchar(255),
    KEY_ varchar(255),
    CATEGORY_ varchar(255),
    CREATE_TIME_ timestamp(3) null,
    LAST_UPDATE_TIME_ timestamp(3) null,
    VERSION_ integer,
    META_INFO_ varchar(4000),
    DEPLOYMENT_ID_ varchar(64),
    EDITOR_SOURCE_VALUE_ID_ varchar(64),
    EDITOR_SOURCE_EXTRA_VALUE_ID_ varchar(64),
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists ACT_RU_EXECUTION;
create table ACT_RU_EXECUTION (
    ID_ varchar(64),
    REV_ integer,
    PROC_INST_ID_ varchar(64),
    BUSINESS_KEY_ varchar(255),
    PARENT_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    SUPER_EXEC_ varchar(64),
    ACT_ID_ varchar(255),
    IS_ACTIVE_ TINYINT,
    IS_CONCURRENT_ TINYINT,
    IS_SCOPE_ TINYINT,
    IS_EVENT_SCOPE_ TINYINT,
    SUSPENSION_STATE_ integer,
    CACHED_ENT_STATE_ integer,
    TENANT_ID_ varchar(255) default '',
    NAME_ varchar(255),
    LOCK_TIME_ timestamp(3) NULL,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


drop table if exists ACT_RU_JOB;
create table ACT_RU_JOB (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    TYPE_ varchar(255) NOT NULL,
    LOCK_EXP_TIME_ timestamp(3) NULL,
    LOCK_OWNER_ varchar(255),
    EXCLUSIVE_ boolean,
    EXECUTION_ID_ varchar(64),
    PROCESS_INSTANCE_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    RETRIES_ integer,
    EXCEPTION_STACK_ID_ varchar(64),
    EXCEPTION_MSG_ varchar(4000),
    DUEDATE_ timestamp(3) NULL,
    REPEAT_ varchar(255),
    HANDLER_TYPE_ varchar(255),
    HANDLER_CFG_ varchar(4000),
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


drop table if exists ACT_RE_PROCDEF;
create table ACT_RE_PROCDEF (
    ID_ varchar(64) not null,
    REV_ integer,
    CATEGORY_ varchar(255),
    NAME_ varchar(255),
    KEY_ varchar(255) not null,
    VERSION_ integer not null,
    DEPLOYMENT_ID_ varchar(64),
    RESOURCE_NAME_ varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    DESCRIPTION_ varchar(4000),
    HAS_START_FORM_KEY_ TINYINT,
    HAS_GRAPHICAL_NOTATION_ TINYINT,
    SUSPENSION_STATE_ integer,
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists ACT_RU_TASK;
create table ACT_RU_TASK (
    ID_ varchar(64),
    REV_ integer,
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    NAME_ varchar(255),
    PARENT_TASK_ID_ varchar(64),
    DESCRIPTION_ varchar(4000),
    TASK_DEF_KEY_ varchar(255),
    OWNER_ varchar(255),
    ASSIGNEE_ varchar(255),
    DELEGATION_ varchar(64),
    PRIORITY_ integer,
    CREATE_TIME_ timestamp(3) NULL,
    DUE_DATE_ datetime(3),
    CATEGORY_ varchar(255),
    SUSPENSION_STATE_ integer,
    TENANT_ID_ varchar(255) default '',
    FORM_KEY_ varchar(255),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists ACT_RU_IDENTITYLINK;
create table ACT_RU_IDENTITYLINK (
    ID_ varchar(64),
    REV_ integer,
    GROUP_ID_ varchar(255),
    TYPE_ varchar(255),
    USER_ID_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists  ACT_RU_VARIABLE;
create table ACT_RU_VARIABLE (
    ID_ varchar(64) not null,
    REV_ integer,
    TYPE_ varchar(255) not null,
    NAME_ varchar(255) not null,
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    TASK_ID_ varchar(64),
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


drop table if exists  ACT_RU_EVENT_SUBSCR;
create table ACT_RU_EVENT_SUBSCR (
    ID_ varchar(64) not null,
    REV_ integer,
    EVENT_TYPE_ varchar(255) not null,
    EVENT_NAME_ varchar(255),
    EXECUTION_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    ACTIVITY_ID_ varchar(64),
    CONFIGURATION_ varchar(255),
    CREATED_ timestamp(3) not null DEFAULT CURRENT_TIMESTAMP(3),
    PROC_DEF_ID_ varchar(64),
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


drop table if exists   ACT_EVT_LOG;
create table ACT_EVT_LOG (
    LOG_NR_ bigint auto_increment,
    TYPE_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    TASK_ID_ varchar(64),
    TIME_STAMP_ timestamp(3) not null,
    USER_ID_ varchar(255),
    DATA_ LONGBLOB,
    LOCK_OWNER_ varchar(255),
    LOCK_TIME_ timestamp(3) null,
    IS_PROCESSED_ tinyint default 0,
    primary key (LOG_NR_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

drop table if exists  ACT_PROCDEF_INFO;
create table ACT_PROCDEF_INFO (
	ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64) not null,
    REV_ integer,
    INFO_JSON_ID_ varchar(64),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION(BUSINESS_KEY_);
create index ACT_IDX_TASK_CREATE on ACT_RU_TASK(CREATE_TIME_);
create index ACT_IDX_IDENT_LNK_USER on ACT_RU_IDENTITYLINK(USER_ID_);
create index ACT_IDX_IDENT_LNK_GROUP on ACT_RU_IDENTITYLINK(GROUP_ID_);
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on ACT_RU_EVENT_SUBSCR(CONFIGURATION_);
create index ACT_IDX_VARIABLE_TASK_ID on ACT_RU_VARIABLE(TASK_ID_);
create index ACT_IDX_ATHRZ_PROCEDEF on ACT_RU_IDENTITYLINK(PROC_DEF_ID_);
create index ACT_IDX_INFO_PROCDEF on ACT_PROCDEF_INFO(PROC_DEF_ID_);

alter table ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
    foreign key (DEPLOYMENT_ID_)
    references ACT_RE_DEPLOYMENT (ID_);

alter table ACT_RE_PROCDEF
    add constraint ACT_UNIQ_PROCDEF
    unique (KEY_,VERSION_, TENANT_ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION (ID_) on delete cascade on update cascade;

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PARENT
    foreign key (PARENT_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_SUPER
    foreign key (SUPER_EXEC_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF
    foreign key (PROC_DEF_ID_)
    references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_TSKASS_TASK
    foreign key (TASK_ID_)
    references ACT_RU_TASK (ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_ATHRZ_PROCEDEF
    foreign key (PROC_DEF_ID_)
    references ACT_RE_PROCDEF(ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_IDL_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_EXE
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
  	add constraint ACT_FK_TASK_PROCDEF
  	foreign key (PROC_DEF_ID_)
  	references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_EXE
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION(ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_BYTEARRAY
    foreign key (BYTEARRAY_ID_)
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXCEPTION
    foreign key (EXCEPTION_STACK_ID_)
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_EVENT_SUBSCR
    add constraint ACT_FK_EVENT_EXEC
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION(ID_);

alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE
    foreign key (EDITOR_SOURCE_VALUE_ID_)
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE_EXTRA
    foreign key (EDITOR_SOURCE_EXTRA_VALUE_ID_)
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_DEPLOYMENT
    foreign key (DEPLOYMENT_ID_)
    references ACT_RE_DEPLOYMENT (ID_);

alter table ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_JSON_BA
    foreign key (INFO_JSON_ID_)
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_PROCDEF
    foreign key (PROC_DEF_ID_)
    references ACT_RE_PROCDEF (ID_);

alter table ACT_PROCDEF_INFO
    add constraint ACT_UNIQ_INFO_PROCDEF
    unique (PROC_DEF_ID_);


-- activiti 测试数据

-- ACT_ID_GROUP

insert into ACT_ID_GROUP(ID_,NAME_) values ("admin","超级管理员");
insert into ACT_ID_GROUP(ID_,NAME_) values ("generalManager","总经理");
insert into ACT_ID_GROUP(ID_,NAME_) values ("hrManager","人事行政部门经理");
insert into ACT_ID_GROUP(ID_,NAME_) values ("financeManager","财务部门经理");
insert into ACT_ID_GROUP(ID_,NAME_) values ("lifeManager","后勤部门经理");
insert into ACT_ID_GROUP(ID_,NAME_) values ("hrAssistant","人事行政部门助理");
insert into ACT_ID_GROUP(ID_,NAME_) values ("financeAssistant","财务部门助理");
insert into ACT_ID_GROUP(ID_,NAME_) values ("lifeAssistant","后勤部门助理");
insert into ACT_ID_GROUP(ID_,NAME_) values ("worker","员工");
insert into ACT_ID_GROUP(ID_,NAME_) values ("doorkeeper","保安");

-- ACT_ID_USER

insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("zhangsan","000000","zhangsan@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("lisi","000000","lisi@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("wangwu","000000","wangwu@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("zhangfei","000000","zhangfei@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("houyi","000000","houyi@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("xiaoqiao","000000","xiaoqiao@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("caocao","000000","caocao@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("libai","000000","libai@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("guanyu","000000","guanyu@localhost");
insert into ACT_ID_USER(ID_,PWD_,EMAIL_) values ("daming","000000","daming@localhost");


-- ACT_ID_MEMBERSHIP

insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("zhangsan","admin");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("lisi","generalManager");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("wangwu","hrManager");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("zhangfei","financeManager");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("houyi","lifeManager");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("xiaoqiao","hrAssistant");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("caocao","financeAssistant");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("libai","lifeAssistant");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("guanyu","worker");
insert into ACT_ID_MEMBERSHIP(USER_ID_,GROUP_ID_) values ("daming","doorkeeper");




