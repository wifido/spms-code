CREATE OR REPLACE PACKAGE BODY PKG_HANDLE_COMPARE_REPORT IS
  ---�����Ű����ݴ��������ݶԱ�����
  PROCEDURE P_REPORT_CAUSE_BY_SCHEDULE IS
    --*************************************************************
    -- AUTHOR  : ����
    -- CREATED : 2015-09-08
    -- PURPOSE : �Ű����г���־�쳣�Աȱ���ִ������;����ֻ�����Ű����ݴ��������ݶԱ�;
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************

    --�Ű��е���·����;
    L_SCHEDULE_LINE_COUNT NUMBER;
    -- ��ʱ�洢��·������ֶ�;
    L_DEPT_CODE VARCHAR2(2000);
    -- �г���־�е���·����;
    L_DRIVING_LINE_COUNT NUMBER;
    -- ���ս��;
    L_FINAL_RESULT NUMBER;
    -- ��������
    L_DEPT_NAME VARCHAR2(50);
    -- ��������
    L_AREA_CODE VARCHAR2(50);
    -- ��������
    L_AREA_NAME VARCHAR2(50);
    -- Ա������
    L_EMP_NAME VARCHAR2(50);
    --�Ѵ��ڱ�������;
    EXIST_REPORT NUMBER;
    ----�г���־��Ӧ��ID;
    L_LINE_ID VARCHAR2(21);

    --1.����ִ�����
    L_CALL_NO NUMBER;
  BEGIN
    --2.����ִ�����
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.��ʼ��¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_SCHEDULE',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    --����һ��������
    UPDATE TT_TASK_COMPARE SET SYNC_STATUS = 1;
    COMMIT;

    --��ѯ�ƻ�������е�����;Ϊ��ֹ��������,��Ҫ����ÿ��ִ�е�����
    FOR V_TEMP IN (SELECT * FROM TT_TASK_COMPARE WHERE SYNC_STATUS = 1) LOOP
      ----CLEAN PARAMETER WITH DEFAULT-------------------------------------------------------------------------------------------------------------------

      -- ���ս��;
      L_FINAL_RESULT := -1;
      -- ��������
      L_DEPT_NAME := '';
      -- ��������
      L_AREA_CODE := '';
      -- ��������
      L_AREA_NAME := '';
      -- Ա������;
      L_EMP_NAME   := '';
      EXIST_REPORT := 0;
      L_LINE_ID    := -1;
      --����ɾ������ʱ;
      IF V_TEMP.OPERATION_TYPE <> 2 THEN
        -- 1.��ѯ�����ڶ�Ӧ���г���־;
        SELECT COUNT(*)
          INTO L_SCHEDULE_LINE_COUNT
          FROM TT_DRIVER_LINE_CONFIGURE T, TT_DRIVER_LINE_CONFIGURE_R TT
         WHERE DEPARTMENT_CODE =
               substr(V_TEMP.CONFIGURE_CODE,
                      0,
                      instr(V_TEMP.CONFIGURE_CODE, '-') - 1)
           AND CODE = substr(V_TEMP.CONFIGURE_CODE,
                             instr(V_TEMP.CONFIGURE_CODE, '-') + 1)
           AND T.ID = TT.CONFIGURE_ID
           AND T.MONTH = V_TEMP.YEAR_MONTH;

      ELSE
        L_SCHEDULE_LINE_COUNT := 0;
      END IF;

      -- 2. ��ѯ�г���־�������µ���·����;
      SELECT MAX(DEPT_CODE), MAX(ID)
        INTO L_DEPT_CODE, L_LINE_ID
        FROM TI_DRIVER_LOG_TMP
       WHERE DRIVE_MEMBER = V_TEMP.EMPLOYEE_CODE
         AND DAY_OF_MONTH = REPLACE(V_TEMP.DAY_OF_MONTH, '-', '');

      -- ���㱻ƴ�ӵ���·�е�����;
      IF L_DEPT_CODE IS NULL THEN
        L_DRIVING_LINE_COUNT := 0;
      ELSE
        SELECT LENGTH(L_DEPT_CODE) - LENGTH(REPLACE(L_DEPT_CODE, '-'))
          INTO L_DRIVING_LINE_COUNT
          FROM DUAL;
      END IF;

      -- 3.�ж����նԱȽ��;

      /**
      * "���Ű�δ�г�", "���г�δ�Ű�", "�Ű�����г���·", "�Ű������г���·"
      *
      **/
      ------------------------------------------SPLIT LINE -------------------------------------------------------------------
      -----��ȡ�����ս��;
      IF L_SCHEDULE_LINE_COUNT = 0 THEN
        ---�Ű���г���Ϊ0;ֻ�п�����ɾ���Ű�ʱ�����ڶԱȽ��;
        IF L_DRIVING_LINE_COUNT = 0 THEN
          L_FINAL_RESULT := 5;
        ELSE
          ---δ�Ű����г�;
          L_FINAL_RESULT := 2;
        END IF;
      ELSE
        IF L_SCHEDULE_LINE_COUNT = L_DRIVING_LINE_COUNT THEN
          --�Ա����쳣;
          L_FINAL_RESULT := 0;
        ELSE
          IF L_DRIVING_LINE_COUNT = 0 THEN
            L_FINAL_RESULT := 1;
          ELSE
            IF L_SCHEDULE_LINE_COUNT < L_DRIVING_LINE_COUNT THEN
              L_FINAL_RESULT := 4;
            ELSE
              L_FINAL_RESULT := 3;
            END IF;
          END IF;
        END IF;
      END IF;

      --��ѯ�������Ӧ������,��������;
      SELECT MAX(T.DEPT_NAME), MAX(T.AREA_CODE), MAX(TT.DEPT_NAME)
        INTO L_DEPT_NAME, L_AREA_CODE, L_AREA_NAME
        FROM TM_DEPARTMENT T, TM_DEPARTMENT TT
       WHERE T.DEPT_CODE = V_TEMP.DEPT_CODE
         AND T.DELETE_FLG = 0
         AND T.AREA_CODE = TT.DEPT_CODE;

      --��ѯԱ������;
      SELECT MAX(T.EMP_NAME)
        INTO L_EMP_NAME
        FROM TM_OSS_EMPLOYEE T
       WHERE T.EMP_CODE = V_TEMP.EMPLOYEE_CODE;

      SELECT COUNT(*)
        INTO EXIST_REPORT
        FROM TT_COMPARED_REPORT
       WHERE EMPLOYEE_CODE = V_TEMP.EMPLOYEE_CODE
         AND DAY_MONTH = V_TEMP.DAY_OF_MONTH;

      IF EXIST_REPORT = 0 THEN
        INSERT INTO TT_COMPARED_REPORT
          (ID,
           ---------------
           EMPLOYEE_CODE,
           EMPLOYEE_NAME,
           -------------------
           DEPARTMENT_CODE,
           DEPARTMENT_NAME,
           ------------------
           AREA_CODE,
           AREA_NAME,
           ------------------
           COMPARE_RESULT,
           DAY_MONTH,
           MODIFIED_TIME)
        VALUES
          (SEQ_TASK_COMPARE.NEXTVAL,
           ----------------
           V_TEMP.EMPLOYEE_CODE,
           L_EMP_NAME,
           ---------------
           V_TEMP.DEPT_CODE,
           L_DEPT_NAME,
           ----------------
           L_AREA_CODE,
           L_AREA_NAME,
           -------------------
           L_FINAL_RESULT,
           V_TEMP.DAY_OF_MONTH,
           SYSDATE);
      ELSE
        UPDATE TT_COMPARED_REPORT
           SET COMPARE_RESULT = L_FINAL_RESULT, MODIFIED_TIME = SYSDATE
         WHERE EMPLOYEE_CODE = V_TEMP.EMPLOYEE_CODE
           AND DAY_MONTH = V_TEMP.DAY_OF_MONTH;
      END IF;

      --д�뱨������;
      UPDATE TI_DRIVER_LOG_TMP SET COMPARE_STATUS = 1 WHERE ID = L_LINE_ID;
    END LOOP;
    ------------------------------------------SPLIT LINE -------------------------------------------------------------------

    DELETE TT_TASK_COMPARE WHERE SYNC_STATUS = 1;
    COMMIT;
    --4.������¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_SCHEDULE',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'END',
                                 0,
                                 L_CALL_NO);

    --5.�쳣��¼��־
  EXCEPTION
    WHEN OTHERS THEN
      UPDATE TT_TASK_COMPARE SET SYNC_STATUS = 0 WHERE SYNC_STATUS = 1;
      COMMIT;
      --�ع�����
      ROLLBACK;
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'P_REPORT_CAUSE_BY_SCHEDULE',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR' || EXIST_REPORT || L_EMP_NAME,
                                   0,
                                   '');
  END P_REPORT_CAUSE_BY_SCHEDULE;

  --�����г����ݴ��������ݶԱ�����
  PROCEDURE P_REPORT_CAUSE_BY_DRIVING IS
    --*************************************************************
    -- AUTHOR  : ����
    -- CREATED : 2015-09-08
    -- PURPOSE : �Ű����г���־�쳣�Աȱ���ִ������;����ֻ�����г���־���ݴ��������ݶԱ�;
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************

    --1.����ִ�����
    L_CALL_NO NUMBER;
  BEGIN

    --2.����ִ�����
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.��ʼ��¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_DRIVING',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);

    -- ��������Ϊ������
    update ti_driver_log_tmp t
       set t.compare_status = 3
     where t.compare_status = 0;

    COMMIT;

    execute immediate 'truncate table TT_COMPARED_REPORT_TEMP';

    INSERT INTO TT_COMPARED_REPORT_TEMP
      SELECT J.ID,
             R.EMP_CODE,
             R.EMP_NAME,
             R.DEPT_CODE,
             R.DEPT_NAME,
             R.AREA_CODE,
             R.AREA_NAME,
             CASE
               WHEN L_SCHEDULE_LINE_COUNT <> 0 THEN
                CASE
                  WHEN L_DRIVING_LINE_COUNT = 0 THEN
                   1
                  WHEN L_DRIVING_LINE_COUNT <> 0 THEN
                   CASE
                     WHEN L_SCHEDULE_LINE_COUNT = L_DRIVING_LINE_COUNT THEN
                      0
                     WHEN L_SCHEDULE_LINE_COUNT > L_DRIVING_LINE_COUNT THEN
                      3
                     ELSE
                      4
                   END
                END
             -- ���Ű�
               WHEN L_SCHEDULE_LINE_COUNT = 0 THEN
                CASE
                  WHEN L_DRIVING_LINE_COUNT = 0 THEN
                   5
                  ELSE
                   2
                END --���Ű�
               ELSE
                2
             END COMPARE_RESULT,
             J.DAY_OF_MONTH,
             SYSDATE,
             SYSDATE
        FROM (SELECT MAX(P.ID) ID,
                     LENGTH(MAX(P.DEPT_CODE)) -
                     LENGTH(REPLACE(MAX(P.DEPT_CODE), '-')) L_DRIVING_LINE_COUNT,
                     P.DRIVE_MEMBER,
                     P.DAY_OF_MONTH,
                     P.Compare_Status
                FROM TI_DRIVER_LOG_TMP P
               GROUP BY P.DRIVE_MEMBER, P.DAY_OF_MONTH, P.Compare_Status) J,
             (SELECT V_TEMP.DRIVE_MEMBER,
                     V_TEMP.DAY_OF_MONTH,
                     COUNT(R.CONFIGURE_ID) L_SCHEDULE_LINE_COUNT
                FROM TI_DRIVER_LOG_TMP          V_TEMP, --�г���־
                     TT_DRIVER_SCHEDULING       T, --�Ű��
                     TT_DRIVER_LINE_CONFIGURE   E, --����
                     TT_DRIVER_LINE_CONFIGURE_R R --�������·��ϵ��
               WHERE T.EMPLOYEE_CODE = V_TEMP.DRIVE_MEMBER
                 AND T.DAY_OF_MONTH = V_TEMP.DAY_OF_MONTH
                 AND T.SCHEDULING_TYPE = 1
                 AND E.DEPARTMENT_CODE || '-' || E.CODE = T.CONFIGURE_CODE
                 AND E.MONTH = substr(T.DAY_OF_MONTH, 0, 4) || '-' ||
                     substr(T.DAY_OF_MONTH, 5, 2)
                 AND E.ID = R.CONFIGURE_ID
               GROUP BY V_TEMP.DRIVE_MEMBER, V_TEMP.DAY_OF_MONTH) S,
             (SELECT B.DEPT_CODE,
                     B.DEPT_NAME,
                     C.DEPT_CODE AREA_CODE,
                     C.DEPT_NAME AREA_NAME,
                     A.EMP_NAME,
                     A.EMP_CODE
                FROM TM_OSS_EMPLOYEE A, TM_DEPARTMENT B, TM_DEPARTMENT C
               WHERE A.DEPT_ID = B.DEPT_ID
                 AND B.AREA_CODE = C.DEPT_CODE) R
       WHERE J.DRIVE_MEMBER = S.DRIVE_MEMBER(+)
         AND J.DAY_OF_MONTH = S.DAY_OF_MONTH(+)
         AND J.DRIVE_MEMBER = R.EMP_CODE
         AND j.Compare_Status = 3;

    -- �г���־���ݴ����µ�����
    merge into TT_COMPARED_REPORT T1 --TT_COMPARED_REPORT������Ҫ���µı�
    using TT_COMPARED_REPORT_TEMP T2 -- ������
    on (t1.employee_code = t2.employee_code and t1.day_month = t2.day_month) --��������
    when matched then --ƥ����������������´���
      update
         set T1.COMPARE_RESULT = T2.COMPARE_RESULT,
             t1.MODIFIED_TIME  = sysdate --�˴�ֻ��˵������ͬʱ���¶���ֶΡ�


    when not matched then --��ƥ����������������봦�����ֻ�������£������������ʡ�ԡ�
      insert
      values
        (t2.ID,
         t2.employee_code,
         t2.employee_name,
         t2.department_code,
         t2.department_name,
         t2.area_code,
         t2.area_name,
         t2.COMPARE_RESULT,
         t2.day_month,
         sysdate,
         sysdate);

    ------------------------------------------SPLIT LINE -------------------------------------------------------------------
    --�޸�һ�±�������;
    UPDATE ti_driver_log_tmp
       SET COMPARE_STATUS = 1
     WHERE COMPARE_STATUS = 3;
    COMMIT;

    --4.������¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'P_REPORT_CAUSE_BY_DRIVING',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'END',
                                 0,
                                 L_CALL_NO);

  EXCEPTION
    WHEN OTHERS THEN
      -- �����쳣ʱ���ع�����״̬
      update ti_driver_log_tmp t
         set t.compare_status = 0
       where t.compare_status = 3;

      COMMIT;
      --�ع�����
      ROLLBACK;
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'P_REPORT_CAUSE_BY_DRIVING',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR',
                                   0,
                                   '');
  END P_REPORT_CAUSE_BY_DRIVING;

  -- �����г���־����
  PROCEDURE HANDLE_DRIVING_CONVERT_DATA IS
    --*************************************************************
    -- AUTHOR  : ����
    -- CREATED : 2015-09-09
    -- PURPOSE : ETL������ȡ�ɹ��󣬴������г���־���ݲ��������ʱ������ʻʱ����ƴ����·�������
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************
    TEMP_DRIVER_MEMBER        VARCHAR2(50); -- Ա������
    TEMP_JOIN_DEPARTMENT_CODE VARCHAR2(2000); --  ƴ����·����
    TEMP_DRIVE_TM             VARCHAR2(2000); -- ����ʱ��
    TEMP_DAY_ATTENDANCE_TIME  NUMBER(19, 4); -- ����ʱ��
    TEMP_DAY_DRIVE_TIME       NUMBER(19, 4); -- ��ʻʱ��
    TEMP_START_TIME           date; -- ��ʼʱ��
    TEMP_END_TIME             date; -- ����ʱ��

    --1.����ִ�����
    L_CALL_NO NUMBER;

  BEGIN
    --2.����ִ�����
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.��ʼ��¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_DRIVING_CONVERT_DATA',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);

    execute immediate 'truncate table TI_DRIVER_LOG_TIME_TMP';

    update ti_vms_drive_convert t
       set t.sync_export_report = 2
     where t.sync_export_report = 0;

    COMMIT;

    insert into TI_DRIVER_LOG_TIME_TMP
      (id,
       drive_tm,
       end_tm,
       drive_member,
       start_place,
       end_place,
       DRIVE_TIME)
      select SEQ_TI_DRIVER_TIME.NEXTVAL,
             t.drive_tm,
             t.end_tm,
             t.drive_member,
             t.start_place,
             t.end_place,
             t.DRIVE_TIME
        from (select c.drive_tm,
                     lead(c.drive_tm, 1, c.end_tm) over(partition by c.driving_log_item_id order by c.drive_tm) end_tm,
                     c.drive_member,
                     c.driving_log_item_id,
                     i.start_place,
                     i.end_place,
                     lead(c.drive_tm, 1, c.end_tm) over(partition by c.driving_log_item_id order by c.drive_tm) - c.drive_tm DRIVE_TIME
                from ti_vms_drive_convert c, TI_VMS_DRIVING_LOG_ITEM i
               where c.driving_log_item_id = i.driving_log_item_id
                 and c.sync_export_report = 2
                 and NOT EXISTS
               (SELECT K.DRIVE_MEMBER
                        FROM TI_VMS_DRIVE_CONVERT_BAK K
                       WHERE K.DRIVING_LOG_ITEM_ID = c.DRIVING_LOG_ITEM_ID)
               order by c.drive_tm) t;

    -- ��ȡETLÿ����ȡ������
    FOR TEMP IN (select t.drive_member,
                        to_char(t.drive_tm, 'yyyy-mm-dd') day_of_month
                   from ti_vms_drive_convert t
                  where t.sync_export_report = 2
                    AND NOT EXISTS
                  (SELECT K.DRIVING_LOG_ITEM_ID
                           FROM TI_VMS_DRIVE_CONVERT_BAK K
                          WHERE K.DRIVING_LOG_ITEM_ID = T.DRIVING_LOG_ITEM_ID)
                  group by t.drive_member, to_char(t.drive_tm, 'yyyy-mm-dd')
                  order by to_char(t.drive_tm, 'yyyy-mm-dd')) LOOP

      -- ��ʼ������
      TEMP_DRIVER_MEMBER        := TEMP.DRIVE_MEMBER;
      TEMP_DRIVE_TM             := REPLACE(TEMP.DAY_OF_MONTH, '-', '');
      TEMP_JOIN_DEPARTMENT_CODE := '';
      TEMP_DAY_DRIVE_TIME       := 0;
      TEMP_DAY_ATTENDANCE_TIME  := 0;

      -- ��ȡԱ����������ݣ��������ʱ�����Ű�ʱ��
      FOR DRIVER_LOG_ROW IN (select *
                               from TI_DRIVER_LOG_TIME_TMP t
                              where t.drive_member = TEMP.DRIVE_MEMBER
                                and to_char(t.drive_tm, 'yyyymmdd') =
                                    TEMP_DRIVE_TM
                              order by t.drive_tm) LOOP

        -- ��ƴ�����㲻Ϊ��ʱ��ֻƴ��Ŀ������
        if TEMP_JOIN_DEPARTMENT_CODE is not null then
          if lengthb(TEMP_JOIN_DEPARTMENT_CODE) < 1900 then
            TEMP_JOIN_DEPARTMENT_CODE := TEMP_JOIN_DEPARTMENT_CODE || '-' ||
                                         DRIVER_LOG_ROW.END_PLACE;
          end if;
          TEMP_END_TIME := DRIVER_LOG_ROW.END_TM;
          -- ƴ�ӿ�ʼ���㡢��������
        else
          TEMP_JOIN_DEPARTMENT_CODE := DRIVER_LOG_ROW.START_PLACE || '-' ||
                                       DRIVER_LOG_ROW.END_PLACE;

          -- ��ȡ�ϰ�ʱ��
          TEMP_START_TIME := DRIVER_LOG_ROW.DRIVE_TM;
          -- ��ȡ�°�ʱ��
          TEMP_END_TIME := DRIVER_LOG_ROW.END_TM;
        end if;

        -- ������·(����ʱ��-��ʼʱ��) ��ӣ��õ�����ʱ��
        TEMP_DAY_DRIVE_TIME := TEMP_DAY_DRIVE_TIME +
                               DRIVER_LOG_ROW.DRIVE_TIME;

      END LOOP;

      -- �������ʱ��(������ʱ��-��ʼʱ��)
      select round(TEMP_END_TIME - TEMP_START_TIME, 2)
        into TEMP_DAY_ATTENDANCE_TIME
        from dual;

      -- �����ʻʱ��
      select round(TEMP_DAY_DRIVE_TIME, 2)
        into TEMP_DAY_DRIVE_TIME
        from dual;

      -- �����ݴ���ʱ��ɾ��������
      delete TI_DRIVER_LOG_TMP tem
       where tem.drive_member = TEMP_DRIVER_MEMBER
         and tem.day_of_month = TEMP_DRIVE_TM;

      -- ����
      INSERT INTO TI_DRIVER_LOG_TMP
        (ID,
         DRIVE_MEMBER,
         DAY_OF_MONTH,
         DAY_ATTENDANCE_TIME,
         DAY_DRIVE_TIME,
         DEPT_CODE)
      VALUES
        (SEQ_DRIVER_LOG.NEXTVAL,
         TEMP_DRIVER_MEMBER,
         TEMP_DRIVE_TM,
         TEMP_DAY_ATTENDANCE_TIME,
         TEMP_DAY_DRIVE_TIME,
         TEMP_JOIN_DEPARTMENT_CODE);

    END LOOP;

    update ti_vms_drive_convert t
       set t.sync_export_report = 1
     where t.sync_export_report = 2;

    COMMIT;

    P_REPORT_CAUSE_BY_DRIVING();

    handle_driver_task_report();

    --4.������¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_DRIVING_CONVERT_DATA',
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
      update ti_vms_drive_convert t
         set t.sync_export_report = 0
       where t.sync_export_report = 2;
      COMMIT;
      ROLLBACK;
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'HANDLE_DRIVING_CONVERT_DATA',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR' || TEMP_DRIVER_MEMBER,
                                   0,
                                   L_CALL_NO);
  END HANDLE_DRIVING_CONVERT_DATA;

  -- ����������Ű�
  PROCEDURE P_INSERT_report_for_temp(DAY1            IN VARCHAR2,
                                     EMPLOYEE_CODE   in VARCHAR2,
                                     DEPARTMENT_CODE IN VARCHAR2,
                                     CONFIGURE_CODE  IN VARCHAR2,
                                     MONTH           in VARCHAR2) IS
  begin
    IF DAY1 = '01' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY1, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '02' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY2, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '03' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY3, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '04' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY4, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '05' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY5, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '06' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY6, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '07' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY7, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '08' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY8, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '09' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY9, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '10' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY10, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '11' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY11, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '12' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY12, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '13' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY13, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '14' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY14, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '15' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY15, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '16' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY16, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '17' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY17, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '18' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY18, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '19' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY19, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '20' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY20, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '21' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY21, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '22' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY22, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '23' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY23, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '24' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY24, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '25' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY25, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '26' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY26, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '27' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY27, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '28' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY28, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '29' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY29, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

    IF DAY1 = '30' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY30, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;
    IF DAY1 = '31' THEN

      INSERT INTO REPORT_DRIVER_SCHEDULING
        (ID, DAY31, EMPLOYEE_CODE, DEPARTMENT_CODE, MONTH)
      VALUES
        (SEQ_REPORT_DRIVER_SCHEDULING1.NEXTVAL,
         CONFIGURE_CODE,
         EMPLOYEE_CODE,
         DEPARTMENT_CODE,
         MONTH);
    END IF;

  end P_INSERT_report_for_temp;

  --�޸ı�����Ű�
  PROCEDURE P_UPDATE_REPORT_FOR_TEMP(V_DAY1           IN VARCHAR2,
                                     V_EMPLOYEE_CODE  in VARCHAR2,
                                     V_DEPARMENT_CODE in VARCHAR2,
                                     V_CONFIGURE_CODE IN VARCHAR2,
                                     V_MONTH          IN VARCHAR2) IS

  begin
    IF V_DAY1 = '01' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY1            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;

    IF V_DAY1 = '02' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY2            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;

    IF V_DAY1 = '03' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY3            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '04' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY4            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '05' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY5            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;

    IF V_DAY1 = '06' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY6            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '07' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY7            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '08' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY8            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '09' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY9            = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '10' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY10           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '11' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY11           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '12' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY12           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '13' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY13           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '14' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY14           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '15' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY15           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '16' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY16           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '17' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY17           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '18' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY18           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '19' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY19           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '20' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY20           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '21' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY21           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '22' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY22           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '23' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY23           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '24' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY24           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '25' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY25           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '26' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY26           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '27' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY27           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '28' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY28           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '29' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY29           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '30' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY30           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;
    IF V_DAY1 = '31' THEN

      UPDATE REPORT_DRIVER_SCHEDULING T
         SET T.DAY31           = V_CONFIGURE_CODE,
             T.DEPARTMENT_CODE = V_DEPARMENT_CODE
       WHERE EMPLOYEE_CODE = V_EMPLOYEE_CODE
         AND T.MONTH = V_MONTH;

    END IF;

  end P_UPDATE_REPORT_FOR_TEMP;

  -- �����Ű����ݡ������Ǻ���
  procedure HANDLE_SCHEDULING_TASK_REPORT is
    --*************************************************************
    -- AUTHOR  : ����
    -- CREATED : 2015-09-09
    -- PURPOSE : �Ű����ݵ����������г���־������ʱ������ʻʱ�����Ű��Ǻ���;
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************
    temp_sch_dept_code   varchar2(20); -- �Ű��������
    temp_configure_code  varchar2(30); -- ������
    temp_conf_dept_code  varchar2(20); --��������
    temp_code            varchar2(20); -- ���Code
    temp_emp_code        varchar2(20); -- Ա������
    temp_year_month      varchar2(10); --�������(yyyy-mm)
    temp_day             varchar2(2); --������dd
    temp_day_of_month    varchar2(10); -- ���������(yyyymmdd)
    temp_scheduling_type number(2); --�Ű����� 0Ԥ�Űࡢ1ʵ���Ű�
    temp_exist_count     number(10); -- ���������Ƿ���� 1����


    temp_match_drive_department varchar2(2000); -- ƴ�ӶԱȵ��г���־��������
    temp_natural_days           number; -- ������Ȼ����

    temp_join_dept_code varchar2(2000); -- ƴ�ӵ��Ű���·��������

    temp_emp_name             varchar2(50); -- Ա������
    temp_department_name      varchar2(50); -- ��������
    temp_area_department_code varchar2(50); -- �������
    temp_area_department_name varchar2(50); -- ��������

    temp_attendance_time       number(10, 2); -- ����ʱ��
    temp_drive_time            number(10, 2); -- ��ʻʱ��(ʵ��)
    temp_scheduling_time       number(10, 2); -- ��¼������·��ʻʱ��
    temp_total_rest_count      number(10); -- ����Ϣ����
    temp_is_rest               varchar2(3); -- ��
    temp_sch_attendance_time    number(10, 2); --�Ű����ʱ��
    sch_attendance_days                 number(2);
    max_attendance_days                 number(2);


    --1.����ִ�����
    L_CALL_NO NUMBER;

  BEGIN

    --2.����ִ�����
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.��ʼ��¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_SCHEDULING_TASK_REPORT',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    -- �Ű�����Ϊ��ʵ���Ű�
    temp_sch_dept_code   := '';
    temp_conf_dept_code  := '';
    temp_code            := '';
    temp_emp_code        := '';
    temp_year_month      := '';
    temp_day_of_month    := '';
    temp_is_rest         := '��';
    temp_scheduling_type := 1;

    update SYNC_TASK_FOR_SCHEDULED_REPORT task set task.sync_status = 3;
    COMMIT;

    -- ��������������
    FOR row_data IN (select task.employee_code, task.year_month
                       from SYNC_TASK_FOR_SCHEDULED_REPORT task
                      where task.sync_status = 3
                        and length(task.year_month) = 7
                      group by task.employee_code, task.year_month
                      order by task.employee_code, task.year_month) LOOP

      temp_year_month := row_data.Year_Month;
      temp_emp_code   := row_data.employee_code;
      -- ��ʼ������ʱ��
      temp_attendance_time := 0;
      -- ��ʼ����ʻʱ��(ʵ��)
      temp_drive_time := 0;
      temp_sch_attendance_time := 0;
      sch_attendance_days := 0;
      max_attendance_days := 0;
      -- ͨ�� Ա�����š����¡��Ű����� ��ѯ�Ű�����
      FOR sch_row IN (select sch.department_code,
                             sch.employee_code,
                             sch.configure_code,
                             sch.year_month,
                             sch.day_of_month,
                             emp.emp_name,
                             dept.dept_name      dept_name,
                             area.dept_code      area_code,
                             area.dept_name      area_name
                        from tt_driver_scheduling sch,
                             tm_oss_employee      emp,
                             tm_department        dept,
                             tm_department        area
                       where sch.employee_code = emp.emp_code
                         and sch.department_code = dept.dept_code
                         and dept.area_code = area.dept_code
                         and sch.employee_code = temp_emp_code
                         and sch.year_month = temp_year_month
                         and sch.scheduling_type = temp_scheduling_type
                       order by sch.day_of_month) loop


          if temp_day_of_month + 1 = sch_row.day_of_month and sch_row.configure_code <> '��'
          then
            sch_attendance_days := sch_attendance_days + 1;
            if sch_attendance_days > max_attendance_days then
            max_attendance_days := sch_attendance_days;
            end if;
            else
              if  sch_row.configure_code <> '��' then 
                      sch_attendance_days := 1;
                      else
              sch_attendance_days := 0;
              end if;
          end if;


        -- ��ȡ��������
        temp_conf_dept_code := substr(sch_row.configure_code,
                                      0,
                                      instr(sch_row.configure_code, '-') - 1);
        -- ��ȡ������
        temp_configure_code := sch_row.configure_code;
        -- ��ȡ���CODE
        temp_code := substr(sch_row.configure_code,
                            instr(sch_row.configure_code, '-') + 1);
        -- ��ȡ�Ű������
        temp_sch_dept_code := sch_row.department_code;
        -- �洢������
        temp_day_of_month := sch_row.day_of_month;
        -- �洢�Ű���
        temp_day := substr(sch_row.day_of_month, 7);
        -- �洢Ա������
        temp_emp_name := sch_row.emp_name;
        -- �����������
        temp_department_name := sch_row.dept_name;
        -- �������
        temp_area_department_code := sch_row.area_code;
        -- �����������
        temp_area_department_name := sch_row.area_name;
        -- ��ʼ��ƴ������
        temp_join_dept_code := '';
        -- ��ʼ���Ű���·��ʱ��
        temp_scheduling_time := 0;
        -- ��ʼ���Ա�����Ϊ��
        temp_match_drive_department := '';

      if sch_row.configure_code <> '��' then
         select conf.attendance_duration + temp_sch_attendance_time
             into temp_sch_attendance_time
        from tt_driver_line_configure   conf
       where conf.code = temp_code
         and conf.month =temp_year_month
         and conf.department_code =temp_conf_dept_code
         and conf.valid_status = 1
         and conf.type = 0;
       end if;


        -- �鿴���������Ƿ����;
        SELECT COUNT(1)
          INTO temp_exist_count
          FROM REPORT_DRIVER_SCHEDULING T1
         WHERE T1.EMPLOYEE_CODE = temp_emp_code
           and t1.month = temp_year_month;

        -- �����������������޸�
        IF temp_exist_count = 0 THEN
          --INSERT
          P_INSERT_report_for_temp(temp_day,
                                   temp_emp_code,
                                   temp_sch_dept_code,
                                   temp_configure_code,
                                   temp_year_month);

        ELSE
          -- UPDATE
          P_UPDATE_REPORT_FOR_TEMP(temp_day,
                                   temp_emp_code,
                                   temp_sch_dept_code,
                                   temp_configure_code,
                                   temp_year_month);
        END IF;

      END LOOP;

      -- ��ȡ����������
      select to_char(last_day(to_date(temp_year_month, 'yyyy-mm')), 'dd')
        into temp_natural_days
        from dual;

      -- ��ѯ��Ϣ������
      select count(1)
        into temp_total_rest_count
        from tt_driver_scheduling sch
       where sch.year_month = temp_year_month
         and sch.employee_code = temp_emp_code
         and sch.scheduling_type = temp_scheduling_type
         and sch.configure_code = temp_is_rest;

      -- ��ȡ����ʱ��(��)
      select nvl(sum(t.day_attendance_time), 0)
        into temp_attendance_time
        from ti_driver_log_tmp t
       where t.drive_member = temp_emp_code
         and substr(t.day_of_month, 0, 6) =
             replace(temp_year_month, '-', '');

      -- ��ȡ�Ű��ʻʱ��(ʵ��)
      select nvl(sum(t.day_drive_time), 0)
        into temp_drive_time
        from ti_driver_log_tmp t
       where t.drive_member = temp_emp_code
         and substr(t.day_of_month, 0, 6) =
             replace(temp_year_month, '-', '');


      -- ���±�������
      UPDATE REPORT_DRIVER_SCHEDULING S
         SET S.EMPLOYEE_NAME       = temp_emp_name,
             S.DEPARTMENT_NAME     = temp_department_name,
             S.AREA_CODE           = temp_area_department_code,
             S.AREA_NAME           = temp_area_department_name,
             S.SCHEDULE_TYPE       = '����',
             S.TOTAL_REST_COUNT    = temp_total_rest_count,
             S.ATTENDANCE_DURATION = round(temp_attendance_time * 24 /
                                           temp_natural_days,
                                           2),
             S.DRIVE_TIME_MONTH_T  = round(temp_drive_time * 24 /
                                           temp_natural_days,
                                           2),
             S.AVERAGE_ATTENDANCE_TIME = round(temp_sch_attendance_time /
                                           temp_natural_days,
                                           2),
              S.CONTINUOUS_ATTENDANCE_DAYS = max_attendance_days
       where S.EMPLOYEE_CODE = temp_emp_code
         and S.MONTH = temp_year_month;
      COMMIT;
    END LOOP;

    DELETE SYNC_TASK_FOR_SCHEDULED_REPORT task where task.sync_status = 3;

    commit;
    --4.������¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_SCHEDULING_TASK_REPORT',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'END',
                                 0,
                                 L_CALL_NO);

    --5.�쳣��¼��־
  EXCEPTION
    WHEN OTHERS THEN
      -- �ع�ͬ��״̬
      update SYNC_TASK_FOR_SCHEDULED_REPORT task
         set task.sync_status = 0
       where task.sync_status = 3;
      COMMIT;

      --�ع�����
      ROLLBACK;
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'HANDLE_SCHEDULING_TASK_REPORT',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR' || temp_emp_code,
                                   0,
                                   L_CALL_NO);

  END HANDLE_SCHEDULING_TASK_REPORT;

  PROCEDURE HANDLE_DRIVER_TASK_REPORT IS
    --*************************************************************
    -- AUTHOR  : JHC
    -- CREATED : 2015-09-09
    -- PURPOSE : �Ű����ݵ����������г���־����ʱ������ʻʱ�����Ű��Ǻ���;
    --
    -- PARAMETER:
    -- NAME             TYPE            DESC
    --
    -- MODIFY HISTORY
    -- PERSON                    DATE                        COMMENTS
    -- -------------------------------------------------------------
    --
    --*************************************************************
    TEMP_SCH_DEPT_CODE   VARCHAR2(20); -- �Ű��������
    TEMP_CONFIGURE_CODE  VARCHAR2(30); -- ������
    TEMP_CONF_DEPT_CODE  VARCHAR2(20); --��������
    TEMP_CODE            VARCHAR2(20); -- ���CODE
    TEMP_EMP_CODE        VARCHAR2(20); -- Ա������
    TEMP_YEAR_MONTH      VARCHAR2(10); --�������(YYYY-MM)
    TEMP_DAY_OF_MONTH    VARCHAR2(10); -- ���������(YYYYMMDD)
    TEMP_SCHEDULING_TYPE NUMBER(2); --�Ű����� 0Ԥ�Űࡢ1ʵ���Ű�
    TEMP_IS_LEAVE        NUMBER; -- ����� 1��١�0�����
    TEMP_CONFIGURE_TYPE  NUMBER; -- ������� 1������ࡢ0�������

    TEMP_MATCH_DRIVE_DEPARTMENT VARCHAR2(2000); -- ƴ�ӶԱȵ��г���־��������
    TEMP_NATURAL_DAYS           NUMBER; -- ������Ȼ����

    TEMP_JOIN_DEPT_CODE VARCHAR2(2000); -- ƴ�ӵ��Ű���·��������

    TEMP_RATE_COUNT          NUMBER(10); -- �Ǻ�����
    TEMP_MATCH_RATE          NUMBER(10, 2); -- ������Ǻ���
    TEMP_IS_REST             VARCHAR2(3); -- ��
    TEMP_EXIST               NUMBER; -- �г���־������������
    TEMP_EXIST_OF_SCHEDULING NUMBER; -- �Ű������������
    TEMP_ATTENDANCE_TIME     NUMBER(10, 2); -- ����ʱ��
    TEMP_DRIVE_TIME          NUMBER(10, 2); -- ��ʻʱ��(ʵ��)

    --1.����ִ�����
    L_CALL_NO NUMBER;

  BEGIN

    --2.����ִ�����
    SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
    --3.��ʼ��¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_DRIVER_TASK_REPORT',
                                 SYSDATE,
                                 NULL,
                                 NULL,
                                 'START',
                                 0,
                                 L_CALL_NO);
    -- �Ű�����Ϊ��ʵ���Ű�
    TEMP_SCH_DEPT_CODE   := '';
    TEMP_CONF_DEPT_CODE  := '';
    TEMP_CODE            := '';
    TEMP_EMP_CODE        := '';
    TEMP_YEAR_MONTH      := '';
    TEMP_DAY_OF_MONTH    := '';
    TEMP_IS_REST         := '��';
    TEMP_SCHEDULING_TYPE := 1;

    -- ��������������
    FOR ROW_DATA IN (SELECT T.DRIVE_MEMBER,
                            TO_CHAR(T.DRIVE_TM, 'YYYY-MM') YEAR_MONTH
                       FROM TI_DRIVER_LOG_TIME_TMP T
                      GROUP BY T.DRIVE_MEMBER,
                               TO_CHAR(T.DRIVE_TM, 'YYYY-MM')) LOOP

      TEMP_YEAR_MONTH := ROW_DATA.YEAR_MONTH;
      TEMP_EMP_CODE   := ROW_DATA.DRIVE_MEMBER;
      -- ��ʼ���Ű��Ǻ���ֵ
      TEMP_RATE_COUNT := 0;
      TEMP_MATCH_RATE := 0;

      -- ��ȡ����ʱ��(��)
      SELECT NVL(SUM(T.DAY_ATTENDANCE_TIME), 0)
        INTO TEMP_ATTENDANCE_TIME
        FROM TI_DRIVER_LOG_TMP T
       WHERE T.DRIVE_MEMBER = TEMP_EMP_CODE
         AND SUBSTR(T.DAY_OF_MONTH, 0, 6) =
             REPLACE(TEMP_YEAR_MONTH, '-', '');

      -- ��ȡ�Ű��ʻʱ��(ʵ��)
      SELECT NVL(SUM(T.DAY_DRIVE_TIME), 0)
        INTO TEMP_DRIVE_TIME
        FROM TI_DRIVER_LOG_TMP T
       WHERE T.DRIVE_MEMBER = TEMP_EMP_CODE
         AND SUBSTR(T.DAY_OF_MONTH, 0, 6) =
             REPLACE(TEMP_YEAR_MONTH, '-', '');
      -- ��ȡ����������
      SELECT TO_CHAR(LAST_DAY(TO_DATE(TEMP_YEAR_MONTH, 'YYYY-MM')), 'DD')
        INTO TEMP_NATURAL_DAYS
        FROM DUAL;
   

      -- ���±�������
      UPDATE REPORT_DRIVER_SCHEDULING S
         SET S.ATTENDANCE_DURATION = ROUND(TEMP_ATTENDANCE_TIME * 24 /
                                           TEMP_NATURAL_DAYS,
                                           2),
             S.DRIVE_TIME_MONTH_T  = ROUND(TEMP_DRIVE_TIME * 24 /
                                           TEMP_NATURAL_DAYS,
                                           2)
       WHERE S.EMPLOYEE_CODE = TEMP_EMP_CODE
         AND S.MONTH = TEMP_YEAR_MONTH;
    END LOOP;

    COMMIT;
    --4.������¼��־
    PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                 'HANDLE_DRIVER_TASK_REPORT',
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
      PKG_OSS_COMM.STP_RUNNING_LOG('PKG_HANDLE_COMPARE_REPORT',
                                   'HANDLE_DRIVER_TASK_REPORT',
                                   SYSDATE,
                                   SQLCODE,
                                   SQLERRM,
                                   'ERROR',
                                   0,
                                   L_CALL_NO);

  END HANDLE_DRIVER_TASK_REPORT;

end PKG_HANDLE_COMPARE_REPORT;
/