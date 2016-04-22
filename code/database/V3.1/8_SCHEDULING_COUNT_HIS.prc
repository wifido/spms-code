create or replace procedure SCHEDULING_COUNT_HIS(COUNT_YM IN VARCHAR) is

  SHEDULE_NUM        NUMBER;
  PROCESS_NUM        NUMBER;
  SHEDULE_TIME_COUNT NUMBER;
  LENGTH_TIME_OF_DAY NUMBER;
  REST_DAYS          NUMBER;
  --1.����ִ�����
  L_CALL_NO NUMBER;
BEGIN

  --2.����ִ�����
  SELECT SEQ_OSS_TL.NEXTVAL INTO L_CALL_NO FROM DUAL;
  --3.��ʼ��¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SCHEDULING_COUNT_HIS',
                               SYSDATE,
                               NULL,
                               NULL,
                               'START',
                               0,
                               L_CALL_NO);

  for schedule_row in (select distinct (t.month_id)
                         from operation_count_sheduling t
                        where t.month_id between COUNT_YM and
                              to_char(sysdate, 'yyyy-mm')) loop
  
    -- ��ѯ����������������Ա����
    FOR EMP_NUM_ROW IN (SELECT TD.DEPT_ID,
                               TD.AREA_CODE,
                               TD.DEPT_CODE,
                               E.EMP_CODE,
                               E.EMP_NAME,
                               E.PERSK_TXT,
                               E.WORK_TYPE,
                               E.SF_DATE,
                               CASE
                                 WHEN E.DIMISSION_DT IS NULL THEN
                                  1
                                 WHEN E.DIMISSION_DT > SYSDATE THEN
                                  1
                                 WHEN E.DIMISSION_DT <= SYSDATE THEN
                                  0
                               END EMP_STATUS
                          FROM TM_DEPARTMENT TD, TM_OSS_EMPLOYEE E
                         WHERE E.DEPT_ID = TD.DEPT_ID
                           AND E.EMP_POST_TYPE = '1'
                           AND E.DEPT_ID IN (SELECT DEPT_ID FROM OP_DEPT)) LOOP
    
      SHEDULE_NUM        := 0;
      PROCESS_NUM        := 0;
      SHEDULE_TIME_COUNT := 0;
      LENGTH_TIME_OF_DAY := 0;
      REST_DAYS          := 0;
      --  ��ѯ�����
      SELECT COUNT(*) CLASS_TOTAL
        INTO SHEDULE_NUM
        FROM (SELECT TD.EMP_CODE, TD.SHEDULE_CODE, TD.DEPT_ID, TM.YM
                FROM TT_PB_SHEDULE_BY_DAY TD, TT_PB_SHEDULE_BY_MONTH TM
               WHERE TD.DEPT_ID = TM.DEPT_ID
                 AND TD.SHEDULE_MON_ID = TM.ID
                 AND TD.SHEDULE_CODE NOT IN ('��', 'SW', 'OFF', '��')
                 AND TM.YM = schedule_row.month_id
                 AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
                 AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE
               GROUP BY TD.EMP_CODE, TD.SHEDULE_CODE, TD.DEPT_ID, TM.YM) MONTH_TOTAL;
    
      --  ��ѯ������
      SELECT COUNT(*) AS CLASS_TOTAL
        INTO PROCESS_NUM
        FROM (SELECT TD.EMP_CODE
                FROM TT_PB_PROCESS_BY_DAY TD, TT_PB_PROCESS_BY_MONTH TM
               WHERE TD.DEPT_ID = TM.DEPT_ID
                 AND TD.PROCESS_MON_ID = TM.ID
                 AND TD.PROCESS_CODE NOT IN ('��', '��')
                 AND TM.YM = schedule_row.month_id
                 AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
                 AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE
               GROUP BY TD.EMP_CODE, TD.PROCESS_CODE, TD.DEPT_ID, TM.YM);
    
      --  ��ѯԱ���Ű�ʱ��
      FOR SHEDULE_CODE_ROW IN (SELECT TD.EMP_CODE,
                                      TD.SHEDULE_CODE,
                                      COUNT(SHEDULE_CODE) SHEDULE_CODE_NUM,
                                      YM,
                                      TD.DEPT_ID
                                 FROM TT_PB_SHEDULE_BY_DAY   TD,
                                      TT_PB_SHEDULE_BY_MONTH TM
                                WHERE TD.DEPT_ID = TM.DEPT_ID
                                  AND TD.SHEDULE_MON_ID = TM.ID
                                  AND TD.SHEDULE_CODE NOT IN
                                      ('��', 'SW', 'OFF', '��')
                                  AND TM.YM = schedule_row.month_id
                                  AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
                                  AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE
                                GROUP BY TD.EMP_CODE,
                                         TD.SHEDULE_CODE,
                                         TD.DEPT_ID,
                                         TM.YM) LOOP
        SELECT (COUNT_TIME_DISTANCE(TB.START1_TIME, TB.END1_TIME) +
               COUNT_TIME_DISTANCE(TB.START2_TIME, TB.END2_TIME) +
               COUNT_TIME_DISTANCE(TB.START3_TIME, TB.END3_TIME)) *
               SHEDULE_CODE_ROW.SHEDULE_CODE_NUM + SHEDULE_TIME_COUNT
          INTO SHEDULE_TIME_COUNT
          FROM TM_PB_SCHEDULE_BASE_INFO TB
         WHERE TB.YM = SHEDULE_CODE_ROW.YM
           AND TB.SCHEDULE_CODE = SHEDULE_CODE_ROW.SHEDULE_CODE
           AND TB.DEPT_ID = SHEDULE_CODE_ROW.DEPT_ID
           AND TB.CLASS_TYPE = '1';
      END LOOP;
    
      -- ��ѯԱ���վ�ʱ��
      SELECT COUNT(*)
        INTO LENGTH_TIME_OF_DAY
        FROM TT_PB_SHEDULE_BY_DAY TD, TT_PB_SHEDULE_BY_MONTH TM
       WHERE TD.DEPT_ID = TM.DEPT_ID
         AND TD.SHEDULE_MON_ID = TM.ID
         AND TD.SHEDULE_CODE NOT IN ('��', 'SW', 'OFF', '��')
         AND TM.YM = schedule_row.month_id
         AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
         AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE;
    
      IF LENGTH_TIME_OF_DAY != 0 THEN
        LENGTH_TIME_OF_DAY := ROUND(SHEDULE_TIME_COUNT / LENGTH_TIME_OF_DAY,
                                    2);
      END IF;
      -- ��ѯԱ����Ϣ����
      SELECT COUNT(*)
        INTO REST_DAYS
        FROM TT_PB_SHEDULE_BY_DAY TD, TT_PB_SHEDULE_BY_MONTH TM
       WHERE TD.DEPT_ID = TM.DEPT_ID
         AND TD.SHEDULE_MON_ID = TM.ID
         AND TD.SHEDULE_CODE in('��','SW','OFF')
         AND TM.YM = schedule_row.month_id
         AND TM.DEPT_ID = EMP_NUM_ROW.DEPT_ID
         AND TM.EMP_CODE = EMP_NUM_ROW.EMP_CODE;
    
      UPDATE OPERATION_COUNT_SHEDULING T
         SET SHEDULE_NUM        = SHEDULE_NUM,
             PROCESS_NUM        = PROCESS_NUM,
             REST_DAYS          = REST_DAYS,
             LENGTH_TIME_OF_DAY = LENGTH_TIME_OF_DAY
       where T.MONTH_ID = schedule_row.month_id
         and T.EMP_CODE = EMP_NUM_ROW.EMP_CODE
         and T.Dept_Id = EMP_NUM_ROW.DEPT_ID;
      commit;
    END LOOP;
  end loop;

  --4.������¼��־
  PKG_OSS_COMM.STP_RUNNING_LOG('',
                               'SCHEDULING_COUNT_HIS',
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
                                 'SCHEDULING_COUNT_HIS',
                                 SYSDATE,
                                 SQLCODE,
                                 SQLERRM,
                                 'ERROR',
                                 0,
                                 L_CALL_NO);
  
end SCHEDULING_COUNT_HIS;
/
