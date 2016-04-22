CREATE OR REPLACE PROCEDURE OPERATION2SAP_SYNC AS
  /**
   *ÿ��22:00�������Ű�������SAP�ӿڱ�
  **/
  KEYVALUE VARCHAR2(900);
  --1.����ִ�����
  L_CALL_NO NUMBER;
BEGIN
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;

  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OPERATION2SAP_SYNC',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  BEGIN
    --��ѯ�ӿ��Ƿ��
    SELECT C.KEY_VALUE
      INTO KEYVALUE
      FROM TL_SPMS_SYS_CONFIG C
     WHERE C.KEY_NAME = 'CD_PRO2SAP_CLASS';

    --�жϽӿ��Ƿ��
    IF KEYVALUE = '1' THEN
      BEGIN
        ----���������Ű�������SAP�ӿڱ�--begin
        --a.����ӿ���������ʱ��
        INSERT INTO TT_SAP_SYNCHRONOUS_TMP1
          (ID,
           EMP_CODE,
           BEGIN_DATE,
           END_DATE,
           BEGIN_TM1,
           END_TM1,
           BEGIN_TM_FLAG1,
           BEGIN_TM2,
           END_TM2,
           BEGIN_TM_FLAG2,
           BEGIN_TM3,
           END_TM3,
           BEGIN_TM_FLAG3,
           TMR_DAY_FLAG,
           OFF_DUTY_FLAG,
           CLASS_SYSTEM,
           CREATE_TM,
           NODE_KEY,
           STATE_FLG,
           ERROR_INFO,
           EMP_POST_TYPE)
          --��'��'״̬����
          SELECT D.ID,
                 D.EMP_CODE,
                 TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') BEGIN_DATE,
                 TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') END_DATE,
                 DECODE(B.START1_TIME,
                        NULL,
                        NULL,
                        SUBSTR(REPLACE(LPAD(B.START1_TIME, 5, '0'), ':', '')||'00', 1, 6)) BEGIN_TM1,
                 DECODE(B.END1_TIME,
                        NULL,
                        NULL,
                        SUBSTR(REPLACE(LPAD(B.END1_TIME, 5, '0') , ':', '')||'00', 1, 6)) END_TM1,
                 DECODE(B.START1_TIME, NULL, 0, 1) BEGIN_TM_FLAG1,
                 DECODE(B.START2_TIME,
                        NULL,
                        NULL,
                        SUBSTR(REPLACE(LPAD(B.START2_TIME, 5, '0'), ':', '')||'00', 1, 6)) BEGIN_TM2,
                 DECODE(B.END2_TIME,
                        NULL,
                        NULL,
                        SUBSTR(REPLACE(LPAD(B.END2_TIME, 5, '0'), ':', '')||'00', 1, 6)) END_TM2,
                 DECODE(B.START2_TIME, NULL, 0, 1) BEGIN_TM_FLAG2,
                 DECODE(B.START3_TIME,
                        NULL,
                        NULL,
                        SUBSTR(REPLACE(LPAD(B.START3_TIME, 5, '0'), ':', '')||'00', 1, 6)) BEGIN_TM2,
                 DECODE(B.END3_TIME,
                        NULL,
                        NULL,
                        SUBSTR(REPLACE(LPAD(B.END3_TIME, 5, '0'), ':', '')||'00', 1, 6)) END_TM2,
                 DECODE(B.START3_TIME, NULL, 0, 1) BEGIN_TM_FLAG3,
                 '' AS TMR_DAY_FLAG,
                 '' OFF_DUTY_FLAG,
                 '2' CLASS_SYSTEM,
                 SYSDATE CREATE_TM,
                 '' NODE_KEY,
                 0 STATE_FLG,
                 '' ERROR_INFO,
                 '1'
            FROM TT_PB_SHEDULE_BY_DAY   D,
                 TT_PB_SHEDULE_BY_MONTH M,
                 TM_PB_SCHEDULE_BASE_INFO B,
                 TM_OSS_EMPLOYEE E
          --�����±�
           WHERE D.SHEDULE_MON_ID = M.ID
                 --������Ա��
             AND D.EMP_CODE = E.EMP_CODE
                 --����ת���Ű�����
             AND D.DEPT_ID = E.DEPT_ID

             AND (E.LAST_ZNO is null or TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
                  --������ְ��Ա�Ű�����
             AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD'))
                 --����ת��ǰ������
             AND (E.TRANSFER_DATE IS NULL OR TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
                 --������ְ����֮ǰ������
             AND TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >=  TO_CHAR(E.SF_DATE, 'YYYYMMDD')
                  --��������
             AND D.DEPT_ID = B.DEPT_ID
             AND D.SHEDULE_CODE = B.SCHEDULE_CODE
             AND D.SHEDULE_DT >= DATE'2015-01-01'
                 --ȡ��ʷǰ40�������
             AND D.SHEDULE_DT >= TRUNC(SYSDATE - 40)
                --ȡ����ǰ������
             AND D.SHEDULE_DT <= TRUNC(SYSDATE)
                --������Ա���ͣ�������������˾�Ӫ�а���
             AND E.WORK_TYPE NOT IN (6, 8, 9)
                --���˺���͸۰�̨
             AND D.DEPT_ID NOT IN
                 (SELECT DEPT_ID
                    FROM TM_DEPARTMENT
                   START WITH DEPT_CODE IN ('CN06', 'CN07')
                  CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
              and D.DEPT_ID NOT IN
                (SELECT DEPT_ID
              FROM TM_DEPARTMENT
            where dept_code in (with t1 as (select t.key_value c1
           from tl_spms_sys_config t
          where t.key_name = 'HONGKONG_DEPTCODE')
         select distinct regexp_substr(c1, '[^,]+', 1, level) c1
           from t1
         connect by level = length(c1) - length(replace(c1, ',', '')) + 1
        ))  
             AND D.SHEDULE_CODE IS NOT NULL
             AND D.SHEDULE_CODE <> '��'
             AND (D.SYNCHRO_STATUS != 1 OR D.SYNCHRO_STATUS IS NULL)
                 --ȡ�������
             AND B.YM = M.YM
             AND B.CLASS_TYPE = '1'
             AND E.EMP_POST_TYPE = '1'
             AND E.DATA_SOURCE = '2'
          UNION ALL
          --'��'״̬����
          SELECT D.ID,
                 D.EMP_CODE,
                 TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') BEGIN_DATE,
                 TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') END_DATE,
                 NULL BEGIN_TM1,
                 NULL END_TM1,
                 0 BEGIN_TM_FLAG1,
                 NULL BEGIN_TM2,
                 NULL END_TM2,
                 0 BEGIN_TM_FLAG2,
                 NULL BEGIN_TM2,
                 NULL END_TM2,
                 0 BEGIN_TM_FLAG3,
                 '' AS TMR_DAY_FLAG,
                 'OFF' OFF_DUTY_FLAG,
                 '2' CLASS_SYSTEM,
                 SYSDATE CREATE_TM,
                 '' NODE_KEY,
                 0 STATE_FLG,
                 '' ERROR_INFO,
                 '1'
            FROM TT_PB_SHEDULE_BY_DAY   D,
                 TT_PB_SHEDULE_BY_MONTH M,
                 --ȡȷ�ϵ��±���Ϣ

                 TM_OSS_EMPLOYEE E
          --�����±�
           WHERE D.SHEDULE_MON_ID = M.ID
                --������ȷ�ϱ�

                 --������Ա��
             AND D.EMP_CODE = E.EMP_CODE
                 --����ת���Ű�����
             AND D.DEPT_ID = E.DEPT_ID
              -- ������ְǰ���Ű�����
             AND (E.SF_DATE IS NULL OR TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >= TO_CHAR(E.SF_DATE, 'YYYYMMDD'))
             AND (E.LAST_ZNO is null or TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >= TO_CHAR(E.DATE_FROM, 'YYYYMMDD'))
                 --������ְ��Ա�Ű�����
             AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYYMMDD') > TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD'))
                   --����ת��ǰ������
             AND (E.TRANSFER_DATE IS NULL OR TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >= TO_CHAR(E.TRANSFER_DATE, 'YYYYMMDD'))
                   --������ְ����֮ǰ������
             AND TO_CHAR(D.SHEDULE_DT, 'YYYYMMDD') >= TO_CHAR(E.SF_DATE, 'YYYYMMDD')

             AND D.SHEDULE_DT >= DATE'2015-01-01'
               --ȡ��ʷǰ40�������
             AND  D.SHEDULE_DT >= TRUNC(SYSDATE - 40)
                --ȡ����ǰ������
             AND D.SHEDULE_DT <= TRUNC(SYSDATE)
                --������Ա���ͣ�������������˾�Ӫ�а���
             AND E.WORK_TYPE NOT IN (6, 8, 9)
                --���˺���͸۰�̨
             AND D.DEPT_ID NOT IN
                 (SELECT DEPT_ID
                    FROM TM_DEPARTMENT
                   START WITH DEPT_CODE IN ('CN06', 'CN07')
                  CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)
                    and D.DEPT_ID NOT IN
                (SELECT DEPT_ID
              FROM TM_DEPARTMENT
            where dept_code in (with t1 as (select t.key_value c1
           from tl_spms_sys_config t
          where t.key_name = 'HONGKONG_DEPTCODE')
         select distinct regexp_substr(c1, '[^,]+', 1, level) c1
           from t1
         connect by level = length(c1) - length(replace(c1, ',', '')) + 1
        ))  
             AND D.SHEDULE_CODE = '��'
             AND (D.SYNCHRO_STATUS != 1 OR D.SYNCHRO_STATUS IS NULL)
             AND E.EMP_POST_TYPE = '1'
             AND E.DATA_SOURCE = '2';
        COMMIT;

        --b.ɾ�����ڵ�����
        DELETE FROM TT_SAP_SYNCHRONOUS S
         WHERE EXISTS
         (SELECT 1
                  FROM (SELECT EMP_CODE, BEGIN_DATE
                          FROM TT_SAP_SYNCHRONOUS_TMP1
                         GROUP BY EMP_CODE, BEGIN_DATE) TM
                 WHERE TM.EMP_CODE = S.EMP_CODE
                   AND TM.BEGIN_DATE = S.BEGIN_DATE��
         AND S.tmr_day_flag is null;

        --c.��ʱ���1����ӿڱ�[ֻ��һ��ʱ���1]
        INSERT INTO TT_SAP_SYNCHRONOUS
          (ID,
           EMP_CODE,
           BEGIN_DATE,
           END_DATE,
           BEGIN_TM,
           END_TM,
           TMR_DAY_FLAG,
           OFF_DUTY_FLAG,
           CLASS_SYSTEM,
           CREATE_TM,
           NODE_KEY,
           STATE_FLG,
           SCHEDULE_DAILY_ID,
           EMP_POST_TYPE)
          SELECT SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
                 TMP.EMP_CODE,
                 TMP.BEGIN_DATE,
                 TMP.END_DATE,
                 TMP.BEGIN_TM1,
                 TMP.END_TM1,
                 TMP.TMR_DAY_FLAG,
                 TMP.OFF_DUTY_FLAG,
                 TMP.CLASS_SYSTEM,
                 SYSDATE,
                 TMP.NODE_KEY,
                 TMP.STATE_FLG,
                 TMP.ID,
                 TMP.EMP_POST_TYPE
            FROM TT_SAP_SYNCHRONOUS_TMP1 TMP
           WHERE TMP.BEGIN_TM_FLAG1 = 1
             AND TMP.BEGIN_TM_FLAG2 = 0
             AND TMP.BEGIN_TM_FLAG3 = 0
             OR TMP.OFF_DUTY_FLAG = 'OFF';

        --d.��ʱ���2��3����ӿڱ�[��ʱ���2����ʱ���3]
        FOR SAP_ROW IN (SELECT *
                          FROM TT_SAP_SYNCHRONOUS_TMP1 TMP
                         WHERE TMP.BEGIN_TM_FLAG1 = 1
                           AND TMP.BEGIN_TM_FLAG2 = 1) LOOP

          INSERT INTO TT_SAP_SYNCHRONOUS
            (ID,
             EMP_CODE,
             BEGIN_DATE,
             END_DATE,
             BEGIN_TM,
             END_TM,
             TMR_DAY_FLAG,
             OFF_DUTY_FLAG,
             CLASS_SYSTEM,
             CREATE_TM,
             NODE_KEY,
             STATE_FLG,
             SCHEDULE_DAILY_ID,
             EMP_POST_TYPE)
          VALUES
            (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
             SAP_ROW.EMP_CODE,
             SAP_ROW.BEGIN_DATE,
             SAP_ROW.BEGIN_DATE,
             SAP_ROW.BEGIN_TM1,
             SAP_ROW.END_TM1,
             SAP_ROW.TMR_DAY_FLAG,
             SAP_ROW.OFF_DUTY_FLAG,
             SAP_ROW.CLASS_SYSTEM,
             SAP_ROW.CREATE_TM,
             SAP_ROW.NODE_KEY,
             SAP_ROW.STATE_FLG,
             SAP_ROW.ID,
             SAP_ROW.EMP_POST_TYPE);
          --���ʱ���2
          IF SAP_ROW.BEGIN_TM_FLAG2 = 1 THEN
            BEGIN
             --���ʱ���1��ʼʱ�����ʱ���2��ʼʱ��,��ʼ���ںͽ������ڼ�1
             IF SAP_ROW.BEGIN_TM1 > SAP_ROW.BEGIN_TM2 THEN
                   INSERT INTO TT_SAP_SYNCHRONOUS
                    (ID,
                     EMP_CODE,
                     BEGIN_DATE,
                     END_DATE,
                     BEGIN_TM,
                     END_TM,
                     TMR_DAY_FLAG,
                     OFF_DUTY_FLAG,
                     CLASS_SYSTEM,
                     CREATE_TM,
                     NODE_KEY,
                     STATE_FLG,
                     SCHEDULE_DAILY_ID,
                     EMP_POST_TYPE)
                  VALUES
                    (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
                     SAP_ROW.EMP_CODE,
                     TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,'YYYYMMDD'),
                     TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,'YYYYMMDD'),
                     SAP_ROW.BEGIN_TM2,
                     SAP_ROW.END_TM2,
                     'X',
                     SAP_ROW.OFF_DUTY_FLAG,
                     SAP_ROW.CLASS_SYSTEM,
                     SAP_ROW.CREATE_TM,
                     SAP_ROW.NODE_KEY,
                     SAP_ROW.STATE_FLG,
                     SAP_ROW.ID,
                     SAP_ROW.EMP_POST_TYPE);
             --������������
             ELSE
                   INSERT INTO TT_SAP_SYNCHRONOUS
                    (ID,
                     EMP_CODE,
                     BEGIN_DATE,
                     END_DATE,
                     BEGIN_TM,
                     END_TM,
                     TMR_DAY_FLAG,
                     OFF_DUTY_FLAG,
                     CLASS_SYSTEM,
                     CREATE_TM,
                     NODE_KEY,
                     STATE_FLG,
                     SCHEDULE_DAILY_ID,
                     EMP_POST_TYPE)
                  VALUES
                    (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
                     SAP_ROW.EMP_CODE,
                     SAP_ROW.BEGIN_DATE,
                     SAP_ROW.BEGIN_DATE,
                     SAP_ROW.BEGIN_TM2,
                     SAP_ROW.END_TM2,
                     SAP_ROW.TMR_DAY_FLAG,
                     SAP_ROW.OFF_DUTY_FLAG,
                     SAP_ROW.CLASS_SYSTEM,
                     SAP_ROW.CREATE_TM,
                     SAP_ROW.NODE_KEY,
                     SAP_ROW.STATE_FLG,
                     SAP_ROW.ID,
                     SAP_ROW.EMP_POST_TYPE);
             END IF;

             END;
          END IF;
          --���ʱ���3
          IF SAP_ROW.BEGIN_TM_FLAG3 = 1 THEN
            BEGIN
              --���ʱ���1��ʼʱ�����ʱ���3��ʼʱ��,��ʼ���ںͽ������ڼ�1
              IF SAP_ROW.BEGIN_TM1 > SAP_ROW.BEGIN_TM3 THEN
                 INSERT INTO TT_SAP_SYNCHRONOUS
                  (ID,
                   EMP_CODE,
                   BEGIN_DATE,
                   END_DATE,
                   BEGIN_TM,
                   END_TM,
                   TMR_DAY_FLAG,
                   OFF_DUTY_FLAG,
                   CLASS_SYSTEM,
                   CREATE_TM,
                   NODE_KEY,
                   STATE_FLG,
                   SCHEDULE_DAILY_ID,
                   EMP_POST_TYPE)
                VALUES
                  (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
                   SAP_ROW.EMP_CODE,
                   TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,'YYYYMMDD'),
                   TO_CHAR(TO_DATE(SAP_ROW.BEGIN_DATE, 'YYYYMMDD') + 1,'YYYYMMDD'),
                   SAP_ROW.BEGIN_TM3,
                   SAP_ROW.END_TM3,
                   'X',
                   SAP_ROW.OFF_DUTY_FLAG,
                   SAP_ROW.CLASS_SYSTEM,
                   SAP_ROW.CREATE_TM,
                   SAP_ROW.NODE_KEY,
                   SAP_ROW.STATE_FLG,
                   SAP_ROW.ID,
                   SAP_ROW.EMP_POST_TYPE);
              ELSE
                 INSERT INTO TT_SAP_SYNCHRONOUS
                  (ID,
                   EMP_CODE,
                   BEGIN_DATE,
                   END_DATE,
                   BEGIN_TM,
                   END_TM,
                   TMR_DAY_FLAG,
                   OFF_DUTY_FLAG,
                   CLASS_SYSTEM,
                   CREATE_TM,
                   NODE_KEY,
                   STATE_FLG,
                   SCHEDULE_DAILY_ID,
                   EMP_POST_TYPE)
                VALUES
                  (SEQ_TT_SAP_SYNCHRONOUS.NEXTVAL,
                   SAP_ROW.EMP_CODE,
                   SAP_ROW.BEGIN_DATE,
                   SAP_ROW.BEGIN_DATE,
                   SAP_ROW.BEGIN_TM3,
                   SAP_ROW.END_TM3,
                   SAP_ROW.TMR_DAY_FLAG,
                   SAP_ROW.OFF_DUTY_FLAG,
                   SAP_ROW.CLASS_SYSTEM,
                   SAP_ROW.CREATE_TM,
                   SAP_ROW.NODE_KEY,
                   SAP_ROW.STATE_FLG,
                   SAP_ROW.ID,
                   SAP_ROW.EMP_POST_TYPE);
              END IF;

            END;
          END IF;
        END LOOP;

        --e.�޸�״̬
        UPDATE TT_PB_SHEDULE_BY_DAY D
          SET SYNCHRO_STATUS = 1
        WHERE D.ID IN (SELECT ID FROM TT_SAP_SYNCHRONOUS_TMP1 E);
        COMMIT;
        --f.ɾ����ʱ��
        EXECUTE IMMEDIATE 'TRUNCATE TABLE TT_SAP_SYNCHRONOUS_TMP1';
        ----���������Ű�������SAP�ӿڱ�--END
      END;
    END IF;
  END;

  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'OPERATION2SAP_SYNC',
                               SYSDATE,
                               NULL,
                               NULL,
                               'END',
                               0,
                               L_CALL_NO);
  --5.�쳣��¼��־
EXCEPTION
  WHEN OTHERS THEN
    --�ع�����
    ROLLBACK;
    PKG_OSS_COMM.STP_RUNNING_LOG('',
                                 'OPERATION2SAP_SYNC',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);

END OPERATION2SAP_SYNC;
/