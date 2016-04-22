CREATE OR REPLACE VIEW V_DRIVER_CONVERT AS
SELECT C."ID",C."VEHICLE_CODE",C."DRIVE_MEMBER",C."DRIVE_MILES",C."DRIVE_TM",C."DRIVE_SPAN",C."DRIVING_LOG_ITEM_ID",C."START_MILES",C."END_MILES",C."START_TM",C."END_TM",C."CREATED_EMP_CODE",C."CREATED_TM",C."MODIFIED_EMP_CODE",C."MODIFIED_TM",C."SYNC_FLAG",C."SYNC_TM",C."SAP_SYNC_FLAG"
  FROM TI_VMS_DRIVE_CONVERT C, TM_OSS_EMPLOYEE E
 WHERE C.DRIVE_MEMBER = E.EMP_CODE
   AND TO_CHAR(C.SYNC_TM,'YYYYMMDD')>= '20150901'
   AND E.WORK_TYPE <> 6;