CREATE OR REPLACE PROCEDURE P_HAND_DRIVE_CONVERT_AND_SCH AS

BEGIN
  -- �����Ű����ݴ��������ݶԱ�����
  PKG_HANDLE_COMPARE_REPORT.p_report_cause_by_schedule;
  
  -- �����г����ݴ��������ݶԱ�����
  PKG_HANDLE_COMPARE_REPORT.P_REPORT_CAUSE_BY_DRIVING;
  
END P_HAND_DRIVE_CONVERT_AND_SCH;
/
