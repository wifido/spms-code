update TL_SPMS_SYS_CONFIG
   set KEY_VALUE   = '地服操作员,地服组长,国际件理货员,数据信息员,外场特种车司机,运作文员,综合文员,仓管员,仓管组长,代理点部主管,代理分部经理,点部主管,分部经理,分部副经理,分部经理助理,运作管理主管,代理运作管理主管,质量管理员,叉车司机,理货员,理货组长,包裹处理员,运作组长,运力操作组长,关务理货员,运作员,补码员,补码组长,安检员,运力操作员,集配站经理,集配站主管,分拣员,仓库管理员,速配站主管,速配站副经理,速配站经理,仓储管理副高级经理,仓储管理经理,仓储管理副经理,仓储管理主管,订单管理员,中转场副高级经理,中转场副经理,中转场高级主管,中转场经理,中转场主管,集散中心经理,集散中心副经理,集散中心主管,现场控制助理,运作管理经理,运作管理副经理,营业部负责人,速配营业部负责人,运营主管,操作员',
       MODIFIED_TM = sysdate
 where KEY_NAME = 'SAP_PRO_WAREHOUSE_POST';
commit;