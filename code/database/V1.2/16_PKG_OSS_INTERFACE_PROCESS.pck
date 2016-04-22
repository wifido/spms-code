CREATE OR REPLACE PACKAGE PKG_OSS_INTERFACE_PROCESS IS

  -- AUTHOR  : BP
  -- CREATED : 2014-7-17
  -- PURPOSE : ��Oracle Hr ����ͬ���ӿڴ���

  --�Խ��յ������ݽ���ȫ����������
  PROCEDURE STP_OSS_HR_ALL_OR_UPDATE(P_DATA_TYPE VARCHAR2,
                                     P_JOURANL   VARCHAR2);

END PKG_OSS_INTERFACE_PROCESS;
/
CREATE OR REPLACE PACKAGE BODY PKG_OSS_INTERFACE_PROCESS IS
  PROCEDURE STP_OSS_HR_ALL_OR_UPDATE(P_DATA_TYPE VARCHAR2,
                                     P_JOURANL   VARCHAR2) AS
    V_COUNT             INT;
    V_DEPT_ID           NUMBER;
    V_INNER_MAX_EMPCODE INT := 100000000;
    --1.����ִ�����
    L_CALL_NO           NUMBER;
    V_OLD_DEPT_ID       NUMBER;
    V_EMP_COUNT             INT;
  BEGIN
    --2.����ִ�����
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.��ʼ��¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_OSS_INTERFACE_PROCESS',
                                 'STP_OSS_HR_ALL_OR_UPDATE',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    --���������������С�
    UPDATE TI_OSS_HR_EMP_INFO_ALTER A SET A.DUTY_SERIAL = '������������' WHERE A.POSITION_NAME IN ('��������Ա','�����鳤','���������鳤','�������Ա','����Ա','����Ա','�����鳤','����Ա','��������Ա');
    COMMIT;

    SELECT COUNT(*)
      INTO V_COUNT
      FROM TS_OSS_ESB_BIG_FILE_RESEND
     WHERE DATA_TYPE = P_DATA_TYPE
       AND JOURNAL_ID = P_JOURANL;

    IF V_COUNT > 0 THEN
      ------�����ȫ�����£�ֻȡ��Ӧ�����������е�ֵ��tm_oss_employee��
      -----Ĭ�������Ķ�����Ч��Ա��
      IF P_DATA_TYPE = 'HRS_EMP_INIT' THEN

        Loop
           SELECT COUNT(1) INTO V_EMP_COUNT FROM (
           SELECT emp_code FROM TI_OSS_HR_EMP_INFO t
        WHERE t.journal_id = P_JOURANL and
              t.duty_serial = '������������'
              GROUP BY emp_code HAVING COUNT(1) > 1);

        IF V_EMP_COUNT > 0 THEN
         DELETE ti_oss_hr_emp_info WHERE  EMP_ID IN  (SELECT MIN(EMP_ID) FROM  ti_oss_hr_emp_info t
          WHERE t.journal_id = P_JOURANL and
                t.duty_serial = '������������'
                GROUP BY emp_code HAVING COUNT(1) > 1);

           COMMIT;
         ELSE
           EXIT;
         END IF ;
        END LOOP;


        DELETE TM_OSS_EMPLOYEE E
         WHERE TO_NUMBER(E.EMP_CODE) < V_INNER_MAX_EMPCODE;

        INSERT INTO TM_OSS_EMPLOYEE
          (EMP_ID,
           EMP_CODE,
           EMP_NAME,
           EMP_DUTY_NAME,
           DEPT_ID,
           WORK_TYPE,
           EMAIL,
           SF_DATE,
           CREATE_EMP_CODE,
           CREATE_TM)
          SELECT SEQ_OSS_BASE.NEXTVAL,
                 T.EMP_CODE,
                 T.EMP_NAME,
                 T.POSITION_NAME,
                 DEPT.DEPT_ID,
                 DECODE(T.PERSON_TYPE,
                        '��ȫ���ƹ�',
                        1,
                        '���ؼ�ϰ��',
                        2,
                        '������ǲ',
                        3,
                        'ȫ����Ա��',
                        4,
                        'ʵϰ��',
                        5,
                        '���',
                        6,
                         '�ڹ���ѧ',
                        7,
                         '����',
                        8,
                         '���˳а���Ӫ��',
                        9,
                         'ҵ�����',
                        10,
                        0),
                 T.EMP_EMAIL,
                 TO_DATE(T.SF_DATE, 'yyyy-mm-dd'),
                 'ossinteface',
                 SYSDATE
            FROM TI_OSS_HR_EMP_INFO T
           INNER JOIN TM_DEPARTMENT DEPT
              ON T.DEPT_CODE = DEPT.DEPT_CODE
              WHERE T.JOURNAL_ID =P_JOURANL
                               AND T.DUTY_SERIAL = '������������';
        COMMIT;
      END IF;

      -------��������� ͨ���α����Ա�����Ƿ���ڣ�������������������������
      IF P_DATA_TYPE = 'EMP_ONE' THEN
        BEGIN

          FOR EMP_ROW IN (SELECT *
                            FROM TI_OSS_HR_EMP_INFO_ALTER HR
                           WHERE HR.JOURNAL_ID = P_JOURANL
                             AND HR.DUTY_SERIAL = '������������') LOOP
            BEGIN
              SELECT T.DEPT_ID
                INTO V_DEPT_ID
                FROM TM_DEPARTMENT T
               WHERE T.DEPT_CODE = EMP_ROW.DEPT_CODE;

              UPDATE TM_OSS_EMPLOYEE EE
                 SET EE.EMP_NAME = EMP_ROW.EMP_NAME,
                     -----����˴β�ֱ���޸ģ����ܻ���ת����
                     ----- ee.dept_id           = v_dept_id,
                     EE.EMP_DUTY_NAME     = EMP_ROW.EMP_DUTY_NAME,
                     EE.WORK_TYPE         = DECODE(EMP_ROW.PERSON_TYPE,
                                                  '��ȫ���ƹ�',
                                                  1,
                                                  '���ؼ�ϰ��',
                                                  2,
                                                  '������ǲ',
                                                  3,
                                                  'ȫ����Ա��',
                                                  4,
                                                  'ʵϰ��',
                                                  5,
                                                  '���',
                                                  6,
                                                   '�ڹ���ѧ',
                                                  7,
                                                   '����',
                                                  8,
                                                   '���˳а���Ӫ��',
                                                  9,
                                                   'ҵ�����',
                                                  10,
                                                  0),
                     EE.EMAIL             = EMP_ROW.EMP_EMAIL,
                     EE.DIMISSION_DT      = TO_DATE(EMP_ROW.CANCEL_DATE,
                                                    'yyyy-mm-dd'),
                     EE.SF_DATE           = TO_DATE(EMP_ROW.SF_DATE,
                                                    'yyyy-mm-dd'),
                     EE.MODIFIED_EMP_CODE = 'ossinterface',
                     EE.MODIFIED_TM       = SYSDATE
               WHERE EE.EMP_CODE = EMP_ROW.EMP_CODE;
              ----������������
              IF SQL%ROWCOUNT = 0 THEN
                ---------���������Ա��פ������
                INSERT INTO TI_OSS_HR_EMP_NEW_CHANGEDEPT
                  (EMP_ID,
                   EMP_CODE,
                   EMP_DUTY_NAME,
                   EMP_TYPE_NAME,
                   EMP_NAME,
                   EMP_GENDER,
                   EMP_EMAIL,
                   EMP_MOBILE,
                   EMP_OFFICEPHONE,
                   EMP_STUS,
                   REGISTER_DT,
                   LOGOUT_DT,
                   EMP_DESC,
                   VALID_FLG,
                   DEPT_CODE,
                   CHANGE_ZONE_TM,
                   INNER_FLG,
                   PERSON_TYPE,
                   POSITION_NAME,
                   POSITION_STATUS,
                   CREATED_EMP_CODE,
                   CREATED_TM,
                   MODIFIED_EMP_CODE,
                   MODIFIED_TM,
                   HRS_EMP_ID,
                   CANCEL_DATE,
                   SF_DATE,
                   CANCEL_FLAG,
                   DEAL_FLAG,
                   INTERFACE_ID)
                VALUES
                  (SEQ_OSS_TL.NEXTVAL,
                   EMP_ROW.EMP_CODE,
                   EMP_ROW.EMP_DUTY_NAME,
                   EMP_ROW.EMP_TYPE_NAME,
                   EMP_ROW.EMP_NAME,
                   EMP_ROW.EMP_GENDER,
                   EMP_ROW.EMP_EMAIL,
                   EMP_ROW.EMP_MOBILE,
                   EMP_ROW.EMP_OFFICEPHONE,
                   1,
                   EMP_ROW.REGISTER_DT,
                   EMP_ROW.LOGOUT_DT,
                   EMP_ROW.EMP_DESC,
                   EMP_ROW.VALID_FLG,
                   EMP_ROW.DEPT_CODE,
                   EMP_ROW.CHANGE_ZONE_TM,
                   EMP_ROW.INNER_FLG,
                   EMP_ROW.PERSON_TYPE,
                   EMP_ROW.POSITION_NAME,
                   EMP_ROW.POSITION_STATUS,
                   EMP_ROW.CREATED_EMP_CODE,
                   SYSDATE,
                   EMP_ROW.MODIFIED_EMP_CODE,
                   EMP_ROW.MODIFIED_TM,
                   EMP_ROW.HRS_EMP_ID,
                   EMP_ROW.CANCEL_DATE,
                   EMP_ROW.SF_DATE,
                   EMP_ROW.CANCEL_FLAG,
                   0,
                   EMP_ROW.EMP_ALTER_ID

                   );
              ELSE
                -----�ж��Ƿ�ת����
                SELECT EE.DEPT_ID
                  INTO V_OLD_DEPT_ID
                  FROM TM_OSS_EMPLOYEE EE
                 WHERE EE.EMP_CODE = EMP_ROW.EMP_CODE;
                IF V_OLD_DEPT_ID <> V_DEPT_ID THEN
                  INSERT INTO TI_OSS_HR_EMP_NEW_CHANGEDEPT
                    (EMP_ID,
                     EMP_CODE,
                     EMP_DUTY_NAME,
                     EMP_TYPE_NAME,
                     EMP_NAME,
                     EMP_GENDER,
                     EMP_EMAIL,
                     EMP_MOBILE,
                     EMP_OFFICEPHONE,
                     EMP_STUS,
                     REGISTER_DT,
                     LOGOUT_DT,
                     EMP_DESC,
                     VALID_FLG,
                     DEPT_CODE,
                     CHANGE_ZONE_TM,
                     INNER_FLG,
                     PERSON_TYPE,
                     POSITION_NAME,
                     POSITION_STATUS,
                     CREATED_EMP_CODE,
                     CREATED_TM,
                     MODIFIED_EMP_CODE,
                     MODIFIED_TM,
                     HRS_EMP_ID,
                     CANCEL_DATE,
                     SF_DATE,
                     CANCEL_FLAG,
                     DEAL_FLAG,
                     INTERFACE_ID)
                  VALUES
                    (SEQ_OSS_TL.NEXTVAL,
                     EMP_ROW.EMP_CODE,
                     EMP_ROW.EMP_DUTY_NAME,
                     EMP_ROW.EMP_TYPE_NAME,
                     EMP_ROW.EMP_NAME,
                     EMP_ROW.EMP_GENDER,
                     EMP_ROW.EMP_EMAIL,
                     EMP_ROW.EMP_MOBILE,
                     EMP_ROW.EMP_OFFICEPHONE,
                     2,
                     EMP_ROW.REGISTER_DT,
                     EMP_ROW.LOGOUT_DT,
                     EMP_ROW.EMP_DESC,
                     EMP_ROW.VALID_FLG,
                     EMP_ROW.DEPT_CODE,
                     EMP_ROW.CHANGE_ZONE_TM,
                     EMP_ROW.INNER_FLG,
                     EMP_ROW.PERSON_TYPE,
                     EMP_ROW.POSITION_NAME,
                     EMP_ROW.POSITION_STATUS,
                     EMP_ROW.CREATED_EMP_CODE,
                     SYSDATE,
                     EMP_ROW.MODIFIED_EMP_CODE,
                     EMP_ROW.MODIFIED_TM,
                     EMP_ROW.HRS_EMP_ID,
                     EMP_ROW.CANCEL_DATE,
                     EMP_ROW.SF_DATE,
                     EMP_ROW.CANCEL_FLAG,
                     0,
                     EMP_ROW.EMP_ALTER_ID

                     );
                END IF;

              END IF;
              ---------���������ת�����¼

              COMMIT;
              ----ѭ�����쳣����
            EXCEPTION
              WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('STP_OSS_HR_ALL_OR_UPDATE' || SQLCODE || '��' ||
                                     SQLERRM);
                ROLLBACK;
            END;

          END LOOP;



        END;

        begin
        FOR NOT_OPENATION_EMP_ROW IN (SELECT *
                            FROM TI_OSS_HR_EMP_INFO_ALTER HR
                           WHERE HR.JOURNAL_ID = P_JOURANL
                             AND (HR.DUTY_SERIAL <> '������������' OR HR.DUTY_SERIAL IS NULL)) LOOP
          BEGIN
           UPDATE TM_OSS_EMPLOYEE EE
                 SET EE.EMP_NAME = NOT_OPENATION_EMP_ROW.EMP_NAME,
                     EE.DEPT_ID = (SELECT TM.DEPT_ID
                  FROM TM_DEPARTMENT  TM
                  WHERE TM.DEPT_CODE = NOT_OPENATION_EMP_ROW.DEPT_CODE),
                     EE.EMP_DUTY_NAME     = NOT_OPENATION_EMP_ROW.EMP_DUTY_NAME,
                     EE.WORK_TYPE         = DECODE(NOT_OPENATION_EMP_ROW.PERSON_TYPE,
                                                  '��ȫ���ƹ�',
                                                  1,
                                                  '���ؼ�ϰ��',
                                                  2,
                                                  '������ǲ',
                                                  3,
                                                  'ȫ����Ա��',
                                                  4,
                                                  'ʵϰ��',
                                                  5,
                                                  '���',
                                                  6,
                                                   '�ڹ���ѧ',
                                                  7,
                                                   '����',
                                                  8,
                                                   '���˳а���Ӫ��',
                                                  9,
                                                   'ҵ�����',
                                                  10,
                                                  0),
                     EE.EMAIL             = NOT_OPENATION_EMP_ROW.EMP_EMAIL,
                     EE.DIMISSION_DT      = TO_DATE(NOT_OPENATION_EMP_ROW.CANCEL_DATE,
                                                    'yyyy-mm-dd'),
                     EE.SF_DATE           = TO_DATE(NOT_OPENATION_EMP_ROW.SF_DATE,
                                                    'yyyy-mm-dd'),
                     EE.MODIFIED_EMP_CODE = 'ossinterface',
                     EE.MODIFIED_TM       = SYSDATE,
                     EE.EMP_POST_TYPE = Gets_type_of_emp(NOT_OPENATION_EMP_ROW.POSITION_NAME,NOT_OPENATION_EMP_ROW.Duty_Serial,NOT_OPENATION_EMP_ROW.POSITION_ATTR),
                     EE.Duty_Serial = NOT_OPENATION_EMP_ROW.Duty_Serial,
                     EE.POSITION_ATTR = NOT_OPENATION_EMP_ROW.POSITION_ATTR
               WHERE EE.EMP_CODE = NOT_OPENATION_EMP_ROW.EMP_CODE;
              ----������������
              IF SQL%ROWCOUNT = 0 THEN
                insert into TM_OSS_EMPLOYEE
                 (EMP_ID,
                  EMP_CODE,
                  EMP_NAME,
                  EMP_DUTY_NAME,
                  DEPT_ID,
                  GROUP_ID,
                  CREATE_TM,
                  MODIFIED_TM,
                  CREATE_EMP_CODE,
                  MODIFIED_EMP_CODE,
                  WORK_TYPE,
                  EMAIL,
                  DIMISSION_DT,
                  SF_DATE,
                  EMP_POST_TYPE,
                  IS_HAVE_COMMISSION,
                  POSITION_ATTR,
                  DUTY_SERIAL)
                  values (
                  SEQ_OSS_BASE.NEXTVAL,
                  NOT_OPENATION_EMP_ROW.Emp_Code,
                  NOT_OPENATION_EMP_ROW.Emp_Name,
                  NOT_OPENATION_EMP_ROW.EMP_DUTY_NAME,
                  (SELECT TM.DEPT_ID
                  FROM TM_DEPARTMENT  TM
                  WHERE TM.DEPT_CODE = NOT_OPENATION_EMP_ROW.DEPT_CODE),
                  null,
                  sysdate,
                  sysdate,
                  '',
                  '',
                  DECODE(NOT_OPENATION_EMP_ROW.PERSON_TYPE,
                                                  '��ȫ���ƹ�',
                                                  1,
                                                  '���ؼ�ϰ��',
                                                  2,
                                                  '������ǲ',
                                                  3,
                                                  'ȫ����Ա��',
                                                  4,
                                                  'ʵϰ��',
                                                  5,
                                                  '���',
                                                  6,
                                                   '�ڹ���ѧ',
                                                  7,
                                                   '����',
                                                  8,
                                                   '���˳а���Ӫ��',
                                                  9,
                                                   'ҵ�����',
                                                  10,
                                                  0),
                  NOT_OPENATION_EMP_ROW.EMP_EMAIL,
                  TO_DATE(NOT_OPENATION_EMP_ROW.CANCEL_DATE,
                                                    'yyyy-mm-dd'),
                  TO_DATE(NOT_OPENATION_EMP_ROW.SF_DATE,
                                                    'yyyy-mm-dd'),
                  Gets_type_of_emp(NOT_OPENATION_EMP_ROW.EMP_DUTY_NAME,NOT_OPENATION_EMP_ROW.Duty_Serial,NOT_OPENATION_EMP_ROW.POSITION_ATTR),
                  '',
                  NOT_OPENATION_EMP_ROW.POSITION_ATTR,
                  NOT_OPENATION_EMP_ROW.Duty_Serial
                  );
                    END IF;

              COMMIT;
              ----ѭ�����쳣����
            EXCEPTION
              WHEN OTHERS THEN
                ROLLBACK;
            END;
          END LOOP;
     end;


      END IF;

    END IF;
    --4.������¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_OSS_INTERFACE_PROCESS',
                                 'STP_OSS_HR_ALL_OR_UPDATE',
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
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_OSS_INTERFACE_PROCESS',
                                   'STP_OSS_HR_ALL_OR_UPDATE',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR',
                                   0,
                                   L_CALL_NO);
  END STP_OSS_HR_ALL_OR_UPDATE;

END PKG_OSS_INTERFACE_PROCESS;
/
