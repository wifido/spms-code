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
  is '������ϸ��ʱ����SAPͬ��������';
-- Add comments to the columns 
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.MANDT
  is '�û���';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PERNR
  is '��Ա���';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.BEGDA
  is '��ʼ����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.NACHN
  is 'Ա������';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ORGTX
  is '��������';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ORGEH_T
  is 'ְλ����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PLANS_T
  is '��֯����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZHRGWSX_T
  is 'ְλ����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PTEXT_G
  is 'Ա����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PTEXT_K
  is 'Ա������';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZTEXT
  is '���ڷ�ʽ';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.BEGUZ
  is '�ϰ�ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ENDUZ
  is '�°�ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.P10
  is 'ǩ��ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.P20
  is 'ǩ��ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_CD
  is '�ٵ�';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_ZT
  is '����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ARBST
  is 'ʱ��ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_CQSJ
  is '����ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_QJQK
  is '������';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_OFF
  is '��Ϣ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.TXT
  is '�Ӱ����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.STDAZ
  is '�Ӱ�ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_KG
  is '����';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.ZZ_SJCQ
  is 'ʵ�ʳ���';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.DW
  is '�Ӱ൥λ';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.PAPER
  is '��������';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.CREATED_ON
  is 'SAP���ݴ���ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.CREATED_TS
  is 'SAP�����޸�ʱ���[����+ʱ���]';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.SYNC_TM
  is '����ͬ��ʱ��';
comment on column TI_SAP_ZTHR_PT_DETAIL_TMP.SYNC_STATUS
  is 'ͬ��״̬';
