package com.sf.module.common.util;

import java.util.HashMap;

import com.sf.module.common.domain.OssDepartment;
import com.sf.module.organization.domain.Department;

public class DepartmentHelper {
	
	private static final int HEADQUARTERS = 0;
	
	private static final int MANAGEMENT_THIS_PART = 1;
	
	private static final int AREA = 2;
    private DepartmentHelper() {
    }

    public static boolean isHeadQuarter(Department department) {
        return department.getDeptCode().equals("001");
    }

    public static boolean isAreaDepartment(Department department) {
        return department.getHqDeptCode() != null && department.getHqDeptCode().equals(department.getDeptCode())
                && department.getDeptName().contains("经营本部");
    }

    public static boolean isArea(Department department) {
        return department.getAreaDeptCode() != null && department.getAreaDeptCode().equals(department.getDeptCode())
                && department.getDeptName().contains("区部");
    }

    public static boolean isTransfer(Department department) {
        return (department.getDeptName().contains("中转场") || department.getDeptName().contains("航空组"));
    }

    public static boolean isOffice(Department department) {
        return (department.getDeptName().contains("分部") || department.getDeptName().contains("点部"));
    }
    
    public static void isCheckOfNetwork(OssDepartment department, HashMap<String, String> queryParameter) {
         if(isCheckOfNetwork(department.getTypeLevel()))
        	 queryParameter.put("isCheckOfNetwork","true");
    }
    
    private static boolean isCheckOfNetwork(int typeLevel) {
		return typeLevel == HEADQUARTERS || typeLevel == MANAGEMENT_THIS_PART || typeLevel == AREA;
	}
}
