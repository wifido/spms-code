CREATE OR REPLACE PACKAGE PKG_HANDLE_COMPARE_REPORT IS

  -- AUTHOR  : 053452
  -- CREATED : 2015-09-10
  -- PURPOSE : �����Ű���г��쳣���ݶԱȱ���Ĺ���

  -- �����Ű����ݴ��������ݶԱ�����
  PROCEDURE p_report_cause_by_schedule;

  -- �����г����ݴ��������ݶԱ�����
  PROCEDURE P_REPORT_CAUSE_BY_DRIVING;

  -- �����г���־����
  PROCEDURE HANDLE_DRIVING_CONVERT_DATA;

  -- ����������Ű�
  PROCEDURE P_INSERT_report_for_temp(DAY1            IN VARCHAR2,
                                     EMPLOYEE_CODE   in VARCHAR2,
                                     DEPARTMENT_CODE IN VARCHAR2,
                                     CONFIGURE_CODE  IN VARCHAR2,
                                     MONTH           in VARCHAR2);

  -- �޸ı�����Ű�
  PROCEDURE P_UPDATE_REPORT_FOR_TEMP(V_DAY1           IN VARCHAR2,
                                     V_EMPLOYEE_CODE  in VARCHAR2,
                                     V_DEPARMENT_CODE in VARCHAR2,
                                     V_CONFIGURE_CODE IN VARCHAR2,
                                     V_MONTH          IN VARCHAR2);

  -- �����Ű����ݡ������Ǻ���
  procedure HANDLE_SCHEDULING_TASK_REPORT;

END PKG_HANDLE_COMPARE_REPORT;
/
