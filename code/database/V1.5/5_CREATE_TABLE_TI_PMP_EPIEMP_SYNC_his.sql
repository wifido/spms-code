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
  is 'PMPͬ����Ա��Ϣ�ӿڱ�';
-- Add comments to the columns 
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_ID
  is '��ԱID';
comment on column TI_PMP_EPIEMP_SYNC.SUPPLIER_ID
  is '��Ӧ��ID';
comment on column TI_PMP_EPIEMP_SYNC.SUPPLIER_NAME
  is '��Ӧ������';
comment on column TI_PMP_EPIEMP_SYNC.CONTRACT_ID
  is '��ͬID';
comment on column TI_PMP_EPIEMP_SYNC.EMP_CONTRACT_ID
  is '��Ա��ͬID';
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_CODE
  is '��Ա���/����[ע���������ݺ�����Ա����ʽ���������ʱ�����Թ�����ΪΨһ��ֵ����ԱID������Ϊ����Ψһ��ֵ]';
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_NAME
  is '��Ա����';
comment on column TI_PMP_EPIEMP_SYNC.SEX
  is '�Ա�';
comment on column TI_PMP_EPIEMP_SYNC.BIRTHDAY
  is '��������';
comment on column TI_PMP_EPIEMP_SYNC.HIGH_DEGREE
  is '���ѧ��[���ֵ��ȡ����ֶε�ֵ��ti_pmp_sys_gl_type]';
comment on column TI_PMP_EPIEMP_SYNC.GRAD_TIME
  is '��ҵʱ��';
comment on column TI_PMP_EPIEMP_SYNC.HEIGHT
  is '���';
comment on column TI_PMP_EPIEMP_SYNC.WEIGHT
  is '����';
comment on column TI_PMP_EPIEMP_SYNC.EPIEMP_TYPE
  is '��Ա���[�������ֶΣ�HIGH_DEGREE�����ֵ����ȡֵ��]';
comment on column TI_PMP_EPIEMP_SYNC.CARD_NO
  is '֤������[�����֤��]';
comment on column TI_PMP_EPIEMP_SYNC.ACCOUNT_ADRESS
  is '�������ڵ�';
comment on column TI_PMP_EPIEMP_SYNC.ACCOUNT_TYPE
  is '�������[�������ֶΣ�HIGH_DEGREE�����ֵ����ȡֵ��]';
comment on column TI_PMP_EPIEMP_SYNC.PHONE
  is '�绰����[���ֻ���]';
comment on column TI_PMP_EPIEMP_SYNC.CONTACTS
  is '������ϵ��';
comment on column TI_PMP_EPIEMP_SYNC.CONTACTS_PHONE
  is '������ϵ�˵绰';
comment on column TI_PMP_EPIEMP_SYNC.DRIVING_TYPE
  is '��������[�������ֶΣ�HIGH_DEGREE�����ֵ����ȡֵ��]';
comment on column TI_PMP_EPIEMP_SYNC.DRIVING_AGE
  is '����';
comment on column TI_PMP_EPIEMP_SYNC.RESERVE_POST
  is '��ְλ';
comment on column TI_PMP_EPIEMP_SYNC.SKILL
  is '����';
comment on column TI_PMP_EPIEMP_SYNC.POS_LEVEL
  is '����[�������ֶΣ�HIGH_DEGREE�����ֵ����ȡֵ��]';
comment on column TI_PMP_EPIEMP_SYNC.IS_EMPLOYER
  is '�Ƿ����[0����1����]';
comment on column TI_PMP_EPIEMP_SYNC.BUS_MODE
  is 'ҵ��ģʽ';
comment on column TI_PMP_EPIEMP_SYNC.IMPORT_TIME
  is '����ʱ��[��ʽ��YYYY-MM-DD����Ϊ������鼰���Ա��ʱ��]';
comment on column TI_PMP_EPIEMP_SYNC.IS_HAVEEQUIP
  is '�Ƿ���װ��[0����1����]';
comment on column TI_PMP_EPIEMP_SYNC.TRAFFIC_TOOL
  is '��ͨ����[�������ֶΣ�HIGH_DEGREE�����ֵ����ȡֵ��]';
comment on column TI_PMP_EPIEMP_SYNC.GC_AREA_CODE
  is '�����������[ע�⣺���ǵ�����Ӧ����֯��ţ�������֯���������]';
comment on column TI_PMP_EPIEMP_SYNC.GC_AREA
  is '��������';
comment on column TI_PMP_EPIEMP_SYNC.STATUS
  is '��Ա״̬[0������ְ��1����ְ]';
comment on column TI_PMP_EPIEMP_SYNC.GC_ORG
  is '������֯����';
comment on column TI_PMP_EPIEMP_SYNC.GC_POSITION_NO
  is 'ְλ���[����λ���]';
comment on column TI_PMP_EPIEMP_SYNC.GC_POSITION
  is 'ְλ����';
comment on column TI_PMP_EPIEMP_SYNC.GC_POSATTR
  is 'ְλ����';
comment on column TI_PMP_EPIEMP_SYNC.SUPERIOR_NO
  is '�ϼ�����';
comment on column TI_PMP_EPIEMP_SYNC.SUPERIOR_NAME
  is '�ϼ�����';
comment on column TI_PMP_EPIEMP_SYNC.GC_ORG_CODE
  is '������֯���[ע�⣺���ǹ�Ա������֯��Ӧ����֯��ţ�������֯���������]';
comment on column TI_PMP_EPIEMP_SYNC.OFFICE_PHONE
  is '�칫�绰';
comment on column TI_PMP_EPIEMP_SYNC.EMAIL
  is '�����ʼ�';
comment on column TI_PMP_EPIEMP_SYNC.EFFECT_DATE
  is '��Чʱ��[��ʽ��YYYY-MM-DD HH24:MI:SS]';
comment on column TI_PMP_EPIEMP_SYNC.IS_SFTOCOMP
  is '�Ƿ�˳��Ա��תΪ�������[0����1����]';
comment on column TI_PMP_EPIEMP_SYNC.REGISTER_DATE
  is '����˳��ʱ��[��ʽ��YYYY-MM-DD��ָ������˳�����ְʱ�䣻����Ϊ˳��תΪ������鼰���Ա�ģ�Ϊ�������Ϊ�Ǳ���]';
comment on column TI_PMP_EPIEMP_SYNC.OUT_DATE
  is '�˳�˳��ʱ��[��ʽ��YYYY-MM-DD����Ϊ˳��תΪ������鼰���Ա�ģ�Ϊ�������Ϊ�Ǳ���]';
comment on column TI_PMP_EPIEMP_SYNC.REMARK
  is '��ע';
comment on column TI_PMP_EPIEMP_SYNC.CREATOR
  is '������';
comment on column TI_PMP_EPIEMP_SYNC.CREATE_TIME
  is '����ʱ��[��ʽ��YYYY-MM-DD HH24:MI:SS]';
comment on column TI_PMP_EPIEMP_SYNC.UPDATOR
  is '����޸���';
comment on column TI_PMP_EPIEMP_SYNC.UPDATE_TIME
  is '�޸�ʱ��[��ʽ��YYYY-MM-DD HH24:MI:SS]';
comment on column TI_PMP_EPIEMP_SYNC.FREEZE_FLAG
  is '�����ʶ[1������]';
comment on column TI_PMP_EPIEMP_SYNC.NET_CODE
  is '�������[ע�⣺���ǹ�Ա������֯��Ӧ����֯�������]';
comment on column TI_PMP_EPIEMP_SYNC.LEAVE_DATE
  is '��ְʱ��[��ʽ��YYYY-MM-DD HH24:MI:SS;��ְʱ�䣬����Ա������˳���ϰ��ʱ��]';
comment on column TI_PMP_EPIEMP_SYNC.FREEZE_DATE
  is '����ʱ��';
comment on column TI_PMP_EPIEMP_SYNC.LASTUPDATE
  is '������ʱ��';
comment on column TI_PMP_EPIEMP_SYNC.DEAL_FLAG
  is '1:�Ѵ���0:δ����2��ͬ������';

