-- Create table
create table TI_SAP_ZTHR_PT_DETAIL_TMP
(
  MANDT       VARCHAR2(9) default '000' not null,
  PERNR       VARCHAR2(24) default '00000000' not null,
  BEGDA       VARCHAR2(30) default '' not null,
  NACHN       VARCHAR2(120) default '' not null,
  ORGTX       VARCHAR2(120) default '' not null,
  ORGEH_T     VARCHAR2(120) default '' not null,
  PLANS_T     VARCHAR2(120) default '' not null,
  ZHRGWSX_T   VARCHAR2(180) default '' not null,
  PTEXT_G     VARCHAR2(60) default '' not null,
  PTEXT_K     VARCHAR2(60) default '' not null,
  ZTEXT       VARCHAR2(150) default '' not null,
  BEGUZ       VARCHAR2(24) default '' not null,
  ENDUZ       VARCHAR2(24) default '' not null,
  P10         VARCHAR2(24) default '' not null,
  P20         VARCHAR2(24) default '' not null,
  ZZ_CD       NUMBER(7,2) default '' not null,
  ZZ_ZT       NUMBER(7,2) default 0 not null,
  ARBST       VARCHAR2(12) default 0 not null,
  ZZ_CQSJ     NUMBER(7,2) default 0 not null,
  ZZ_QJQK     VARCHAR2(120) default '' not null,
  ZZ_OFF      VARCHAR2(6) default '' not null,
  TXT         VARCHAR2(60) default '' not null,
  STDAZ       NUMBER(7,2) default 0 not null,
  ZZ_KG       NUMBER(7,2) default 0 not null,
  ZZ_SJCQ     NUMBER(7,2) default 0 not null,
  DW          VARCHAR2(30) default '' not null,
  PAPER       VARCHAR2(6),
  CREATED_ON  VARCHAR2(24) default '',
  CREATED_TS  VARCHAR2(14),
  SYNC_TM     DATE default SYSDATE,
  SYNC_STATUS NUMBER default 0
)
tablespace SPMS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table TI_SAP_ZTHR_PT_DETAIL_TMP
  is '考勤明细零时表【从SAP同步过来】';
-- Add comments to the columns 
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.MANDT
  is '用户端';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PERNR
  is '人员编号';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.BEGDA
  is '开始日期';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.NACHN
  is '员工姓名';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ORGTX
  is '所属地区';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ORGEH_T
  is '职位名称';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PLANS_T
  is '组织名称';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZHRGWSX_T
  is '职位属性';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PTEXT_G
  is '员工组';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PTEXT_K
  is '员工子组';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZTEXT
  is '考勤方式';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.BEGUZ
  is '上班时间';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ENDUZ
  is '下班时间';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.P10
  is '签到时间';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.P20
  is '签退时间';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_CD
  is '迟到';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_ZT
  is '早退';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ARBST
  is '时段时间';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_CQSJ
  is '出勤时长';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_QJQK
  is '请假情况';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_OFF
  is '休息日';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.TXT
  is '加班类别';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.STDAZ
  is '加班时数';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_KG
  is '旷工';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_SJCQ
  is '实际出勤';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.DW
  is '加班单位';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PAPER
  is '考勤年月';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.CREATED_ON
  is 'SAP数据创建时间';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.CREATED_TS
  is 'SAP数据修改时间戳[日期+时间戳]';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.SYNC_TM
  is '数据同步时间';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.SYNC_STATUS
  is '同步状态';
