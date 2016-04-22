alter table TRANSACTION_LOG add(
   FILEPATH             VARCHAR2(1024),                                   
  ISZIP                VARCHAR2(10),                                     
  MD5                  VARCHAR2(200),                                    
  REPROCESS            VARCHAR2(4)  
);
comment on column TRANSACTION_LOG.FILEPATH                               
  is '�ļ�·��';                                                         
comment on column TRANSACTION_LOG.ISZIP                                  
  is '�Ƿ�ѹ��';                                                         
comment on column TRANSACTION_LOG.REPROCESS                              
  is '�Ƿ����´�������';                                                 
-- Create/Recreate indexes                                               
create index IDX_TRANSACTION_LOG on TRANSACTION_LOG (FILENAME,TIMESTAMP, DATATYPE); 