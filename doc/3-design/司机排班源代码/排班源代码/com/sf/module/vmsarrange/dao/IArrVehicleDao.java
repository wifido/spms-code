/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-30     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ArrVehicle;

/**
 * 
 * 车辆视图dao接口
 * @author 方芳 (350614) 2014-5-30
 */
public interface IArrVehicleDao extends IEntityDao<ArrVehicle> {
	/**
	 * 查找车型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> listModelBase(String modelBase);
	/**
	 * 根据车牌号查找车辆
	 * @param vehicleCode
	 * @return
	 */
	public ArrVehicle listByCode(String vehicleCode);
	/**
	 * 分页查找车辆-无权限控制
	 * @param vehicleCode
	 * @return
	 */
	public IPage<ArrVehicle> listVehiclePage(String vehicleCode,String modelBase,int pageSize,int pageIndex);
	/**
	 * 查找所有正常停用车辆
	 * @return
	 */
	public List<ArrVehicle> listAllVehicle();
}
