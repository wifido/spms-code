//<%@ page language="java" contentType="text/html; charset=utf-8"%>

//左侧网点树

// 查询按钮
var btnSearch = new Ext.Button({
	text: "查 询",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		if (Ext.getCmp("query.AreaCode").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择网点！");
			return;
		}
		if (Ext.getCmp("query.month").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择月份！");
			return;
		}
		Ext.Ajax.request({
			async: false,
			url: '../dispatch/dispatch_queryPermissions.action',
			params: {'DEPARTMENT_CODE':getDepartmentCodes()},
			success: function(res, config) {
					var returnObj = Ext.decode(res.responseText);
					if (returnObj.dataMap.totalSize == 0){
						Ext.Msg.alert("提示", "当前用户没有该网点的权限！");	
						return;
					}else{
						querySchedule();
					}
			}
		});	
	}
});

// 新增按钮
var btnAdd = new Ext.Button({
	text: "新 增",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {		
		addSchedule();
	}
});

// 导出方法
var btnExport = new Ext.Button({
	text: "导出未排班",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		if (Ext.getCmp("query.AreaCode").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择网点！");
			return;
		}
		if (Ext.getCmp("query.month").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择月份！");
			return;
		}
		Ext.Ajax.request({
			async: false,
			url: '../dispatch/dispatch_queryPermissions.action',
			params: {'DEPARTMENT_CODE':getDepartmentCodes()},
			success: function(res, config) {					
					var returnObj = Ext.decode(res.responseText);
					if (returnObj.dataMap.totalSize == 0){
						Ext.Msg.alert("提示", "当前用户没有该网点的权限！");	
						return;
					}else{
						exportSchedule();
					}
			}
		});
		
	}
});

// 导出排班明细报表
var btnExportSchedulingDeatail = new Ext.Button({
	text: "导出排班明细",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		if (Ext.getCmp("query.AreaCode").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择网点！");
			return;
		}
		if (Ext.getCmp("query.month").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择月份！");
			return;
		}
		Ext.Ajax.request({
			async: false,
			url: '../dispatch/dispatch_queryPermissions.action',
			params: {'DEPARTMENT_CODE':getDepartmentCodes()},
			success: function(res, config) {					
					var returnObj = Ext.decode(res.responseText);
					if (returnObj.dataMap.totalSize == 0){
						Ext.Msg.alert("提示", "当前用户没有该网点的权限！");	
						return;
					}else{
						exportScheduleDeatail();
					}
			}
		});		
	}
});

// 导入按钮
var btnImport = new Ext.Button({
	text: "导 入",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		if (Ext.getCmp("query.AreaCode").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择网点！");
			return;
		}
		Ext.Ajax.request({
			async: false,
			url: '../dispatch/dispatch_queryPermissions.action',
			params: {'DEPARTMENT_CODE':getDepartmentCodes()},
			success: function(res, config) {
					var returnObj = Ext.decode(res.responseText);								
					if (returnObj.dataMap.totalSize == 0){
						Ext.Msg.alert("提示", "当前用户没有该网点的权限！");	
						return;
					}else{
						importSchedule();
					}				
			}
		});
	}
});

var btnDelete = new Ext.Button({
	text: "删除",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		if (Ext.getCmp("query.AreaCode").getValue() == "") {
			var records = grid.getSelectionModel().getSelections();
			if (records.length == 0) {
				Ext.Msg.alert("提示", "请选择一条记录！");
				return;
			}
		}else{
			Ext.Ajax.request({
				async: false,
				url: '../dispatch/dispatch_queryPermissions.action',
				params: {'DEPARTMENT_CODE':getDepartmentCodes()},
				success: function(res, config) {
						var returnObj = Ext.decode(res.responseText);								
						if (returnObj.dataMap.totalSize == 0){
							Ext.Msg.alert("提示", "当前用户没有该网点的权限！");	
							return;
						}else{
							var records = grid.getSelectionModel().getSelections();
							if (records.length == 0) {
								Ext.Msg.alert("提示", "请选择一条记录！");
								return;
							}
							deleteScheduleDaily(getEmployeeCode(records), Ext.util.Format.date(Ext.getCmp('query.month').getValue(), 'Ym'));
						}				
				}
			});
		}		
	}
});

function getEmployeeCode(records) {
	var employeeCode = "";
	for ( var i = 0; i < records.length; i++) {
		var record = records[i];
		employeeCode = employeeCode + record.data.employeeCode + ",";
	}
	return employeeCode;
}

// 重置
var btnReset = new Ext.Button({
	text: "重置",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		resetSearchCriteria();
	}
});

var resetSearchCriteria = function() {
	Ext.getCmp('query.AreaCode').reset();
	Ext.getCmp('query.month').reset();
	Ext.getCmp('query.dataSourcePostType').reset();
	Ext.getCmp('query.employeeCode').reset();
	Ext.getCmp('query.employeeName').reset();
};

var tbar = [];

addBar('<app:isPermission code = "/dispatch/dispatch_querySchedule.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/dispatch/dispatch_updateSchedule.action">a</app:isPermission>', btnEditHoursWork);
addBar('<app:isPermission code = "/dispatch/dispatch_scheduleUploadFile.action">a</app:isPermission>', btnImport);
addBar('<app:isPermission code = "/dispatch/dispatch_exportSchedule.action">a</app:isPermission>', btnExport);
addBar('<app:isPermission code = "/dispatch/dispatch_deleteSchedule.action">a</app:isPermission>', btnDelete);
addBar('<app:isPermission code = "/dispatch/dispatch_reset.action">a</app:isPermission>', btnReset);
addBar('<app:isPermission code = "/dispatch/dispatch_exportSchedulingDeatail.action">a</app:isPermission>', btnExportSchedulingDeatail);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}

// 顶部的Panel
var topPanel = new Ext.Panel({
	frame: true,
	layout: 'column',
	height: 160,
	tbar: tbar,
	items: [{
		xtype: 'fieldset',
		title: '查询条件',
		layout: "column",
		columnWidth: 1,
		style: 'margin-top:5px;',
		frame: true,
		items: [{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'trigger',
				triggerClass: 'x-form-search-trigger',
				id: 'query.AreaCode',
				fieldLabel: '<font color=red>网点代码*</font>',
				anchor: '90%',
				onTriggerClick: function() {
					var _window = new Ext.sf_dept_window({
						branchDepartmentEditable: true,
						isDispatch: true,
						callBackInput: "query.AreaCode"
					});
					_window.show(this);
				}
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: "form",
			items: [{
				xtype: 'datefield',
				width: 100,
				fieldLabel: "月份",
				id: "query.month",
				format: 'Y-m',
				value: new Date,
				plugins: 'monthPickerPlugin',
				anchor: '90%'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'query.employeeCode',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '员工工号',
				anchor: '90%'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'query.employeeName',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '员工姓名',
				anchor: '90%'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'combo',
				id: 'query.dataSourcePostType',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				forceSelection: true,
				emptyText: '请选择...',
				fieldLabel: '岗位类型',
				store: [['0|4','排班全日制'],['0|-4','排班非全日制'],['1|4','调度全日制'],['1|-4','调度非全日制']],
				anchor: '90%'
			}]
		}]
	}]
});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '年月',
		sortable: true,
		dataIndex: 'monthId',
		width: 50,
		align: "center"
	},{
		header: '地区代码',
		sortable: true,
		dataIndex: 'areaCode',
		align: "center"
	},{
		header: '分点部代码',
		sortable: true,
		dataIndex: 'divisionCode',
		align: "center"
	},{
		header: '网点代码',
		sortable: true,
		dataIndex: 'departmentCode',
		align: "center"
	},{
		header: '员工工号',
		sortable: true,
		dataIndex: 'employeeCode',
		align: "center"
	},{
		header: '员工姓名',
		sortable: true,
		dataIndex: 'employeeName',
		align: "center"
	},{
		header: '员工状态',
		sortable: true,
		dataIndex: 'cancelFlag',
		align: "center"
	},{
		header: '离职时间',
		sortable: true,
		dataIndex: 'dimissionTime',
		align: "center"
	},{
		header: '数据来源',
		sortable: true,
		dataIndex: 'dataSource',
		align: "center"
	},{
		header: 'workType',
		hidden: true,
		sortable: true,
		dataIndex: 'workType',
		align: "center"
	},{
		header: '1',
		sortable: true,
		dataIndex: 'firstDay',
		width: 80,
		align: "center"
	},{
		header: '2',
		sortable: true,
		dataIndex: 'secondDay',
		width: 80,
		align: "center"
	},{
		header: '3',
		sortable: true,
		dataIndex: 'thirdDay',
		width: 80,
		align: "center"
	},{
		header: '4',
		sortable: true,
		dataIndex: 'fourthDay',
		width: 80,
		align: "center"
	},{
		header: '5',
		sortable: true,
		dataIndex: 'fifthDay',
		width: 80,
		align: "center"
	},{
		header: '6',
		sortable: true,
		dataIndex: 'sixthDay',
		width: 80,
		align: "center"
	},{
		header: '7',
		sortable: true,
		dataIndex: 'seventhDay',
		width: 80,
		align: "center"
	},{
		header: '8',
		sortable: true,
		dataIndex: 'eighthDay',
		width: 80,
		align: "center"
	},{
		header: '9',
		sortable: true,
		dataIndex: 'ninthDay',
		width: 80,
		align: "center"
	},{
		header: '10',
		sortable: true,
		dataIndex: 'tenthDay',
		width: 80,
		align: "center"
	},{
		header: '11',
		sortable: true,
		dataIndex: 'eleventhDay',
		width: 80,
		align: "center"
	},{
		header: '12',
		sortable: true,
		dataIndex: 'twelfthDay',
		width: 80,
		align: "center"
	},{
		header: '13',
		sortable: true,
		dataIndex: 'thirteenthDay',
		width: 80,
		align: "center"
	},{
		header: '14',
		sortable: true,
		dataIndex: 'fourteenthDay',
		width: 80,
		align: "center"
	},{
		header: '15',
		sortable: true,
		dataIndex: 'fifteenthDay',
		width: 80,
		align: "center"
	},{
		header: '16',
		sortable: true,
		dataIndex: 'sixteenthDay',
		width: 80,
		align: "center"
	},{
		header: '17',
		sortable: true,
		dataIndex: 'seventeenthDay',
		width: 80,
		align: "center"
	},{
		header: '18',
		sortable: true,
		dataIndex: 'eighteenthDay',
		width: 80,
		align: "center"
	},{
		header: '19',
		sortable: true,
		dataIndex: 'nineteenthDay',
		width: 80,
		align: "center"
	},{
		header: '20',
		sortable: true,
		dataIndex: 'twentiethDay',
		width: 80,
		align: "center"
	},{
		header: '21',
		sortable: true,
		dataIndex: 'twentyFirstDay',
		width: 80,
		align: "center"
	},{
		header: '22',
		sortable: true,
		dataIndex: 'twentySecondDay',
		width: 80,
		align: "center"
	},{
		header: '23',
		sortable: true,
		dataIndex: 'twentyThirdDay',
		width: 80,
		align: "center"
	},{
		header: '24',
		sortable: true,
		dataIndex: 'twentyFourthDay',
		width: 80,
		align: "center"
	},{
		header: '25',
		sortable: true,
		dataIndex: 'twentyFifthDay',
		width: 80,
		align: "center"
	},{
		header: '26',
		sortable: true,
		dataIndex: 'twentySixthDay',
		width: 80,
		align: "center"
	},{
		header: '27',
		sortable: true,
		dataIndex: 'twentySeventhDay',
		width: 80,
		align: "center"
	},{
		header: '28',
		sortable: true,
		dataIndex: 'twentyEighthDay',
		width: 80,
		align: "center"
	},{
		header: '29',
		sortable: true,
		dataIndex: 'twentyNinthDay',
		width: 80,
		align: "center"
	},{
		header: '30',
		sortable: true,
		dataIndex: 'thirtiethDay',
		width: 80,
		align: "center"
	},{
		header: '31',
		sortable: true,
		dataIndex: 'thirtyFirstDay',
		width: 80,
		align: "center"
	},{
		header: '创建人',
		sortable: true,
		dataIndex: 'createdEmployeeCode',
		width: 100,
		align: "center"
	},{
		header: '修改人',
		sortable: true,
		dataIndex: 'modifiedEmployeeCode',
		width: 200,
		align: "center"
	},{
		header: '修改时间',
		sortable: true,
		dataIndex: 'modifiedTime',
		width: 200,
		align: "center",
		renderer: function(value) {
			if (Ext.isEmpty(value)) {
				return "";
			}
			return value.replace('T',' ');
		}
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'monthId',
	mapping: 'monthId',
	type: 'string'
},{
	name: 'employeeCode',
	mapping: 'employeeCode',
	type: 'string'
},{
	name: 'employeeName',
	mapping: 'employeeName',
	type: 'string'
},{
	name: 'cancelFlag',
	mapping: 'cancelFlag',
	type: 'string'
},{
	name: 'dimissionTime',
	mapping: 'dimissionTime',
	type: 'string'
},{
	name: 'dataSource',
	mapping: 'dataSource',
	type: 'string'
},{
	name: 'areaCode',
	mapping: 'areaCode',
	type: 'string'
},{
	name: 'divisionCode',
	mapping: 'divisionCode',
	type: 'string'
},{
	name: 'departmentCode',
	mapping: 'departmentCode',
	type: 'string'
},{
	name: 'deptName',
	mapping: 'deptName',
	type: 'string'
},{
	name: 'createdEmployeeCode',
	mapping: 'createdEmployeeCode',
	type: 'string'
},{
	name: 'modifiedEmployeeCode',
	mapping: 'modifiedEmployeeCode',
	type: 'string'
},{
	name: 'modifiedTime',
	mapping: 'modifiedTime',
	type: 'string'
},{
	name: 'workType',
	mapping: 'workType',
	type: 'string'
},{
	name: 'firstDay',
	mapping: 'firstDay',
	type: 'string'
},{
	name: 'secondDay',
	mapping: 'secondDay',
	type: 'string'
},{
	name: 'thirdDay',
	mapping: 'thirdDay',
	type: 'string'
},{
	name: 'fourthDay',
	mapping: 'fourthDay',
	type: 'string'
},{
	name: 'fifthDay',
	mapping: 'fifthDay',
	type: 'string'
},{
	name: 'sixthDay',
	mapping: 'sixthDay',
	type: 'string'
},{
	name: 'seventhDay',
	mapping: 'seventhDay',
	type: 'string'
},{
	name: 'eighthDay',
	mapping: 'eighthDay',
	type: 'string'
},{
	name: 'ninthDay',
	mapping: 'ninthDay',
	type: 'string'
},{
	name: 'tenthDay',
	mapping: 'tenthDay',
	type: 'string'
},{
	name: 'eleventhDay',
	mapping: 'eleventhDay',
	type: 'string'
},{
	name: 'twelfthDay',
	mapping: 'twelfthDay',
	type: 'string'
},{
	name: 'thirteenthDay',
	mapping: 'thirteenthDay',
	type: 'string'
},{
	name: 'fourteenthDay',
	mapping: 'fourteenthDay',
	type: 'string'
},{
	name: 'fifteenthDay',
	mapping: 'fifteenthDay',
	type: 'string'
},{
	name: 'sixteenthDay',
	mapping: 'sixteenthDay',
	type: 'string'
},{
	name: 'seventeenthDay',
	mapping: 'seventeenthDay',
	type: 'string'
},{
	name: 'eighteenthDay',
	mapping: 'eighteenthDay',
	type: 'string'
},{
	name: 'nineteenthDay',
	mapping: 'nineteenthDay',
	type: 'string'
},{
	name: 'twentiethDay',
	mapping: 'twentiethDay',
	type: 'string'
},{
	name: 'twentyFirstDay',
	mapping: 'twentyFirstDay',
	type: 'string'
},{
	name: 'twentySecondDay',
	mapping: 'twentySecondDay',
	type: 'string'
},{
	name: 'twentyThirdDay',
	mapping: 'twentyThirdDay',
	type: 'string'
},{
	name: 'twentyFourthDay',
	mapping: 'twentyFourthDay',
	type: 'string'
},{
	name: 'twentyFifthDay',
	mapping: 'twentyFifthDay',
	type: 'string'
},{
	name: 'twentySixthDay',
	mapping: 'twentySixthDay',
	type: 'string'
},{
	name: 'twentySeventhDay',
	mapping: 'twentySeventhDay',
	type: 'string'
},{
	name: 'twentyEighthDay',
	mapping: 'twentyEighthDay',
	type: 'string'
},{
	name: 'twentyNinthDay',
	mapping: 'twentyNinthDay',
	type: 'string'
},{
	name: 'thirtiethDay',
	mapping: 'thirtiethDay',
	type: 'string'
},{
	name: 'thirtyFirstDay',
	mapping: 'thirtyFirstDay',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../dispatch/querySchedule.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'dataMap.root',
		totalProperty: 'dataMap.totalSize'
	}, record)
});

var confirm_Store = new Ext.data.Store({
	reader: new Ext.data.ArrayReader({}, record),
	pruneModifiedRecords: true
});

var pbar = new Ext.PagingToolbar({
	displayInfo: true,
	store: confirm_Store,
	pageSize: 20,
	displayMsg: "当前显示 {0} - {1} 总记录数目 {2}",
	emptyMsg: "未检索到数据"
});

var confirm_sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: false
});
var confirm_gridView = new Ext.grid.GridPanel({
	enableHdMenu: false,
	frame: true,
	loadMask: true,
	columnLines: true,
	region: "center",
	store: confirm_Store,
	// tbar : pbar,
	sm: confirm_sm,
	cm: new Ext.grid.ColumnModel([confirm_sm,{
		header: '年月',
		sortable: true,
		dataIndex: 'monthId'
	},{
		header: '分点部代码',
		sortable: true,
		dataIndex: 'departmentCode'
	},{
		header: '员工工号',
		sortable: true,
		dataIndex: 'employeeCode'
	},{
		header: '员工姓名',
		sortable: true,
		dataIndex: 'employeeName'
	},{
		header: '员工状态',
		sortable: true,
		dataIndex: 'cancelFlag'
	},{
		header: '离职时间',
		sortable: true,
		dataIndex: 'dimissionTime'
	},{
		header: '数据来源',
		sortable: true,
		dataIndex: 'employeeStuas'
	},{
		header: '创建人',
		sortable: true,
		dataIndex: 'createdEmployeeCode'
	},{
		header: '修改人',
		sortable: true,
		dataIndex: 'modifiedEmployeeCode'
	},{
		header: 'workType',
		hidden: true,
		sortable: true,
		dataIndex: 'workType'
	},{
		header: '1',
		sortable: true,
		dataIndex: 'firstDay'
	},{
		header: '2',
		sortable: true,
		dataIndex: 'secondDay'
	},{
		header: '3',
		sortable: true,
		dataIndex: 'thirdDay'
	},{
		header: '4',
		sortable: true,
		dataIndex: 'fourthDay'
	},{
		header: '5',
		sortable: true,
		dataIndex: 'fifthDay'
	},{
		header: '6',
		sortable: true,
		dataIndex: 'sixthDay'
	},{
		header: '7',
		sortable: true,
		dataIndex: 'seventhDay'
	},{
		header: '8',
		sortable: true,
		dataIndex: 'eighthDay'
	},{
		header: '9',
		sortable: true,
		dataIndex: 'ninthDay'
	},{
		header: '10',
		sortable: true,
		dataIndex: 'tenthDay'
	},{
		header: '11',
		sortable: true,
		dataIndex: 'eleventhDay'
	},{
		header: '12',
		sortable: true,
		dataIndex: 'twelfthDay'
	},{
		header: '13',
		sortable: true,
		dataIndex: 'thirteenthDay'
	},{
		header: '14',
		sortable: true,
		dataIndex: 'fourteenthDay'
	},{
		header: '15',
		sortable: true,
		dataIndex: 'fifteenthDay'
	},{
		header: '16',
		sortable: true,
		dataIndex: 'sixteenthDay'
	},{
		header: '17',
		sortable: true,
		dataIndex: 'seventeenthDay'
	},{
		header: '18',
		sortable: true,
		dataIndex: 'eighteenthDay'
	},{
		header: '19',
		sortable: true,
		dataIndex: 'nineteenthDay'
	},{
		header: '20',
		sortable: true,
		dataIndex: 'twentiethDay'
	},{
		header: '21',
		sortable: true,
		dataIndex: 'twentyFirstDay'
	},{
		header: '22',
		sortable: true,
		dataIndex: 'twentySecondDay'
	},{
		header: '23',
		sortable: true,
		dataIndex: 'twentyThirdDay'
	},{
		header: '24',
		sortable: true,
		dataIndex: 'twentyFourthDay'
	},{
		header: '25',
		sortable: true,
		dataIndex: 'twentyFifthDay'
	},{
		header: '26',
		sortable: true,
		dataIndex: 'twentySixthDay'
	},{
		header: '27',
		sortable: true,
		dataIndex: 'twentySeventhDay'
	},{
		header: '28',
		sortable: true,
		dataIndex: 'twentyEighthDay'
	},{
		header: '29',
		sortable: true,
		dataIndex: 'twentyNinthDay'
	},{
		header: '30',
		sortable: true,
		dataIndex: 'thirtiethDay'
	},{
		header: '31',
		sortable: true,
		dataIndex: 'thirtyFirstDay'
	}])
});

// 分页组件
var pageBar = new Ext.PagingToolbar({
	store: store,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 20,
	emptyMsg: '未检索到数据'
});

// 表格构建
var grid = new Ext.grid.GridPanel({
	cm: cm,
	sm: sm,
	store: store,
	autoScroll: true,
	loadMask: true,
	tbar: pageBar
});

// 中部的Panel
var centerPanel = new Ext.Panel({
	region: 'center',
	margins: '1 1 1 0',
	items: [topPanel,grid],
	listeners: {
		resize: function(p, adjWidth, adjHeight, rawWidth, rawHeight) {
			grid.setWidth(adjWidth - 5);
			grid.setHeight(adjHeight - 165);
		}
	}
});

// 标识班别代码和班别名称唯一性
var onlyScheduleCode = false;
// 查询方法
var querySchedule = function(node) {	
	
	var dataSource = null;
	var workType = null;
	var dataSourceWorkType = Ext.getCmp('query.dataSourcePostType').getValue();

	if (dataSourceWorkType != "") {
		var array = dataSourceWorkType.split("|");
		dataSource = array[0];
		workType = array[1];
	}

	store.setBaseParam("DEPARTMENT_CODE", getDepartmentCodes());// checkedDepartmentListForSearching.toString()/Ext.getCmp('query.AreaCode').getValue().split('/')[0]
	store.setBaseParam("MONTH_ID", Ext.util.Format.date(Ext.getCmp('query.month').getValue(), 'Ym'));
	store.setBaseParam("DATA_SOURCE", dataSource);
	store.setBaseParam("WORK_TYPE", workType);
	store.setBaseParam("EMPLOYEE_CODE", Ext.getCmp('query.employeeCode').getValue());
	store.setBaseParam("EMP_NAME", Ext.getCmp('query.employeeName').getValue());

	store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
};

function getDepartmentCodes() {
	return Ext.getCmp("query.AreaCode").getValue().split("/")[0];
}

// 导出方法
var exportSchedule = function() {	
	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();
	Ext.Ajax.request({
		url: "dispatch_export.action",
		method: 'POST',
		params: {
			'MONTH_ID': Ext.util.Format.date(Ext.getCmp('query.month').getValue(), 'Ym'),
			'DEPARTMENT_CODE': getDepartmentCodes()
		},
		success: function(res, config) {
			myMask.hide();
			var obj = Ext.decode(res.responseText);
			if (!obj.success) {
				Ext.Msg.alert("提示", obj.msg);
				return;
			}
			var url = obj.fileName;
			window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
		},
		failure: function(res, config) {
			var obj = Ext.decode(res.responseText);
			Ext.Msg.alert("提示", obj.msg);
			myMask.hide();
		}
	});
};

// 导出方法
var exportScheduleDeatail = function() {	
	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();
	Ext.Ajax.request({
		url: "dispatch_exportSchedulingDeatail.action",
		method: 'POST',
		params: {
			'MONTH_ID': Ext.util.Format.date(Ext.getCmp('query.month').getValue(), 'Ym'),
			'DEPARTMENT_CODE': getDepartmentCodes()
		},
		success: function(res, config) {
			myMask.hide();
			var obj = Ext.decode(res.responseText);
			if (!obj.success) {
				Ext.Msg.alert("提示", obj.msg);
				return;
			}
			var url = obj.fileName;
			window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
		},
		failure: function(res, config) {
			var obj = Ext.decode(res.responseText);
			Ext.Msg.alert("提示", obj.msg);
			myMask.hide();
		}
	});
};

// 模板下载
var downTemp = function() {
	url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=一线排班模板.xls&moduleName=dispatch&entityName=SchedulingForDispatch&isTemplate=true";
	window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
};

// 导入功能校验没通过返回数据下载
var downError = function(objectA) {
	window.location = objectA.attributes.url.nodeValue;
};

// 导入方法

var importSchedule = function() {	
	var win = new Ext.Window({
		width: 580,
		height: 200,
		modal: true,
		border: false,
		bodyBorder: false,
		closable: false,
		resizable: false,
		layout: 'fit',
		title: '导入',
		items: [{
			xtype: 'form',
			border: false,
			fileUpload: true,
			frame: true,
			labelAlign: 'right',
			items: [{
				border: false,
				height: 20
			},{
				border: false,
				xtype: 'label',
				style: 'margin-left : 60px;',
				html: '<font color=red size=4>最多导入1000条数据</font>'
			},{
				border: false,
				height: 10
			},{
				xtype: 'textfield',
				inputType: 'file',
				width: 290,
				name: 'importExcelFile',
				fieldLabel: '文件路径'
			},{
				xtype: 'textfield',
				hidden: true,
				name: 'departmentCodes',
				fieldLabel: '网点',
				value: getDepartmentCodes()
			},{
				border: false,
				height: 10
			},{
				border: false,
				xtype: 'label',
				style: 'margin-left : 40px;',
				html: '<font size=3>模板文件： </font> <a href="#" onclick="downTemp()">导入文件模板下载</a>'
			}],
			fbar: [{
				text: '上传',
				handler: function() {
					var form = this.ownerCt.ownerCt;
					var basicForm = form.getForm();
					var fileName = basicForm.findField("importExcelFile").getValue();
					var xls = fileName.substr(fileName.lastIndexOf(".")).toLowerCase();// 获得文件后缀名
					if (xls != '.xls') {
						Ext.Msg.alert("提示", "系统只支持xls类型文件上传，请下载模板！");
						return;
					}
					basicForm.submit({
						url: '../dispatch/importScheduling.action',
						success: function(form, action) {
							win.close();
							if (Ext.isEmpty(action.result.bizMsg)) {
								var url = "../common/downloadReportFile.action?" + encodeURI(encodeURI(action.result.fileName));
								var aTag = "";
								if (action.result.fileName) {
									aTag = "<a href = '#' url='" + url + "' onclick = 'downError(this)'>错误数据下载</a>";
								}
								Ext.Msg.alert('提示', action.result.msg + aTag);
							} else {
								Ext.Msg.alert('提示', action.result.bizMsg);
							}
							if (Ext.isEmpty(action.result.schedulMgts)) {
								return;
							}
						},
						failure: function(form, action) {
							if (Ext.isEmpty(action.result.bizMsg)) {
								Ext.Msg.alert('提示', "导入模板错误,请检查导入文件！");
							} else {
								Ext.Msg.alert('提示', action.result.bizMsg);
							}
							win.close();
						},
						waitTitle: '提示',
						waitMsg: '正在导入，请稍后'
					});
				}
			},{
				text: '取消',
				handler: function() {
					win.close();
				}
			}]
		}]
	});
	win.show();
};
// 查询班别代码的唯一性
var onlySchedule = function(params, opera, obj) {
	Ext.Ajax.request({
		url: '../dispatch/dispatch_querySchedule.action',
		params: params,
		success: function(res, config) {
			var returnObj = Ext.decode(res.responseText);
			if (returnObj.totalSize == 0)
				onlyScheduleCode = true;
			else
				obj.markInvalid("此值已存在！");
		}
	});
};

// 初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout: "border",
		items: [centerPanel]
	});
});

var deleteScheduleDaily = function(employeeCodes, monthId) {
	Ext.Msg.confirm("提示", "是否确认删除？", function(btn) {
		if (btn == "yes") {
			Ext.Ajax.request({
				url: '../dispatch/dispatch_deleteScheulDaily.action',
				method: 'POST',
				params: {
					empCodes: employeeCodes,
					MONTH_ID: monthId
				},
				success: function(res, config) {
					var obj = Ext.decode(res.responseText);
					if (obj.success) {
						Ext.Msg.alert("提示", "删除成功！");
					} else {
						Ext.Msg.alert("提示", "删除失败！");
					}
					querySchedule();
				},
				failure: function(res, config) {
					Ext.Msg.alert("提示", "删除失败，系统异常！");
				}
			});
		}
	}, this);
};