package com.sf.module.driver.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.common.dao.ScheduleBaseDao;
import com.sf.module.driver.domain.Vehicle;

@Repository
public class DriverVehicleDao extends ScheduleBaseDao<Vehicle> {
	@Transactional(readOnly = true)
	public Vehicle queryVehicleByVehicleCode(String vehicleCode) {
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleCode(vehicleCode);
		List<Vehicle> vehicles = findBy(vehicle);
		if (vehicles.isEmpty()) {
			return new Vehicle();
		}
		return vehicles.get(0);
	}
}
