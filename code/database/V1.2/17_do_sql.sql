

--�������ݣ��ýű���ִ�к����������ĵȴ�
BEGIN
       FOR JOURNAL IN (SELECT DISTINCT A.JOURNAL_ID FROM TI_OSS_HR_EMP_INFO_ALTER A WHERE A.POSITION_ATTR = 'һ��' ORDER BY A.JOURNAL_ID) 
       LOOP
           PKG_OSS_INTERFACE_PROCESS.STP_OSS_HR_ALL_OR_UPDATE('EMP_ONE', JOURNAL.JOURNAL_ID);
       END LOOP;           
END;
/