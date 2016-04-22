-- Create table
create table TI_PMP_EPIEMP_SYNC_his
(
  EPIEMP_ID       NUMBER(18) not null primary key,
  SUPPLIER_ID     VARCHAR2(32),
  SUPPLIER_NAME   VARCHAR2(128),
  CONTRACT_ID     NUMBER(18),
  EMP_CONTRACT_ID NUMBER(18),
  EPIEMP_CODE     VARCHAR2(32),
  EPIEMP_NAME     VARCHAR2(128),
  SEX             NUMBER(1),
  BIRTHDAY        DATE,
  HIGH_DEGREE     VARCHAR2(32),
  GRAD_TIME       DATE,
  HEIGHT          NUMBER(3),
  WEIGHT          NUMBER(3),
  EPIEMP_TYPE     VARCHAR2(32),
  CARD_NO         VARCHAR2(32),
  ACCOUNT_ADRESS  VARCHAR2(256),
  ACCOUNT_TYPE    VARCHAR2(32),
  PHONE           VARCHAR2(30),
  CONTACTS        VARCHAR2(128),
  CONTACTS_PHONE  VARCHAR2(32),
  DRIVING_TYPE    VARCHAR2(32),
  DRIVING_AGE     NUMBER(3,1),
  RESERVE_POST    VARCHAR2(128),
  SKILL           VARCHAR2(256),
  POS_LEVEL       VARCHAR2(32),
  IS_EMPLOYER     NUMBER(1),
  BUS_MODE        VARCHAR2(32),
  IMPORT_TIME     DATE,
  IS_HAVEEQUIP    NUMBER(1),
  TRAFFIC_TOOL    VARCHAR2(32),
  GC_AREA_CODE    VARCHAR2(32),
  GC_AREA         VARCHAR2(600),
  STATUS          NUMBER(2),
  GC_ORG          VARCHAR2(300),
  GC_POSITION_NO  VARCHAR2(32),
  GC_POSITION     VARCHAR2(600),
  GC_POSATTR      VARCHAR2(120),
  SUPERIOR_NO     VARCHAR2(32),
  SUPERIOR_NAME   VARCHAR2(128),
  GC_ORG_CODE     VARCHAR2(32),
  OFFICE_PHONE    VARCHAR2(32),
  EMAIL           VARCHAR2(200),
  EFFECT_DATE     DATE,
  IS_SFTOCOMP     NUMBER(1),
  REGISTER_DATE   DATE,
  OUT_DATE        DATE,
  REMARK          VARCHAR2(1024),
  CREATOR         VARCHAR2(128),
  CREATE_TIME     DATE,
  UPDATOR         VARCHAR2(128),
  UPDATE_TIME     DATE,
  FREEZE_FLAG     VARCHAR2(10),
  NET_CODE        VARCHAR2(32),
  LEAVE_DATE      DATE,
  FREEZE_DATE     DATE,
  LASTUPDATE      DATE default sysdate,
  DEAL_FLAG       NUMBER(1) default 0
);

-- Add comments to the table 
comment on table TI_PMP_EPIEMP_SYNC
  is 'PMP同步人员信息接口表';
-- Add comments to the columns 
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_ID
  is '雇员ID';
comment on column TI_PMP_EPIEMP_SYNC.SUPPLIER_ID
  is '供应商ID';
comment on column TI_PMP_EPIEMP_SYNC.SUPPLIER_NAME
  is '供应商名称';
comment on column TI_PMP_EPIEMP_SYNC.CONTRACT_ID
  is '合同ID';
comment on column TI_PMP_EPIEMP_SYNC.EMP_CONTRACT_ID
  is '雇员合同ID';
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_CODE
  is '雇员编号/工号[注：接收数据后，在往员工正式表更新数据时，请以工号作为唯一键值；雇员ID不能作为更新唯一键值]';
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_NAME
  is '雇员姓名';
comment on column TI_PMP_EPIEMP_SYNC.SEX
  is '性别';
comment on column TI_PMP_EPIEMP_SYNC.BIRTHDAY
  is '出生日期';
comment on column TI_PMP_EPIEMP_SYNC.HIGH_DEGREE
  is '最高学历[从字典表取这个字段的值：ti_pmp_sys_gl_type]';
comment on column TI_PMP_EPIEMP_SYNC.GRAD_TIME
  is '毕业时间';
comment on column TI_PMP_EPIEMP_SYNC.HEIGHT
  is '身高';
comment on column TI_PMP_EPIEMP_SYNC.WEIGHT
  is '体重';
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_TYPE
  is '人员类别[类似于字段：HIGH_DEGREE（从字典表中取值）]';
comment on column TI_PMP_EPIEMP_SYNC.CARD_NO
  is '证件号码[放身份证号]';
comment on column TI_PMP_EPIEMP_SYNC.ACCOUNT_ADRESS
  is '户口所在地';
comment on column TI_PMP_EPIEMP_SYNC.ACCOUNT_TYPE
  is '户口类别[类似于字段：HIGH_DEGREE（从字典表中取值）]';
comment on column TI_PMP_EPIEMP_SYNC.PHONE
  is '电话号码[放手机号]';
comment on column TI_PMP_EPIEMP_SYNC.CONTACTS
  is '紧急联系人';
comment on column TI_PMP_EPIEMP_SYNC.CONTACTS_PHONE
  is '紧急联系人电话';
comment on column TI_PMP_EPIEMP_SYNC.DRIVING_TYPE
  is '驾照类型[类似于字段：HIGH_DEGREE（从字典表中取值）]';
comment on column TI_PMP_EPIEMP_SYNC.DRIVING_AGE
  is '驾龄';
comment on column TI_PMP_EPIEMP_SYNC.RESERVE_POST
  is '后备职位';
comment on column TI_PMP_EPIEMP_SYNC.SKILL
  is '技能';
comment on column TI_PMP_EPIEMP_SYNC.POS_LEVEL
  is '级别[类似于字段：HIGH_DEGREE（从字典表中取值）]';
comment on column TI_PMP_EPIEMP_SYNC.IS_EMPLOYER
  is '是否雇主[0：否；1：是]';
comment on column TI_PMP_EPIEMP_SYNC.BUS_MODE
  is '业务模式';
comment on column TI_PMP_EPIEMP_SYNC.IMPORT_TIME
  is '引入时间[格式：YYYY-MM-DD；成为合作伙伴及其雇员的时间]';
comment on column TI_PMP_EPIEMP_SYNC.IS_HAVEEQUIP
  is '是否有装备[0：否；1：是]';
comment on column TI_PMP_EPIEMP_SYNC.TRAFFIC_TOOL
  is '交通工具[类似于字段：HIGH_DEGREE（从字典表中取值）]';
comment on column TI_PMP_EPIEMP_SYNC.GC_AREA_CODE
  is '所属地区编号[注意：这是地区对应的组织编号；不是组织的网络代码]';
comment on column TI_PMP_EPIEMP_SYNC.GC_AREA
  is '所属地区';
comment on column TI_PMP_EPIEMP_SYNC.STATUS
  is '雇员状态[0：非在职；1：在职]';
comment on column TI_PMP_EPIEMP_SYNC.GC_ORG
  is '所属组织名称';
comment on column TI_PMP_EPIEMP_SYNC.GC_POSITION_NO
  is '职位编号[即岗位编号]';
comment on column TI_PMP_EPIEMP_SYNC.GC_POSITION
  is '职位名称';
comment on column TI_PMP_EPIEMP_SYNC.GC_POSATTR
  is '职位属性';
comment on column TI_PMP_EPIEMP_SYNC.SUPERIOR_NO
  is '上级工号';
comment on column TI_PMP_EPIEMP_SYNC.SUPERIOR_NAME
  is '上级姓名';
comment on column TI_PMP_EPIEMP_SYNC.GC_ORG_CODE
  is '所属组织编号[注意：这是雇员所属组织对应的组织编号；不是组织的网络代码]';
comment on column TI_PMP_EPIEMP_SYNC.OFFICE_PHONE
  is '办公电话';
comment on column TI_PMP_EPIEMP_SYNC.EMAIL
  is '电子邮件';
comment on column TI_PMP_EPIEMP_SYNC.EFFECT_DATE
  is '生效时间[格式：YYYY-MM-DD HH24:MI:SS]';
comment on column TI_PMP_EPIEMP_SYNC.IS_SFTOCOMP
  is '是否顺丰员工转为合作伙伴[0：否；1：是]';
comment on column TI_PMP_EPIEMP_SYNC.REGISTER_DATE
  is '加入顺丰时间[格式：YYYY-MM-DD；指的是在顺丰的入职时间；，如为顺丰转为合作伙伴及其雇员的，为必填，否则为非必填]';
comment on column TI_PMP_EPIEMP_SYNC.OUT_DATE
  is '退出顺丰时间[格式：YYYY-MM-DD；如为顺丰转为合作伙伴及其雇员的，为必填，否则为非必填]';
comment on column TI_PMP_EPIEMP_SYNC.REMARK
  is '备注';
comment on column TI_PMP_EPIEMP_SYNC.CREATOR
  is '创建人';
comment on column TI_PMP_EPIEMP_SYNC.CREATE_TIME
  is '创建时间[格式：YYYY-MM-DD HH24:MI:SS]';
comment on column TI_PMP_EPIEMP_SYNC.UPDATOR
  is '最后修改人';
comment on column TI_PMP_EPIEMP_SYNC.UPDATE_TIME
  is '修改时间[格式：YYYY-MM-DD HH24:MI:SS]';
comment on column TI_PMP_EPIEMP_SYNC.FREEZE_FLAG
  is '冻结标识[1：冻结]';
comment on column TI_PMP_EPIEMP_SYNC.NET_CODE
  is '网络代码[注意：这是雇员所属组织对应的组织网络代码]';
comment on column TI_PMP_EPIEMP_SYNC.LEAVE_DATE
  is '离职时间[格式：YYYY-MM-DD HH24:MI:SS;离职时间，即雇员不再在顺丰上班的时间]';
comment on column TI_PMP_EPIEMP_SYNC.FREEZE_DATE
  is '冻结时间';
comment on column TI_PMP_EPIEMP_SYNC.LASTUPDATE
  is '最后更新时间';
comment on column TI_PMP_EPIEMP_SYNC.DEAL_FLAG
  is '1:已处理0:未处理2：同步错误';

