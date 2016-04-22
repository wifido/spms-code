//<%@ page language="java" contentType="text/html; charset=utf-8"%>
var CONDITION_EMPLOYEE_NAME = 'EMP_NAME';
var CONDITION_EMPLOYEE_TYPE = 'WORK_TYPE';
var CONDITION_POST_NAME = 'EMP_DUTY_NAME';

var LABEL_EMPLOYEE_NAME = '姓名';
var LABEL_EMPLOYEE_TYPE = '人员类型';
var LABEL_POST_NAME = '岗位名称';

var query_departmentCode = '';

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

var exportAttendance = function() {
	if (Ext.getCmp('query.monthId').getValue() == "") {
		Ext.Msg.alert("提示", "请先选择月份！");
		return;
	}
	var departmentCode = Ext.getCmp("query.branchCode").getValue();
	var empCode = Ext.getCmp('query.empCode').getValue();
	var empName = Ext.getCmp(CONDITION_EMPLOYEE_NAME).getValue();
	if (Ext.isEmpty(departmentCode) && Ext.isEmpty(empCode) && Ext.isEmpty(empName)) {
		Ext.Msg.alert("提示", "网点代码、员工工号、姓名必须要填写其中一项！");
		return;
	}
	if (Ext.isEmpty(departmentCode)) {
		departmentCode = "001";
	}

	var monthId = Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Ym');
	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();

	Ext.Ajax.request({
		url: 'export_attendance.action',
		method: 'POST',
		timeout: 500000,
		params: {
			start: 0,
			limit: 60000,
			DEPARTMENT_CODE: departmentCode,
			EMP_CODE: empCode,
			EMP_NAME: empName,
			WORK_TYPE: Ext.getCmp("WORK_TYPE").getValue(),
			MONTH_ID: monthId
		},
		success: function(response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert("提示", "导出成功!");
				var url = result.resultMap.downloadPath;
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

var btnExport = new Ext.Button({
	text: "导出",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		exportAttendance();
	}
});

var tbar = [];

addBar('<app:isPermission code = "/warehouse/query_attendance.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/warehouse/export_attendance.action">a</app:isPermission>', btnExport);

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
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'query.monthId',
				fieldLabel: '<font color=red>*月份</font>',
				anchor: '90%',
				format: 'Y-m',
				value: new Date,
				plugins: 'monthPickerPlugin'
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
				id: CONDITION_EMPLOYEE_NAME,
				fieldLabel: LABEL_EMPLOYEE_NAME,
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'combo',
				id: CONDITION_EMPLOYEE_TYPE,
				anchor: '90%',
				fieldLabel: LABEL_EMPLOYEE_TYPE,
				mode: 'local',
				emptyText: '请选择...',
				forceSelection: true,
				triggerAction: 'all',
				store: new Ext.data.SimpleStore({
					fields: ['value','text'],
					data: workType
				}),
				valueField: 'text',
				displayField: 'text'
			}]
		}]
	}]
});
// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '考勤月份',
		sortable: true,
		dataIndex: 'month',
		align: "center;vertical-align:middle"
	},{
		header: '地区代码',
		sortable: true,
		dataIndex: 'areaCode',
		align: "center;vertical-align:middle"
	},{
		header: '网点代码',
		sortable: true,
		dataIndex: 'deptCode',
		align: "center;vertical-align:middle"
	},{
		header: '工号',
		sortable: true,
		dataIndex: 'empCode',
		align: "center;vertical-align:middle"
	},{
		header: '姓名',
		sortable: true,
		dataIndex: 'empName',
		align: "center;vertical-align:middle"
	},{
		header: '人员类型',
		sortable: true,
		dataIndex: 'personType',
		align: "center;vertical-align:middle"
	},{
		header: '岗位',
		sortable: true,
		dataIndex: 'dutyName',
		align: "center;vertical-align:middle"
	},{
		header: '1',
		sortable: true,
		dataIndex: 'firstDay',
		align: "center;vertical-align:middle"
	},{
		header: '2',
		sortable: true,
		dataIndex: 'secondDay',
		align: "center;vertical-align:middle"
	},{
		header: '3',
		sortable: true,
		dataIndex: 'thirdDay',
		align: "center;vertical-align:middle"
	},{
		header: '4',
		sortable: true,
		dataIndex: 'fourthDay',
		align: "center;vertical-align:middle"
	},{
		header: '5',
		sortable: true,
		dataIndex: 'fifthDay',
		align: "center;vertical-align:middle"
	},{
		header: '6',
		sortable: true,
		dataIndex: 'sixthDay',
		align: "center;vertical-align:middle"
	},{
		header: '7',
		sortable: true,
		dataIndex: 'seventhDay',
		align: "center;vertical-align:middle"
	},{
		header: '8',
		sortable: true,
		dataIndex: 'eighthDay',
		align: "center;vertical-align:middle"
	},{
		header: '9',
		sortable: true,
		dataIndex: 'ninthDay',
		align: "center;vertical-align:middle"
	},{
		header: '10',
		sortable: true,
		dataIndex: 'tenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '11',
		sortable: true,
		dataIndex: 'eleventhDay',
		align: "center;vertical-align:middle"
	},{
		header: '12',
		sortable: true,
		dataIndex: 'twelfthDay',
		align: "center;vertical-align:middle"
	},{
		header: '13',
		sortable: true,
		dataIndex: 'thirteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '14',
		sortable: true,
		dataIndex: 'fourteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '15',
		sortable: true,
		dataIndex: 'fifteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '16',
		sortable: true,
		dataIndex: 'sixteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '17',
		sortable: true,
		dataIndex: 'seventeenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '18',
		sortable: true,
		dataIndex: 'eighteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '19',
		sortable: true,
		dataIndex: 'nineteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '20',
		sortable: true,
		dataIndex: 'twentiethDay',
		align: "center;vertical-align:middle"
	},{
		header: '21',
		sortable: true,
		dataIndex: 'twentyFirstDay',
		align: "center;vertical-align:middle"
	},{
		header: '22',
		sortable: true,
		dataIndex: 'twentySecondDay',
		align: "center;vertical-align:middle"
	},{
		header: '23',
		sortable: true,
		dataIndex: 'twentyThirdDay',
		align: "center;vertical-align:middle"
	},{
		header: '24',
		sortable: true,
		dataIndex: 'twentyFourthDay',
		align: "center;vertical-align:middle"
	},{
		header: '25',
		sortable: true,
		dataIndex: 'twentyFifthDay',
		align: "center;vertical-align:middle"
	},{
		header: '26',
		sortable: true,
		dataIndex: 'twentySixthDay',
		align: "center;vertical-align:middle"
	},{
		header: '27',
		sortable: true,
		dataIndex: 'twentySeventhDay',
		align: "center;vertical-align:middle"
	},{
		header: '28',
		sortable: true,
		dataIndex: 'twentyEighthDay',
		align: "center;vertical-align:middle"
	},{
		header: '29',
		sortable: true,
		dataIndex: 'twentyNinthDay',
		align: "center;vertical-align:middle"
	},{
		header: '30',
		sortable: true,
		dataIndex: 'thirtiethDay',
		align: "center;vertical-align:middle"
	},{
		header: '31',
		sortable: true,
		dataIndex: 'thirtyFirstDay',
		align: "center;vertical-align:middle"
	},{
		header: '总计',
		sortable: true,
		dataIndex: 'totalWorkTime',
		align: "center;vertical-align:middle"
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'month',
	mapping: 'month',
	type: 'string'
},{
	name: 'areaCode',
	mapping: 'areaCode',
	type: 'string'
},{
	name: 'deptCode',
	mapping: 'deptCode',
	type: 'string'
},{
	name: 'empCode',
	mapping: 'empCode',
	type: 'string'
},{
	name: 'empName',
	mapping: 'empName',
	type: 'string'
},{
	name: 'positionType',
	mapping: 'positionType',
	type: 'string'
},{
	name: 'dutyName',
	mapping: 'dutyName',
	type: 'string'
},{
	name: 'personType',
	mapping: 'personType',
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
},{
	name: 'totalWorkTime',
	mapping: 'totalWorkTime',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: 'query_attendance.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'resultMap.root',
		totalProperty: 'resultMap.totalSize'
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

// 查询方法
var querySchedule = function(node) {
	var departmentCode = Ext.getCmp("query.branchCode").getValue();
	var empCode = Ext.getCmp('query.empCode').getValue();
	var empName = Ext.getCmp(CONDITION_EMPLOYEE_NAME).getValue();
	if (Ext.isEmpty(departmentCode) && Ext.isEmpty(empCode) && Ext.isEmpty(empName)) {
		Ext.Msg.alert("提示", "网点代码、员工工号、姓名必须要填写其中一项！");
		return;
	}

	if (Ext.isEmpty(departmentCode)) {
		departmentCode = "001";
	}

	if (Ext.getCmp('query.monthId').getValue() == "") {
		Ext.Msg.alert("提示", "请先选择月份！");
		return;
	}

	store.setBaseParam("DEPARTMENT_CODE", departmentCode);
	store.setBaseParam("MONTH_ID", Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Ym'));
	store.setBaseParam("EMP_CODE", empCode);
	store.setBaseParam(CONDITION_EMPLOYEE_NAME, empName);
	store.setBaseParam(CONDITION_EMPLOYEE_TYPE, Ext.getCmp("WORK_TYPE").getValue());
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
