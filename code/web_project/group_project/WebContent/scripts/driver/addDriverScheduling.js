//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var INDEX_CODE = 'CODE';
var INDEX_VALID_STATUS = 'VALID_STATUS';
var INDEX_ID = 'ID';
var INDEX_MONTH = 'MONTH';
var INDEX_START_TIME = 'START_TIME';
var INDEX_END_TIME = 'END_TIME';
var INDEX_SOURCE_CODE = 'SOURCE_CODE';
var INDEX_SOURCE_NAME = 'SOURCE_NAME';
var INDEX_DESTINATION_CODE = 'DESTINATION_CODE';
var INDEX_DESTINATION_NAME = 'DESTINATION_NAME';
var INDEX_DEPT_CODE = 'DEPT_CODE';
var INDEX_DEPT_ID = 'DEPT_ID';
var INDEX_AREA_CODE = 'AREA_CODE';

var COL_CODE = '班次代码';
var COL_MONTH = '月份';
var COL_VALID_STATUS = '有效性';
var COL_ID = '编号';
var COL_START_TIME = '出车时间';
var COL_END_TIME = '收车时间';
var COL_SOURCE_CODE = '始发网点';
var COL_DESTINATION_CODE = '目的网点';
var COL_DEPT_CODE = '网点代码';
var COL_DEPT_ID = '网点编号';
var COL_AREA_CODE = '区域代码';
var MANEUVER = "机动";

var area_department_id = '';

var searchBtn = new Ext.Button({
	width : 60,
	cls : "x-btn-normal",
	pressed : true,
	text : '查询',
	handler : function() {
		searchEmployee();
	}
});

function searchEmployee() {
	var departmentCode = Ext.getCmp("query.emp.departmentCode").getValue();
	var employeeCode = Ext.getCmp("query.emp.employeeCode").getValue();
	var employeeName = Ext.getCmp("query.emp.employeeName").getValue();
	var yearWeek = Ext.get("weekOfYear").getValue();

	emp_store.setBaseParam("departmentCode", departmentCode.split('/')[0]);
	emp_store.setBaseParam("employeeCode", employeeCode);
	emp_store.setBaseParam("employeeName", employeeName);
	emp_store.setBaseParam("year_week", yearWeek);
	emp_store.load({
		params : {
			start : 0,
			limit : 15
		}
	});
}

var searchEmployeeConditionPanel = new Ext.Panel({
	layout : 'column',
	frame : true,
	heigth : 50,
	items : [{
		labelWidth : 60,
		xtype : 'panel',
		layout : 'form',
		columnWidth : .4,
		items : [{
			width : 80,
			xtype : 'textfield',
			id : 'query.emp.departmentCode',
			name : 'departmentCode',
			fieldLabel : "网点代码",
			disabled : true,
			readOnly : true
		},{
			width : 80,
			xtype : 'textfield',
			id : 'query.emp.employeeName',
			fieldLabel : "员工姓名"
		}]
	},{
		xtype : 'panel',
		labelWidth : 60,
		layout : 'form',
		columnWidth : .4,
		items : [{
			width : 80,
			xtype : 'textfield',
			id : 'query.emp.employeeCode',
			name : 'employeeCode',
			triggerAction : "all",
			fieldLabel : '员工工号'
		}]
	},{
		xtype : 'panel',
		layout : 'form',
		columnWidth : .2,
		items : [searchBtn]
	}]
});

var emp_sm = new Ext.grid.CheckboxSelectionModel({});
var emp_store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : 'queryNoSchedulingEmployees_scheduling.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'resultMap.root',
		totalProperty : 'resultMap.totalSize'
	}, new Ext.data.Record.create([{
		name : 'DEPT_CODE',
		mapping : 'DEPT_CODE',
		type : 'string'
	},{
		name : 'AREA_CODE',
		mapping : 'AREA_CODE',
		type : 'string'
	},{
		name : 'EMP_CODE',
		mapping : 'EMP_CODE',
		type : 'string'
	},{
		name : 'EMP_NAME',
		mapping : 'EMP_NAME',
		type : 'string'
	},{
		name : 'WORK_TYPE_NAME',
		mapping : 'WORK_TYPE_NAME',
		type : 'string'
	},{
		name : 'DEPTTYPE',
		mapping : 'DEPTTYPE',
		type : 'string'
	}]))
});

var emp_grid = new Ext.Panel({
	frame : true,
	items : [{
		xtype : 'grid',
		id : 'emp_grid',
		layout : 'form',
		store : emp_store,
		loadMask : true,
		sm : emp_sm,
		height : 320,
		tbar : new Ext.PagingToolbar({
			pageSize : 15,
			store : emp_store,
			displayInfo : true,
			displayMsg : "当前显示 {0} - {1} 总记录数目 {2}",
			emptyMsg : "未检索到数据"
		}),
		columns : [emp_sm,{
			width : 60,
			header : '区部代码',
			dataIndex : 'AREA_CODE'
		},{
			width : 60,
			header : '网点代码',
			dataIndex : 'DEPT_CODE'
		},{
			width : 60,
			header : '工号',
			dataIndex : 'EMP_CODE'
		},{
			width : 60,
			header : '姓名',
			dataIndex : 'EMP_NAME'
		},{
			width : 60,
			header : '人员类型',
			dataIndex : 'WORK_TYPE_NAME'
		}]
	}]
});

function formatDepartName(code, name) {
	return code + "/" + name;
}

function formatConfigureCode(code, departmentName, sourceCode) {
	if(sourceCode)
		return departmentName + "-" + code;
	return MANEUVER + departmentName + "-" + code;
}

function formattedTime(value) {
	var left = value.substr(0, 2);
	var right = value.substr(2);
	return left + ":" + right;
}

var searchLineConfigureBtn = new Ext.Button({
	style : 'margin-left:10px;',
	width : 60,
	cls : "x-btn-normal",
	pressed : true,
	text : '查询',
	handler : function() {
		searchLineConfigure();
	}
});

function searchLineConfigure() {
	var selectedDay = Ext.get("selectedDay").getValue();
	var yearMonth = moment(selectedDay).format('YYYY') + '-' + moment(selectedDay).format('MM');
	var code = Ext.getCmp("query.lineConfigure.code").getValue();
	store_lineConfigure.setBaseParam("DEPT_ID", area_department_id);
	store_lineConfigure.setBaseParam("MONTH", yearMonth);
	store_lineConfigure.setBaseParam("CODE", code);
	store_lineConfigure.setBaseParam('VALID_STATUS', 1);

	store_lineConfigure.removeAll();
	store_lineConfigure.load({
		add : true,
		params : {
			start : 0,
			limit : 10
		}
	});
}

var searchLineConfigurePanel = new Ext.Panel({
	layout : 'column',
	frame : true,
	heigth : 50,
	items : [{
		labelWidth : 60,
		xtype : 'panel',
		layout : 'form',
		columnWidth : .4,
		items : [{
			style : 'margin-left:-10px;',
			xtype : 'textfield',
			id : 'query.lineConfigure.department',
			name : 'departmentCode',
			disabled : true,
			readOnly : true,
			fieldLabel : "网点代码"
		}]
	},{
		xtype : 'panel',
		labelWidth : 60,
		layout : 'form',
		columnWidth : .4,
		items : [{
			style : 'margin-left:-10px;',
			xtype : 'textfield',
			id : 'query.lineConfigure.code',
			name : 'lineConfigureCode',
			triggerAction : "all",
			fieldLabel : '班次代码'
		}]
	},{
		xtype : 'panel',
		layout : 'form',
		columnWidth : .2,
		items : [searchLineConfigureBtn]
	}]
});

var sm_lineConfigure = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true
});

var cm_lineConfigure = new Ext.grid.ColumnModel({
	columns : [new Ext.grid.RowNumberer(),sm_lineConfigure,{
		header : COL_CODE,
		sortable : true,
		dataIndex : INDEX_CODE,
		width : 150,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			if ('休' == value) {
				return value;
			}
			return formatConfigureCode(value, record.get(INDEX_DEPT_CODE), record.get(INDEX_SOURCE_CODE));
		}
	},{
		header : COL_START_TIME,
		sortable : true,
		dataIndex : INDEX_START_TIME,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			if ('休' == record.get(INDEX_CODE)) {
				return value;
			}
			return formattedTime(value);
		}
	},{
		header : COL_END_TIME,
		sortable : true,
		dataIndex : INDEX_END_TIME,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			if ('休' == record.get(INDEX_CODE)) {
				return value;
			}
			return formattedTime(value);
		}
	},{
		header : COL_SOURCE_CODE,
		sortable : true,
		dataIndex : INDEX_SOURCE_CODE

	},{
		header : COL_DESTINATION_CODE,
		sortable : true,
		dataIndex : INDEX_DESTINATION_CODE
	}]
});

var record_lineConfigure = Ext.data.Record.create([{
	name : INDEX_CODE,
	mapping : INDEX_CODE,
	type : 'string'
},{
	name : INDEX_VALID_STATUS,
	mapping : INDEX_VALID_STATUS,
	type : 'int'
},{
	name : INDEX_DEPT_CODE,
	mapping : INDEX_DEPT_CODE,
	type : 'string'
},{
	name : INDEX_START_TIME,
	mapping : INDEX_START_TIME,
	type : 'string'
},{
	name : INDEX_END_TIME,
	mapping : INDEX_END_TIME,
	type : 'string'
},{
	name : INDEX_SOURCE_CODE,
	mapping : INDEX_SOURCE_CODE,
	type : 'string'
},{
	name : INDEX_SOURCE_NAME,
	mapping : INDEX_SOURCE_NAME,
	type : 'string'
},{
	name : INDEX_DESTINATION_CODE,
	mapping : INDEX_DESTINATION_CODE,
	type : 'string'
},{
	name : INDEX_DESTINATION_NAME,
	mapping : INDEX_DESTINATION_NAME,
	type : 'string'
}]);

var store_lineConfigure = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : 'query_lineConfigure.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'resultMap.root',
		totalProperty : 'resultMap.totalSize'
	}, record_lineConfigure),
	listeners : {
		load : function(obj, record, options) {

			if (options.params.start == 0) {
				var restRecord = new record_lineConfigure({
					"CODE" : "休"
				});
				store_lineConfigure.add(restRecord);
			}
		}
	}
});

var pageBar_lineConfigure = new Ext.PagingToolbar({
	store : store_lineConfigure,
	displayInfo : true,
	displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize : 10,
	emptyMsg : '未检索到数据'
});

var grid_lineConfigure = new Ext.grid.GridPanel({
	height : 160,
	id : 'lineConfigure_grid',
	region : 'center',
	cm : cm_lineConfigure,
	sm : sm_lineConfigure,
	store : store_lineConfigure,
	autoScroll : true,
	loadMask : true,
	tbar : pageBar_lineConfigure,
	viewConfig : {
		forceFit : true
	}
});

var add_scheduling_datePicker = new Ext.ux.DatePicker({
	id : 'add_datepicker_update',
	style : 'margin-left:17px;',
	format : 'Ymd',
	showToday : false,
	listeners : {
		render : function() {

		}
	}
});

var westPanel = new Ext.Panel({
	frame : true,
	region : 'west',
	title : '选择未排班人员',
	width : 410,
	items : [searchEmployeeConditionPanel,emp_grid]
});

var eastPanel = new Ext.Panel({
	region : 'center',
	frame : true,
	title : '选择排班日期及配班代码',
	layout : 'border',
	width : 500,
	items : [{
		region : 'north',
		height : 200,
		layout : 'form',
		xtype : 'panel',
		items : [add_scheduling_datePicker]
	},{
		region : 'center',
		layout : 'form',
		xtype : 'panel',
		items : [searchLineConfigurePanel,grid_lineConfigure]
	}]
});

var addWindow = new Ext.Window({
	id : 'formAddWin',
	width : 960,
	height : 500,
	modal : true,
	border : true,
	bodyBorder : false,
	title : '新增司机排班',
	layout : 'border',
	closeAction : 'hide',
	items : [westPanel,eastPanel],
	buttons : [{
		text : "保存",
		handler : function() {
			validateSchedulingDateAndSave();
		}
	},{
		text : "取消",
		handler : function() {
			addWindow.hide();
		}
	}]
});

function validateSchedulingDateAndSave() {
	var schedulingDays = Ext.getCmp('add_datepicker_update').getDays();
	var empRecords = Ext.getCmp('emp_grid').getSelectionModel().getSelections();
	var lineConfigureRecords = grid_lineConfigure.getSelectionModel().getSelections();

	if (empRecords.length < 1) {
		Ext.Msg.alert('提示', '请选择人员!');
		return;
	}
	if (lineConfigureRecords.length < 1) {
		Ext.Msg.alert('提示', '请选择一条配班数据!');
		return;
	}

	if (schedulingDays.length < 1) {
		Ext.Msg.alert('提示', '请选择排班日期!');
		return;
	}

	var selectedLineConfigure = grid_lineConfigure.getSelectionModel().getSelected();
	var configureCode = selectedLineConfigure.data['CODE'];
	if ('休' != configureCode) {
		configureCode = selectedLineConfigure.data['DEPT_CODE'] + '-' + configureCode;
	}
	var departmentCode = Ext.getCmp("query.emp.departmentCode").getValue();
	var employeeCodes = [];
	Ext.each(empRecords, function(v, i) {
		employeeCodes.push(empRecords[i].data['EMP_CODE']+"/" + empRecords[i].data['DEPT_CODE']);
	});

	var myMask = new Ext.LoadMask(addWindow.getEl(), {
		msg : "正在新增..."
	});
	myMask.show();

	var weekOfYear = Ext.get("weekOfYear").getValue();
	Ext.Ajax.request({
		url : "addScheduling_scheduling.action",
		async : true,
		params : {
			employeeCode : employeeCodes.join(","),
			configureCode : configureCode,
			schedulingDays : schedulingDays.join(","),
			yearWeek : weekOfYear
		},
		success : function(response) {
			myMask.hide();
			result = Ext.util.JSON.decode(response.responseText);
			if (Ext.isEmpty(result.resultMap.errorMessage)) {
				Ext.Msg.alert("提示", "新增成功!");
				addWindow.hide();
				queryDriverScheduling();
				return;
			}
			Ext.Msg.alert("提示", "新增失败!" + result.resultMap.errorMessage);
		},
		failure : function(response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);// 就可以取出来。如果是数组，那么很简单
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert('提示', "新增失败！服务器错误！");
				return;
			}
			Ext.Msg.alert("提示", "新增失败!" + result.error);
		}
	});

}

function isAreaCode(departmentCode) {
	var flag = false;
	Ext.Ajax.request({
		url : "isAreaCode_scheduling.action",
		async : false,
		params : {
			departmentCode : departmentCode
		},
		success : function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			if (result.success) {
				flag = true;
				return;
			}
			
			area_department_id = result.resultMap.areaDepartment.id;
		},
		failure : function(response) {
		}
	});
	return flag;
}

// 当参数
function includeRestClass(schedulingCodes) {
	return schedulingCodes.length > 1 && schedulingCodes.indexOf('休') != -1;
}

function classMoreThanThree(schedulingCodes) {
	return schedulingCodes.split(",").length > 3;
}

function checkMonth() {
	var prevWeekMonday = moment().subtract(1, 'weeks').startOf('isoWeek');
	var selectWeekSunday = moment(Ext.get("selectedDay").getValue()).endOf('isoWeek');
	return selectWeekSunday.isBefore(prevWeekMonday);
}

var btnAddScheduling = new Ext.Button({
	text : "新增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		var queryType = Ext.getCmp("query.queryType").getValue();
		if ("1" != queryType) {
			Ext.Msg.alert('提示', "只有查询方式为'按周'查询时才支持新增排班！");
			return;
		}
		var departmentCode = Ext.getCmp("query.departmentCode").getValue();
		if (Ext.isEmpty(departmentCode)) {
			Ext.Msg.alert('提示', "请选择一个网点！");
			return;
		}
		if (checkMonth()) {
			Ext.Msg.alert('提示', "只能新增上周及以后的排班！");
			return;
		}
		if (isAreaCode(departmentCode)) {
			Ext.Msg.alert('提示', "只能在经营本部以下网点新增排班！");
			return;
		}
		addWindow.show();
		Ext.getCmp("query.emp.departmentCode").setValue(departmentCode);
		Ext.getCmp("query.lineConfigure.department").setValue(departmentCode);
		Ext.getCmp("query.emp.employeeCode").setValue('');
		Ext.getCmp("query.emp.employeeName").setValue('');
		Ext.getCmp("query.lineConfigure.code").setValue('');
		searchEmployee();
		searchLineConfigure();
		initDatePicker();
	}
});

function initDatePicker() {
	var yearWeek = Ext.get("weekOfYear").getValue();
	var selectedDay = Ext.get("selectedDay").getValue();
	var monday = moment().year(yearWeek.split("-")[0]).isoWeek(yearWeek.split("-")[1]).isoWeekday(1);
	var sunday = moment().year(yearWeek.split("-")[0]).isoWeek(yearWeek.split("-")[1]).isoWeekday(7);
	var firstDayOfMonth = moment(selectedDay).startOf('month');
	var lastDayOfMonth = moment(selectedDay).endOf('month');
	if (monday.isBefore(firstDayOfMonth)) {
		monday = firstDayOfMonth;
	}
	if (sunday.isAfter(lastDayOfMonth)) {
		sunday = lastDayOfMonth;
	}

	add_scheduling_datePicker.setValue(new Date(monday));
	add_scheduling_datePicker.setMinDate(new Date(monday));
	add_scheduling_datePicker.setMaxDate(new Date(sunday));
}