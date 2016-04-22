CREATE OR REPLACE PROCEDURE PMP_SYNCHRONOUS_TO_SPMS AS
  /**
   *每天02:00、14:00将SAP人员数据至TM_OSS_EMPLOYEE接口表
  **/
  --1.定义执行序号
  L_CALL_NO NUMBER;
  KEYVALUE  VARCHAR(900);

BEGIN

  --2.设置执行序号
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.开始记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'PMP_SYNCHRONOUS_TO_SPMS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  --查询接口是否打开
  SELECT C.KEY_VALUE
    INTO KEYVALUE
    FROM TL_SPMS_SYS_CONFIG C
   WHERE C.KEY_NAME = 'CD_SAP2SPMS_EMP';
  IF KEYVALUE = '1' THEN

    BEGIN

      --插入历史表
      INSERT INTO ti_pmp_epiemp_sync_his
        (EPIEMP_ID,
         SUPPLIER_ID,
         SUPPLIER_NAME,
         CONTRACT_ID,
         EMP_CONTRACT_ID,
         EPIEMP_CODE,
         EPIEMP_NAME,
         SEX,
         BIRTHDAY,
         HIGH_DEGREE,
         GRAD_TIME,
         HEIGHT,
         WEIGHT,
         EPIEMP_TYPE,
         CARD_NO,
         ACCOUNT_ADRESS,
         ACCOUNT_TYPE,
         PHONE,
         CONTACTS,
         CONTACTS_PHONE,
         DRIVING_TYPE,
         DRIVING_AGE,
         RESERVE_POST,
         SKILL,
         POS_LEVEL,
         IS_EMPLOYER,
         BUS_MODE,
         IMPORT_TIME,
         IS_HAVEEQUIP,
         TRAFFIC_TOOL,
         GC_AREA_CODE,
         GC_AREA,
         STATUS,
         GC_ORG,
         GC_POSITION_NO,
         GC_POSITION,
         GC_POSATTR,
         SUPERIOR_NO,
         SUPERIOR_NAME,
         GC_ORG_CODE,
         OFFICE_PHONE,
         EMAIL,
         EFFECT_DATE,
         IS_SFTOCOMP,
         REGISTER_DATE,
         OUT_DATE,
         REMARK,
         CREATOR,
         CREATE_TIME,
         UPDATOR,
         UPDATE_TIME,
         FREEZE_FLAG,
         NET_CODE,
         LEAVE_DATE,
         FREEZE_DATE,
         LASTUPDATE,
         DEAL_FLAG)
        SELECT EPIEMP_ID,
               SUPPLIER_ID,
               SUPPLIER_NAME,
               CONTRACT_ID,
               EMP_CONTRACT_ID,
               EPIEMP_CODE,
               EPIEMP_NAME,
               SEX,
               BIRTHDAY,
               HIGH_DEGREE,
               GRAD_TIME,
               HEIGHT,
               WEIGHT,
               EPIEMP_TYPE,
               CARD_NO,
               ACCOUNT_ADRESS,
               ACCOUNT_TYPE,
               PHONE,
               CONTACTS,
               CONTACTS_PHONE,
               DRIVING_TYPE,
               DRIVING_AGE,
               RESERVE_POST,
               SKILL,
               POS_LEVEL,
               IS_EMPLOYER,
               BUS_MODE,
               IMPORT_TIME,
               IS_HAVEEQUIP,
               TRAFFIC_TOOL,
               GC_AREA_CODE,
               GC_AREA,
               STATUS,
               GC_ORG,
               GC_POSITION_NO,
               GC_POSITION,
               GC_POSATTR,
               SUPERIOR_NO,
               SUPERIOR_NAME,
               GC_ORG_CODE,
               OFFICE_PHONE,
               EMAIL,
               EFFECT_DATE,
               IS_SFTOCOMP,
               REGISTER_DATE,
               OUT_DATE,
               REMARK,
               CREATOR,
               CREATE_TIME,
               UPDATOR,
               UPDATE_TIME,
               FREEZE_FLAG,
               NET_CODE,
               LEAVE_DATE,
               FREEZE_DATE,
               LASTUPDATE,
               DEAL_FLAG
          FROM ti_pmp_epiemp_sync S
         WHERE S.DEAL_FLAG = 0;
      COMMIT;

      --插入临时表
      insert into tm_pmp_employee_tmp
        (EMP_ID,
         EMP_CODE,
         EMP_NAME,
         EMP_DUTY_NAME,
         DEPT_ID,
         CREATE_TM,
         MODIFIED_TM,
         CREATE_EMP_CODE,
         MODIFIED_EMP_CODE,
         WORK_TYPE,
         EMAIL,
         DIMISSION_DT,
         SF_DATE,
         EMP_POST_TYPE,
         POSITION_ATTR,
         CANCEL_FLAG,
         SYNC_TM,
         DATA_SOURCE,
         PERSK_TXT)
        select EPIEMP_ID,
         EPIEMP_CODE,
         EPIEMP_NAME,
         gc_position,
         d.dept_id,
         create_time,
         update_time,
         'ADMIN',
         'ADMIN',
         6,
         email,
         leave_date,
         register_date,
         GETS_TYPE_OF_PMP(d.type_code, t.gc_posattr, t.gc_position),
         case when t.gc_posattr = '0' then
           '一线'
           when  t.gc_posattr = '1' then
             '二线'
             when t.gc_posattr = '2' then
               '三线'
               end,
         case
           when status = '0' then
            'Y'
           else
            ''
         end,
         sysdate,
         '3',
         '外包'
          from ti_pmp_epiemp_sync t, tm_department d
         where t.net_code = d.dept_code
         and t.deal_flag = '0';
  

      --更新已经存在的信息
      UPDATE tm_oss_employee E
         SET (EMP_CODE,
              EMP_NAME,
              EMP_DUTY_NAME,
              DEPT_ID,
              CREATE_TM,
              MODIFIED_TM,
              CREATE_EMP_CODE,
              MODIFIED_EMP_CODE,
              WORK_TYPE,
              EMAIL,
              DIMISSION_DT,
              SF_DATE,
              EMP_POST_TYPE,
              POSITION_ATTR,
              CANCEL_FLAG,
              SYNC_TM,
              DATA_SOURCE,
              PERSK_TXT) =
             (SELECT EMP_CODE,
                     EMP_NAME,
                     EMP_DUTY_NAME,
                     DEPT_ID,
                     CREATE_TM,
                     MODIFIED_TM,
                     CREATE_EMP_CODE,
                     MODIFIED_EMP_CODE,
                     WORK_TYPE,
                     EMAIL,
                     DIMISSION_DT,
                     SF_DATE,
                     EMP_POST_TYPE,
                     POSITION_ATTR,
                     CANCEL_FLAG,
                     SYNC_TM,
                     DATA_SOURCE,
                     PERSK_TXT
                FROM tm_pmp_employee_tmp TMP
               WHERE TMP.EMP_CODE = E.EMP_CODE)
       WHERE E.EMP_CODE IN (SELECT TMP.EMP_CODE
                              FROM tm_pmp_employee_tmp TMP
                             WHERE TMP.EMP_CODE = E.EMP_CODE);

      --插入不存在的值
      INSERT INTO tm_oss_employee E
        (E.EMP_ID,
         EMP_CODE,
         EMP_NAME,
         EMP_DUTY_NAME,
         DEPT_ID,
         CREATE_TM,
         MODIFIED_TM,
         CREATE_EMP_CODE,
         MODIFIED_EMP_CODE,
         WORK_TYPE,
         EMAIL,
         DIMISSION_DT,
         SF_DATE,
         EMP_POST_TYPE,
         POSITION_ATTR,
         CANCEL_FLAG,
         SYNC_TM,
         DATA_SOURCE,
         PERSK_TXT)
        SELECT SEQ_OSS_BASE.NEXTVAL,
               EMP_CODE,
               EMP_NAME,
               EMP_DUTY_NAME,
               DEPT_ID,
               CREATE_TM,
               MODIFIED_TM,
               CREATE_EMP_CODE,
               MODIFIED_EMP_CODE,
               WORK_TYPE,
               EMAIL,
               DIMISSION_DT,
               SF_DATE,
               EMP_POST_TYPE,
               POSITION_ATTR,
               CANCEL_FLAG,
               SYNC_TM,
               DATA_SOURCE,
               PERSK_TXT
          FROM tm_pmp_employee_tmp TMP
         WHERE NOT EXISTS (SELECT 1
                  FROM tm_oss_employee EE
                 WHERE EE.EMP_CODE = TMP.EMP_CODE);
      --更改接口表状态
      UPDATE ti_pmp_epiemp_sync S
         SET S.DEAL_FLAG = 1
       WHERE S.EPIEMP_ID IN (SELECT TMP.EMP_ID FROM tm_pmp_employee_tmp TMP);
      COMMIT;
      EXECUTE IMMEDIATE 'TRUNCATE TABLE tm_pmp_employee_tmp';
    END;

  END IF;
  --4.结束记录日志
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'PMP_SYNCHRONOUS_TO_SPMS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
  --5.异常记录日志
EXCEPTION
  WHEN OTHERS THEN
    --回滚数据
    ROLLBACK;
    
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'PMP_SYNCHRONOUS_TO_SPMS',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END PMP_SYNCHRONOUS_TO_SPMS;
/