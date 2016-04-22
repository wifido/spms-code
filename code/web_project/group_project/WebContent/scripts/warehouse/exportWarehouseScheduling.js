//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var exportScheduling = function() {
	var departmentCodes = Ext.getCmp("query.branchCode").getValue();
	if (Ext.isEmpty(departmentCodes)) {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}
	if (Ext.getCmp('query.monthId').getValue() == "") {
		Ext.Msg.alert("提示", "请先选择月份！");
		return;
	}

	var monthId = Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Y-m');
	monthId = monthId.replace("-", "");
	var employeeCode = Ext.getCmp('query.empCode').getValue();

	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();

	Ext.Ajax.request({
		url: 'export_warehouseScheduling.action',
		method: 'POST',
		timeout: 500000,
		params: {
			start: 0,
			limit: 60000,
			in_departmentCode: departmentCodes.split(',')[0].split('/')[0],
			MONTH_ID: monthId,
			EMP_CODE: employeeCode,
			EMP_NAME: Ext.getCmp(CONDITION_EMPLOYEE_NAME).getValue(),
			WORK_TYPE: Ext.getCmp(CONDITION_EMPLOYEE_TYPE).getValue(),
			EMP_DUTY_NAME: Ext.getCmp(CONDITION_POST_NAME).getValue()
		},
		success: function(response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert("提示", "导出成功!");
				var url = result.dataMap.downloadPath;
				window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
			} else {
				Ext.Msg.alert("提示", "导出失败!" + result.error);
			}
			myMask.hide();
		},
		failure: function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			Ext.Msg.alert('提示', "导出失败！" + result.error);
			myMask.hide();
		}
	});
};

var btnExportScheduling = new Ext.Button({
	text: "导出",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		exportScheduling();
	}
});

var exportNoSchedulingEmployee = function() {
	var departmentCodes = Ext.getCmp("query.branchCode").getValue();
	if (Ext.isEmpty(departmentCodes)) {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}
	if (Ext.getCmp('query.monthId').getValue() == "") {
		Ext.Msg.alert("提示", "请先选择月份！");
		return;
	}

	var monthId = Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Y-m');

	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();

	Ext.Ajax.request({
		url: 'exportNoSchedulingEmployee_warehouseScheduling.action',
		method: 'POST',
		timeout: 500000,
		params: {
			start: 0,
			limit: 60000,
			departmentCode: departmentCodes.split(',')[0].split('/')[0],
			monthId: monthId,
			month: monthId.replace("-", "")
		},
		success: function(response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert("提示", "导出成功!");
				var url = result.dataMap.downloadPath;
				window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
			} else {
				Ext.Msg.alert("提示", "导出失败!" + result.error);
			}
			myMask.hide();
		},
		failure: function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			Ext.Msg.alert('提示', "导出失败！" + result.error);
			myMask.hide();
		}
	});
};

var btnExportNoSchedulingEmployee = new Ext.Button({
	text: "导出未排班人员",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		exportNoSchedulingEmployee();
	}
});