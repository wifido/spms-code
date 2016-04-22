//<%@ page language="java" contentType="text/html; charset=utf-8"%>

function validQueryConditionFail(departmentCode, empCode, empName) {
	if (Ext.isEmpty(departmentCode) && Ext.isEmpty(empCode) && Ext.isEmpty(empName)) {
		Ext.Msg.alert("提示", "网点代码、员工工号、姓名必须要填写其中一项！");
		return true;
	}
	
	if (Ext.getCmp('startTime').getValue() == "") {
		Ext.getCmp('startTime').focus(false, 100);
		Ext.Msg.alert("提示", "请先选择开始时间！");
		return true;
	}
	
	if (Ext.getCmp('endTime').getValue() == "") {
		Ext.getCmp('endTime').focus(false, 100);
		Ext.Msg.alert("提示", "请先选择结束时间！");
		return true;
	}
}

function parseDepartment(departmentCodes) {
	var codes = [];
	Ext.each(departmentCodes.split(','), function(v, i) {
		codes.push("'" + v.split('/')[0] + "'");
	});
	return codes.join(",");
}

// 查询按钮
var btnSearch = new Ext.Button({
	text: "查 询",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		querySchedule();
	}
});

var btnExport = new Ext.Button({
	text: '导出',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCode = Ext.getCmp("query.branchCode").getValue();
		var empCode = Ext.getCmp('query.empCode').getValue();
		var empName = Ext.getCmp('query.empName').getValue();
		
		if (validQueryConditionFail(departmentCode, empCode, empName)) {
			return;
		}
		
		var myMask = new Ext.LoadMask(centerPanel.getEl(), {
			msg: "正在导出..."
		});
		myMask.show();

		Ext.Ajax.request({
			url: 'export_warehouseAttendanceDetail.action',
			method: 'POST',
			timeout: 500000,
			params: {
				paging: 'yes',
				departmentCode: parseDepartment(departmentCode),
				startTime: Ext.util.Format.date(Ext.getCmp('startTime').getValue(), 'Y/m/d'),
				endTime: Ext.util.Format.date(Ext.getCmp('endTime').getValue(), 'Y/m/d'),
				emp_code: empCode,
				emp_name: empName
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
	}
});

var tbar = [];

addBar('<app:isPermission code = "/warehouse/query_warehouseAttendanceDetail.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/warehouse/export_warehouseAttendanceDetail.action">a</app:isPermission>', btnExport);

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
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: "textfield",
				fieldLabel: '网点代码',
				id: "query.branchCode",
				anchor: '90%',
				onTriggerClick: function() {
					var _window = new Ext.sf_dept_window({
						callBackInput: "query.branchCode"
					});
					_window.show(this);
				}
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'startTime',
				name: 'startTime',
				anchor: '90%',
				allowBlank: false,
				format: 'Y/m/d',
				fieldLabel: '开始时间<font color=red>*</font>'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'endTime',
				name: 'endTime',
				anchor: '90%',
				allowBlank: false,
				format: 'Y/m/d',
				fieldLabel: '结束时间<font color=red>*</font>'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'field',
				id: 'query.empCode',
				fieldLabel: '员工工号',
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'field',
				id: 'query.empName',
				fieldLabel: '姓名',
				anchor: '90%'
			}]
		}]
	}]
});

/**
 * 重写Grid.Column.renderer 每列悬停时提示内容信息
 */
Ext.override(Ext.grid.Column, {
	renderer: function(value, metadata, record, rowIdx, colIdx, ds) {
		if (this.rendererCall) {
			var ret = this.rendererCall(value, metadata, record, rowIdx, colIdx, ds);
			return '<div ext:qtitle="' + this.header + '" ext:qtip="' + (ret == null ? "" : ret) + '">' + (ret == null ? "" : ret) + '</div>';
		} else {
			return '<div ext:qtitle="' + this.header + '" ext:qtip="' + (value == null ? "" : value) + '">' + (value == null ? "" : value) + '</div>';
		}
	}
});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

var record_start = 0;
// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '日期',
		sortable: true,
		dataIndex: 'WORK_DATE',
		align: "center;vertical-align:middle"
	},{
		header: '地区代码',
		sortable: true,
		dataIndex: 'AREA_CODE',
		align: "center;vertical-align:middle"
	},{
		header: '网点代码',
		sortable: true,
		dataIndex: 'DEPT_CODE',
		align: "center;vertical-align:middle"
	},{
		header: '排班网点代码',
		sortable: true,
		dataIndex: 'SCHEDULED_DEPT',
		align: "center;vertical-align:middle"
	},{
		header: '工号',
		sortable: true,
		dataIndex: 'EMP_CODE',
		align: "center;vertical-align:middle"
	},{
		header: '姓名',
		sortable: true,
		dataIndex: 'EMP_NAME',
		align: "center;vertical-align:middle"
	},{
		header: '人员类型',
		sortable: true,
		dataIndex: 'PERSON_TYPE',
		align: "center;vertical-align:middle"
	},{
		header: '岗位名称',
		sortable: true,
		dataIndex: 'EMP_DUTY_NAME',
		align: "center;vertical-align:middle"
	},{
		header: '上下班时间段',
		sortable: true,
		dataIndex: 'SCHEDULE_CODE',
		width: 200,
		align: "center;vertical-align:middle"
	},{
		header: '排班时长',
		sortable: true,
		dataIndex: 'ARBST',
		align: "center;vertical-align:middle"
	},{
		header: '考勤小时数',
		sortable: true,
		dataIndex: 'KQ_XSS',
		align: "center;vertical-align:middle"
	},{
		header: '加班时长',
		sortable: true,
		dataIndex: 'STDAZ',
		align: "center;vertical-align:middle"
	},{
		header: '考勤系数',
		sortable: true,
		dataIndex: 'WORK_TIME',
		align: "center;vertical-align:middle"
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'WORK_DATE',
	mapping: 'WORK_DATE',
	type: 'string'
},{
	name: 'AREA_CODE',
	mapping: 'AREA_CODE',
	type: 'string'
},{
	name: 'DEPT_CODE',
	mapping: 'DEPT_CODE',
	type: 'string'
},{
	name: 'SCHEDULED_DEPT',
	mapping: 'SCHEDULED_DEPT',
	type: 'string'
},{
	name: 'EMP_CODE',
	mapping: 'EMP_CODE',
	type: 'string'
},{
	name: 'EMP_NAME',
	mapping: 'EMP_NAME',
	type: 'string'
},{
	name: 'PERSON_TYPE',
	mapping: 'PERSON_TYPE',
	type: 'string'
},{
	name: 'EMP_DUTY_NAME',
	mapping: 'EMP_DUTY_NAME',
	type: 'string'
},{
	name: 'SCHEDULE_CODE',
	mapping: 'SCHEDULE_CODE',
	type: 'string'
},{
	name: 'ARBST',
	mapping: 'ARBST',
	type: 'string'
},{
	name: 'KQ_XSS',
	mapping: 'KQ_XSS',
	type: 'string'
},{
	name: 'STDAZ',
	mapping: 'STDAZ',
	type: 'string'
},{
	name: 'WORK_TIME',
	mapping: 'WORK_TIME',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../warehouse/query_warehouseAttendanceDetail.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'dataMap.root',
		totalProperty: 'dataMap.totalSize'
	}, record)
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
	tbar: pageBar,
	viewConfig: {
	// forceFit: true
	}
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

// 查询方法
var querySchedule = function(node) {
	var departmentCode = Ext.getCmp("query.branchCode").getValue();
	var empCode = Ext.getCmp('query.empCode').getValue();
	var empName = Ext.getCmp('query.empName').getValue();
	
	if (validQueryConditionFail(departmentCode, empCode, empName)) {
		return;
	}
	
	store.setBaseParam("departmentCode", parseDepartment(departmentCode));
	store.setBaseParam("startTime", Ext.util.Format.date(Ext.getCmp('startTime').getValue(), 'Y/m/d'));
	store.setBaseParam("endTime", Ext.util.Format.date(Ext.getCmp('endTime').getValue(), 'Y/m/d'));
	store.setBaseParam("emp_code", empCode);
	store.setBaseParam('emp_name', empName);
	
	store.load({
		params: {
			start: 0,
			limit: 20
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
