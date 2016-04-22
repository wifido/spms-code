alter table TRANSACTION_LOG add(
   FILEPATH             VARCHAR2(1024),                                   
  ISZIP                VARCHAR2(10),                                     
  MD5                  VARCHAR2(200),                                    
  REPROCESS            VARCHAR2(4)  
);
comment on column TRANSACTION_LOG.FILEPATH                               
  is '文件路径';                                                         
comment on column TRANSACTION_LOG.ISZIP                                  
  is '是否压缩';                                                         
comment on column TRANSACTION_LOG.REPROCESS                              
  is '是否重新处理请求';                                                 
-- Create/Recreate indexes                                               
create index IDX_TRANSACTION_LOG on TRANSACTION_LOG (FILENAME,TIMESTAMP, DATATYPE); 