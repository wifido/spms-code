CREATE OR REPLACE PACKAGE PKG_HANDLE_COMPARE_REPORT IS

  -- AUTHOR  : 053452
  -- CREATED : 2015-09-10
  -- PURPOSE : 处理排班和行车异常数据对比报表的过程

  -- 处理排班数据触发的数据对比任务
  PROCEDURE p_report_cause_by_schedule;

  -- 处理行车数据触发的数据对比任务
  PROCEDURE P_REPORT_CAUSE_BY_DRIVING;

  -- 处理行车日志数据
  PROCEDURE HANDLE_DRIVING_CONVERT_DATA;

  -- 新增报表的排班
  PROCEDURE P_INSERT_report_for_temp(DAY1            IN VARCHAR2,
                                     EMPLOYEE_CODE   in VARCHAR2,
                                     DEPARTMENT_CODE IN VARCHAR2,
                                     CONFIGURE_CODE  IN VARCHAR2,
                                     MONTH           in VARCHAR2);

  -- 修改报表的排班
  PROCEDURE P_UPDATE_REPORT_FOR_TEMP(V_DAY1           IN VARCHAR2,
                                     V_EMPLOYEE_CODE  in VARCHAR2,
                                     V_DEPARMENT_CODE in VARCHAR2,
                                     V_CONFIGURE_CODE IN VARCHAR2,
                                     V_MONTH          IN VARCHAR2);

  -- 处理排班数据、计算吻合率
  procedure HANDLE_SCHEDULING_TASK_REPORT;

END PKG_HANDLE_COMPARE_REPORT;
/
