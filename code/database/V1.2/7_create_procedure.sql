create or replace procedure SAP_SYNCHRONOUS_TO_SPMS(P_DATA_TYPE VARCHAR2) AS
  V_DEPT_ID NUMBER;

  --1.����ִ�����
  L_CALL_NO NUMBER;

BEGIN
  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SAP_SYNCHRONOUS_TO_SPMS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);


    -------���� ͨ���α����Ա�����Ƿ���ڣ�������������������������
    IF P_DATA_TYPE = 'EMP_ONE' THEN
      BEGIN
      
        FOR EMP_ROW IN (select e.emp_num,
                               e.last_name,
                               t.position_name,
                               e.NET_CODE,
                               e.persk_txt,
                               e.MAIL_ADDRESS,
                               e.cancel_date,
                               e.SF_DATE,
                               t.ZHRGWXL,
                               t.position_attr
                          from ti_sap_synchronous_emp e,
                               ti_sap_synchronous_pos t
                         where e.position_id = t.position_id(+)
                           and e.deal_flag <> 1) LOOP
          BEGIN
            SELECT T.DEPT_ID
              INTO V_DEPT_ID
              FROM TM_DEPARTMENT T
             WHERE T.DEPT_CODE = EMP_ROW.NET_CODE;
          
            insert into TM_OSS_EMPLOYEE_HISTORY
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
               DUTY_SERIAL,
               VERSION_NUMBER)
              SELECT SEQ_OSS_BASE.NEXTVAL,
                     EMP_CODE,
                     EMP_NAME,
                     EMP_DUTY_NAME,
                     DEPT_ID,
                     GROUP_ID,
                     sysdate,
                     '',
                     CREATE_EMP_CODE,
                     MODIFIED_EMP_CODE,
                     WORK_TYPE,
                     EMAIL,
                     DIMISSION_DT,
                     SF_DATE,
                     EMP_POST_TYPE,
                     IS_HAVE_COMMISSION,
                     POSITION_ATTR,
                     DUTY_SERIAL,
                     1
                FROM TM_OSS_EMPLOYEE T
               where t.emp_code = EMP_ROW.Emp_Num;
          
            UPDATE TM_OSS_EMPLOYEE EE
               SET EE.EMP_NAME = EMP_ROW.LAST_NAME,
                   EE.dept_id           = v_dept_id,
                   EE.EMP_DUTY_NAME     = EMP_ROW.POSITION_NAME,
                   EE.WORK_TYPE         = DECODE(EMP_ROW.persk_txt,
                                                 '���ں�ͬ��',
                                                 1,
                                                 '���ں�ͬ��',
                                                 2,
                                                 '������ǲ',
                                                 3,
                                                 '���ݷ�Ƹ',
                                                 4,
                                                 'ҵ�����',
                                                 5,
                                                 '���',
                                                 6,
                                                 '���ؼ�ϰ��',
                                                 7,
                                                 '�����ǲ',
                                                 8,
                                                 'ʵϰ��',
                                                 9,
                                                 'Сʱ��',
                                                 10,
                                                 '����',
                                                 11,
                                                 '�ڹ���ѧ',
                                                 12,
                                                 0),
                   EE.EMAIL             = EMP_ROW.MAIL_ADDRESS,
                   EE.DIMISSION_DT      = TO_DATE(EMP_ROW.CANCEL_DATE,
                                                  'yyyy-mm-dd'),
                   EE.SF_DATE           = TO_DATE(EMP_ROW.SF_DATE,
                                                  'yyyy-mm-dd'),
                   EE.MODIFIED_EMP_CODE = 'sapinterface',
                   EE.MODIFIED_TM       = SYSDATE,
                   EE.VERSION_NUMBER    = EE.VERSION_NUMBER + 1
             WHERE EE.EMP_CODE = EMP_ROW.EMP_NUM;
            ----������������
            IF SQL%ROWCOUNT = 0 THEN
              insert into tm_oss_employee
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
                 DUTY_SERIAL,
                 VERSION_NUMBER)
              values
                (SEQ_OSS_BASE.NEXTVAL,
                 EMP_ROW.EMP_NUM,
                 EMP_ROW.LAST_NAME,
                 EMP_ROW.POSITION_NAME,
                 V_DEPT_ID,
                 '',
                 sysdate,
                 sysdate,
                 'sapinterface',
                 'sapinterface',
                 DECODE(EMP_ROW.persk_txt,
                        '���ں�ͬ��',
                        1,
                        '���ں�ͬ��',
                        2,
                        '������ǲ',
                        3,
                        '���ݷ�Ƹ',
                        4,
                        'ҵ�����',
                        5,
                        '���',
                        6,
                        '���ؼ�ϰ��',
                        7,
                        '�����ǲ',
                        8,
                        'ʵϰ��',
                        9,
                        'Сʱ��',
                        10,
                        '����',
                        11,
                        '�ڹ���ѧ',
                        12,
                        0),
                 EMP_ROW.MAIL_ADDRESS,
                 EMP_ROW.CANCEL_DATE,
                 EMP_ROW.Sf_Date,
                 Gets_type_of_emp(EMP_ROW.POSITION_NAME,
                                  EMP_ROW.ZHRGWXL,
                                  EMP_ROW.POSITION_ATTR),
                 '',
                 EMP_ROW.POSITION_ATTR,
                 EMP_ROW.Zhrgwxl,
                 0);
            
            END IF;
            
            -- ����ͬ��״̬
            update ti_sap_synchronous_emp ti_emp set ti_emp.deal_flag = 1 where ti_emp.emp_num = EMP_ROW.Emp_Num;
          
            COMMIT;
            ----ѭ�����쳣����
          EXCEPTION
            WHEN OTHERS THEN
              DBMS_OUTPUT.PUT_LINE('SAP_SYNCHRONOUS_TO_SPMS' || SQLCODE || '��' ||
                                   SQLERRM);
              ROLLBACK;
          END;
        
        END LOOP;
      
      END;
    
    END IF;
  
  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SAP_SYNCHRONOUS_TO_SPMS',
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
                                 'SAP_SYNCHRONOUS_TO_SPMS',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
END SAP_SYNCHRONOUS_TO_SPMS;
/